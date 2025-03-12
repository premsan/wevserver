package com.wevserver.payment.paymentattempt;

import java.util.Map;
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
@Table(name = "payment_payment_attempt")
public class PaymentAttempt {

    @Id
    @Column("payment_attempt_id")
    private String id;

    @Version
    @Column("payment_attempt_version")
    private Long version;

    @Column("payment_attempt_payment_id")
    private String paymentId;

    @Column("payment_attempt_gateway_id")
    private String gatewayId;

    @Column("payment_attempt_gateway_attempt_id")
    private String gatewayAttemptId;

    @Column("payment_attempt_gateway_attempt_url")
    private String gatewayAttemptUrl;

    @Column("payment_attempt_gateway_attempt_attributes")
    private Map<String, String> gatewayAttemptAttributes;

    @Column("payment_attempt_status")
    private PaymentAttemptStatus status;

    @Column("payment_attempt_created_at")
    private Long createdAt;

    @Column("payment_attempt_created_by")
    private String createdBy;

    @Column("payment_attempt_updated_at")
    private Long updatedAt;

    @Column("payment_attempt_updated_by")
    private String updatedBy;
}
