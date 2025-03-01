package com.wevserver.security.email;

import com.wevserver.api.GmailCreate;
import com.wevserver.application.feature.FeatureMapping;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class GmailCreateController {

    public static final String CLIENT_REGISTRATION_ID = "google";

    public static final String GMAIL_SEND_URI =
            "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    private final RestClient restClient;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @FeatureMapping
    @GetMapping(GmailCreate.PATH)
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('MAIL_GMAIL_CREATE')")
    public ModelAndView gmailCreateGet(final GmailCreate.RequestParams requestParams) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/security/templates/gmail-create");

        model.addAllObjects(requestParams.map());

        return model;
    }

    @PostMapping(GmailCreate.PATH)
    //    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('MAIL_GMAIL_CREATE')")
    public String gmailCreatePost(
            final GmailCreate.RequestParams requestParams,
            @CurrentSecurityContext final SecurityContext securityContext)
            throws MessagingException, IOException {

        final OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(
                        CLIENT_REGISTRATION_ID, securityContext.getAuthentication().getName());

        final MimeMessage email =
                new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        email.addRecipient(Message.RecipientType.TO, new InternetAddress(requestParams.getTo()));
        email.setSubject(requestParams.getSubject());
        email.setText(requestParams.getContent());

        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);

        final ResponseEntity<String> response =
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

        return response.toString();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static final class SendRequestBody {

        private String raw;
    }
}
