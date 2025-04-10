package com.wevserver.security.webkey;

import com.nimbusds.jose.jwk.RSAKey;
import java.text.ParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "security_web_key")
public class WebKey {

    @Id
    @Column("web_key_id")
    private String id;

    @Version
    @Column("web_key_version")
    private Long version;

    @Column("web_key_key_id")
    private String keyId;

    @Column("web_key_key_json")
    private String keyJson;

    @Column("web_key_created_at")
    private Long createdAt;

    public RSAKey toRSAKey() {

        try {

            return RSAKey.parse(keyJson);

        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
