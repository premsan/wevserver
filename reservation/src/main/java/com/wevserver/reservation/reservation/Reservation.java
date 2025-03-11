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
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("plan_id")
    private String planId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("start_at")
    private Long startAt;

    @Column("end_at")
    private Long endAt;

    @Column("created_at")
    private Long createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
