package com.wevserver.reservation.reservation;

import com.wevserver.db.Auditable;
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
@Table(name = "reservation_reservation")
public class Reservation implements Auditable {

    @Id
    @Column("reservation_id")
    private String id;

    @Version
    @Column("reservation_version")
    private Long version;

    @Column("reservation_plan_id")
    private String planId;

    @Column("reservation_name")
    private String name;

    @Column("reservation_details")
    private String details;

    @Column("reservation_start_at")
    private Long startAt;

    @Column("reservation_end_at")
    private Long endAt;

    @Column("reservation_created_at")
    private Long createdAt;

    @Column("reservation_created_by")
    private String createdBy;

    @Column("reservation_updated_at")
    private Long updatedAt;

    @Column("reservation_updated_by")
    private String updatedBy;
}
