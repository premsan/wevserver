package com.wevserver.reservation.reservationresource;

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
@Table(name = "reservation_reservation_resource")
public class ReservationResource {

    @Id
    @Column("reservation_resource_id")
    private String id;

    @Version
    @Column("reservation_resource_version")
    private Long version;

    @Column("reservation_resource_name")
    private String name;

    @Column("reservation_resource_details")
    private String details;

    @Column("reservation_resource_created_at")
    private Long createdAt;

    @Column("reservation_resource_created_by")
    private String createdBy;

    @Column("reservation_resource_updated_at")
    private Long updatedAt;

    @Column("reservation_resource_updated_by")
    private String updatedBy;
}
