CREATE TABLE "scheduled_scheduled_job_configuration" (
    "scheduled_job_configuration_id" CHAR(36) NOT NULL,
    "scheduled_job_configuration_version" INT NOT NULL,
    "scheduled_job_configuration_chrono_unit" VARCHAR(256) NOT NULL,
    "scheduled_job_configuration_period" BIGINT NOT NULL,
    "scheduled_job_configuration_attributes" VARCHAR(65535) NOT NULL,
    "scheduled_job_configuration_updated_at" BIGINT NOT NULL,
    "scheduled_job_configuration_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "scheduled_scheduled_job_configuration_pk" PRIMARY KEY ("scheduled_job_configuration_id")
);