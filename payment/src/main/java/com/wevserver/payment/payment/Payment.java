package com.wevserver.payment.payment;

import com.wevserver.db.Auditable;
import java.math.BigDecimal;
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
@Table(name = "payment_payment")
public class Payment implements Auditable {

    @Id
    @Column("payment_id")
    private String id;

    @Version
    @Column("payment_version")
    private Long version;

    @Column("payment_reference_id")
    private String referenceId;

    @Column("payment_currency")
    private String currency;

    @Column("payment_amount")
    private BigDecimal amount;

    @Column("payment_name")
    private String name;

    @Column("payment_details")
    private String details;

    @Column("payment_created_at")
    private Long createdAt;

    @Column("payment_created_by")
    private String createdBy;

    @Column("payment_updated_at")
    private Long updatedAt;

    @Column("payment_updated_by")
    private String updatedBy;
}
