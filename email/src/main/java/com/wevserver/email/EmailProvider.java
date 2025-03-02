package com.wevserver.email;

import com.wevserver.api.EmailCreate;
import org.springframework.util.MultiValueMap;

public interface EmailProvider {

    String name();

    MultiValueMap<String, String> emailCreate(final EmailCreate.RequestParams requestParams);
}
