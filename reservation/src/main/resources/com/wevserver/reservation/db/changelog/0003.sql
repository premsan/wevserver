CREATE TABLE "reservation_reservation" (
    "reservation_id" CHAR(36) NOT NULL,
    "reservation_version" BIGINT NOT NULL,
    "reservation_plan_id" CHAR(36) NOT NULL,
    "reservation_name" VARCHAR(256) NOT NULL,
    "reservation_details" VARCHAR(65535) NOT NULL,
    "reservation_start_at" BIGINT NOT NULL,
    "reservation_end_at" BIGINT NOT NULL,
    "reservation_created_at" BIGINT NOT NULL,
    "reservation_created_by" CHAR(36) NOT NULL,
    "reservation_updated_at" BIGINT NOT NULL,
    "reservation_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "reservation_reservation_pk" PRIMARY KEY ("reservation_id"),
    CONSTRAINT "reservation_reservation_plan_id_fk" FOREIGN KEY ("reservation_plan_id") REFERENCES "reservation_reservation_plan" ("reservation_plan_id") ON DELETE CASCADE
);

CREATE INDEX "reservation_reservation_start_at_idx" ON "reservation_reservation" ("reservation_start_at");
CREATE INDEX "reservation_reservation_end_at_idx" ON "reservation_reservation" ("reservation_end_at");
CREATE INDEX "reservation_reservation_created_at_idx" ON "reservation_reservation" ("reservation_created_at");
CREATE INDEX "reservation_reservation_updated_at_idx" ON "reservation_reservation" ("reservation_updated_at");