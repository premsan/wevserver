CREATE TABLE "broadcast_broadcast" (
    "id" CHAR(36) NOT NULL,
    "version" INT NOT NULL,
    "reference" VARCHAR(256) NOT NULL,
    "name" VARCHAR(256) NOT NULL,
    "url" VARCHAR(2048) NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "broadcast_broadcast_pk" PRIMARY KEY ("id")
);

CREATE INDEX "broadcast_broadcast_reference_idx" ON "broadcast_broadcast" ("reference");

