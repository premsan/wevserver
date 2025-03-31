package com.wevserver.email;

import com.wevserver.api.EmailCreate;
import com.wevserver.lib.FormData;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class EmailProviderGmail implements EmailProvider {

    public static final String CLIENT_REGISTRATION_ID = "google";

    public static final String GMAIL_SEND_URI =
            "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    private final RestClient restClient = RestClient.builder().build();
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public String name() {

        return "Gmail";
    }

    @Override
    public FormData emailCreate(
            final String principalName, final EmailCreate.RequestParams requestParams) {

        final OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(
                        CLIENT_REGISTRATION_ID, principalName);

        if (oAuth2AuthorizedClient.getAccessToken() == null) {

            return null;
        }

        final byte[] messageBytes;

        try {
            final MimeMessage mimeMessage =
                    new MimeMessage(Session.getDefaultInstance(new Properties(), null));

            mimeMessage.addRecipient(
                    Message.RecipientType.TO, new InternetAddress(requestParams.getTo()));

            mimeMessage.setSubject(requestParams.getSubject());
            mimeMessage.setText(requestParams.getBody());

            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            mimeMessage.writeTo(buffer);

            messageBytes = buffer.toByteArray();

        } catch (final MessagingException | IOException e) {

            throw new RuntimeException(e);
        }

        final ResponseEntity<Map<String, String>> responseEntity =
                restClient
                        .method(HttpMethod.POST)
                        .uri(GMAIL_SEND_URI)
                        .headers(
                                SecurityHeaders.bearerToken(
                                        oAuth2AuthorizedClient.getAccessToken().getTokenValue()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new SendRequestBody(Base64.getEncoder().encodeToString(messageBytes)))
                        .retrieve()
                        .toEntity(new ParameterizedTypeReference<>() {});

        return new FormData(responseEntity.getBody());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static final class SendRequestBody {

        private String raw;
    }
}
