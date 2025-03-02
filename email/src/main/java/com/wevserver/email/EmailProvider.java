package com.wevserver.email;

import com.wevserver.api.EmailCreate;
import com.wevserver.lib.FormData;

public interface EmailProvider {

    String name();

    FormData emailCreate(final EmailCreate.RequestParams requestParams);
}
