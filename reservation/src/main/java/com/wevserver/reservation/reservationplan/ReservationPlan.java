package com.wevserver.reservation.reservationplan;

import java.time.temporal.ChronoUnit;
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
@Table(name = "reservation_reservation_plan")
public class ReservationPlan {

    @Id
    @Column("reservation_plan_id")
    private String id;

    @Version
    @Column("reservation_plan_version")
    private Long version;

    @Column("reservation_plan_resource_id")
    private String resourceId;

    @Column("reservation_plan_chrono_unit")
    private ChronoUnit chronoUnit;

    @Column("reservation_plan_min_unit")
    private Long minUnit;

    @Column("reservation_plan_max_unit")
    private Long maxUnit;

    @Column("reservation_plan_zone_id")
    private String zoneId;

    @Column("reservation_plan_start_at")
    private Long startAt;

    @Column("reservation_plan_end_at")
    private Long endAt;

    @Column("reservation_plan_created_at")
    private Long createdAt;

    @Column("reservation_plan_created_by")
    private String createdBy;

    @Column("reservation_plan_updated_at")
    private Long updatedAt;

    @Column("reservation_plan_updated_by")
    private String updatedBy;
}
