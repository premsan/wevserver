package com.wevserver.scheduled;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scheduled_scheduled_job_configuration")
public class ScheduledJobConfiguration {

    @Id
    @Column("scheduled_job_configuration_id")
    private String id;

    @Version
    @Column("scheduled_job_configuration_version")
    private Long version;

    @Column("scheduled_job_configuration_chrono_unit")
    private ChronoUnit chronoUnit;

    @Column("scheduled_job_configuration_period")
    private Long period;

    @Column("scheduled_job_configuration_attributes")
    private Map<String, String> attributes;
}
