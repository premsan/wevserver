package com.wevserver.email;

import com.wevserver.api.EmailCreate;
import com.wevserver.application.feature.FeatureMapping;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EmailCreateController {

    public static final String CLIENT_REGISTRATION_ID = "google";

    public static final String GMAIL_SEND_URI =
            "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    private final RestClient restClient = RestClient.builder().build();
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    private final EmailRepository emailRepository;

    @FeatureMapping
    @GetMapping(EmailCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('EMAIL_CREATE')")
    public ModelAndView emailCreateGet(final EmailCreate.RequestParams requestParams) {

        final ModelAndView model = new ModelAndView("com/wevserver/email/templates/email-create");

        model.addAllObjects(requestParams.map());

        return model;
    }

    @PostMapping(EmailCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('EMAIL_CREATE')")
    public ModelAndView emailCreatePost(
            @Valid final EmailCreate.RequestParams requestParams,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext)
            throws MessagingException, IOException {

        if (bindingResult.hasErrors()) {

            final ModelAndView modelAndView =
                    new ModelAndView("com/wevserver/email/templates/email-create");
            modelAndView.addAllObjects(requestParams.map());

            return modelAndView;
        }

        final OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(
                        CLIENT_REGISTRATION_ID, securityContext.getAuthentication().getName());

        final MimeMessage mimeMessage =
                new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        mimeMessage.addRecipient(
                Message.RecipientType.TO, new InternetAddress(requestParams.getTo()));
        mimeMessage.setSubject(requestParams.getSubject());
        mimeMessage.setText(requestParams.getBody());

        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);

        final ResponseEntity<String> responseEntity =
                restClient
                        .method(HttpMethod.POST)
                        .uri(GMAIL_SEND_URI)
                        .headers(
                                SecurityHeaders.bearerToken(
                                        oAuth2AuthorizedClient.getAccessToken().getTokenValue()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(
                                new SendRequestBody(
                                        Base64.getEncoder().encodeToString(buffer.toByteArray())))
                        .retrieve()
                        .toEntity(String.class);

        final Map<String, String> providerAttributes = new HashMap<>();
        providerAttributes.put("response", responseEntity.toString());

        final Email email =
                emailRepository.save(
                        new Email(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getTo(),
                                requestParams.getTo(),
                                requestParams.getSubject(),
                                requestParams.getBody(),
                                EmailProvider.GMAIL,
                                providerAttributes,
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", email.getId());
        return new ModelAndView("redirect:/email/email-read/{id}");
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static final class SendRequestBody {

        private String raw;
    }
}
