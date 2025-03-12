package com.wevserver.email;

import com.wevserver.api.EmailCreate;
import com.wevserver.lib.FormData;
import com.wevserver.security.user.User;
import com.wevserver.security.user.UserRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailDigestTask {

    private static final Integer EMAIL_DIGEST_INTERVAL_MS = 30 * 60 * 1000;
    private static final String EMAIL_PROVIDER = "Gmail";

    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final EmailProviderLocator emailProviderLocator;
    private final EmailDigestRepository emailDigestRepository;

    @Scheduled(fixedDelay = 60 * 1000)
    public void doTask() {

        while (true) {

            final EmailProvider emailProvider = emailProviderLocator.emailProvider(EMAIL_PROVIDER);

            if (emailProvider == null) {

                return;
            }

            final EmailDigest emailDigest =
                    emailDigestRepository
                            .findTopBySentAtLessThanEqualAndBodyIsNotNullOrderBySentAtAsc(
                                    System.currentTimeMillis() - EMAIL_DIGEST_INTERVAL_MS);

            if (emailDigest == null) {

                break;
            }

            final FormData digestBody = emailDigest.getBody();
            final Long sentAt = emailDigest.getSentAt();

            emailDigest.setSentAt(System.currentTimeMillis());
            emailDigest.setBody(null);
            emailDigestRepository.save(emailDigest);

            final Optional<User> userOptional =
                    userRepository.findById(emailDigest.getPrincipalName());

            if (userOptional.isEmpty()) {

                continue;
            }

            final EmailCreate.RequestParams requestParams = new EmailCreate.RequestParams();
            requestParams.setTo(userOptional.get().getEmail());
            requestParams.setSubject(
                    "Digest: " + Instant.ofEpochMilli(sentAt) + " " + Instant.now());
            requestParams.setBody(getBody(digestBody));
            requestParams.setProvider("Gmail");

            emailProvider.emailCreate(emailDigest.getPrincipalName(), requestParams);
        }
    }

    private String getBody(final FormData formData) {

        final StringBuilder stringBuilder = new StringBuilder();

        for (final String key : formData.getData().keySet()) {

            try {

                final String message =
                        messageSource.getMessage(
                                key,
                                formData.getData().get(key).toArray(),
                                LocaleContextHolder.getLocale());

                stringBuilder.append(message).append(System.lineSeparator());

            } catch (final NoSuchMessageException exception) {

                stringBuilder.append(key).append(System.lineSeparator());
            }
        }

        return stringBuilder.toString();
    }
}
