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
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("reference_id")
    private String referenceId;

    @Column("currency")
    private String currency;

    @Column("amount")
    private BigDecimal amount;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("created_at")
    private Long createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
