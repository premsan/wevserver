CREATE TABLE "reservation_reservation_plan" (
    "reservation_plan_id" CHAR(36) NOT NULL,
    "reservation_plan_version" BIGINT NOT NULL,
    "reservation_plan_resource_id" CHAR(36) NOT NULL,
    "reservation_plan_chrono_unit" VARCHAR(256) NOT NULL,
    "reservation_plan_min_unit" BIGINT NOT NULL,
    "reservation_plan_max_unit" BIGINT NOT NULL,
    "reservation_plan_zone_id" VARCHAR(256) NOT NULL,
    "reservation_plan_start_at" BIGINT NOT NULL,
    "reservation_plan_end_at" BIGINT NOT NULL,
    "reservation_plan_created_at" BIGINT NOT NULL,
    "reservation_plan_created_by" CHAR(36) NOT NULL,
    "reservation_plan_updated_at" BIGINT NOT NULL,
    "reservation_plan_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "reservation_reservation_plan_pk" PRIMARY KEY ("reservation_plan_id"),
    CONSTRAINT "reservation_reservation_plan_resource_id_fk" FOREIGN KEY ("reservation_plan_resource_id") REFERENCES "reservation_reservation_resource" ("reservation_resource_id") ON DELETE CASCADE
);

CREATE INDEX "reservation_reservation_plan_created_at_idx" ON "reservation_reservation_plan" ("reservation_plan_created_at");
CREATE INDEX "reservation_reservation_plan_updated_at_idx" ON "reservation_reservation_plan" ("reservation_plan_updated_at");