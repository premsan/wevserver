package com.wevserver.security;

import com.wevserver.api.GmailCreate;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

@Controller
@RequiredArgsConstructor
public class GmailCreateController {

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final RestClient restClient;

    @GetMapping("/mail-send")
    @PreAuthorize("isAuthenticated()")
    public String send(@CurrentSecurityContext final SecurityContext securityContext)
            throws MessagingException, IOException {

        final OAuth2AuthorizedClient oAuth2AuthorizedClient =
                oAuth2AuthorizedClientService.loadAuthorizedClient(
                        "google", securityContext.getAuthentication().getName());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.addRecipient(Message.RecipientType.TO, new InternetAddress("admin@premsan.com"));
        email.setSubject("<<email>>");
        email.setText("<<email>>");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();

        String encodedEmail = Base64.getEncoder().encodeToString(bytes);

        Map<String, String> body = new HashMap<>();
        body.put("raw", encodedEmail);

        ResponseEntity<String> response =
                restClient
                        .method(HttpMethod.POST)
                        .uri(GmailCreate.URI)
                        .header(
                                "Authorization",
                                "Bearer " + oAuth2AuthorizedClient.getAccessToken().getTokenValue())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body)
                        .retrieve()
                        .toEntity(String.class);

        return "ok";
    }
}
