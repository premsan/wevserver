package com.wevserver.email;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProviderLocator {

    private final List<EmailProvider> providers;

    public EmailProvider emailProvider(final String name) {

        for (final EmailProvider provider : providers) {

            if (provider.name().equalsIgnoreCase(name)) {

                return provider;
            }
        }

        return null;
    }
}
