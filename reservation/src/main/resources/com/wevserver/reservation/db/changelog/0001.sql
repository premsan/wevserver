CREATE TABLE "reservation_reservation_resource" (
    "reservation_resource_id" CHAR(36) NOT NULL,
    "reservation_resource_version" BIGINT NOT NULL,
    "reservation_resource_name" VARCHAR(256) NOT NULL,
    "reservation_resource_details" VARCHAR(65535) NOT NULL,
    "reservation_resource_created_at" BIGINT NOT NULL,
    "reservation_resource_created_by" CHAR(36) NOT NULL,
    "reservation_resource_updated_at" BIGINT NOT NULL,
    "reservation_resource_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "reservation_reservation_resource_pk" PRIMARY KEY ("reservation_resource_id")
);

CREATE INDEX "reservation_reservation_resource_created_at_idx" ON "reservation_reservation_resource" ("reservation_resource_created_at");
CREATE INDEX "reservation_reservation_resource_updated_at_idx" ON "reservation_reservation_resource" ("reservation_resource_updated_at");