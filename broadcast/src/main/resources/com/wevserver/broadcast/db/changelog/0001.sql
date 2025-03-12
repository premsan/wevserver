CREATE TABLE "broadcast_broadcast" (
    "broadcast_id" CHAR(36) NOT NULL,
    "broadcast_version" INT NOT NULL,
    "broadcast_name" VARCHAR(256) NOT NULL,
    "broadcast_url" VARCHAR(2048) NOT NULL,
    "broadcast_created_at" BIGINT NOT NULL,
    "broadcast_created_by" CHAR(36) NOT NULL,
    "broadcast_updated_at" BIGINT NOT NULL,
    "broadcast_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "broadcast_broadcast_pk" PRIMARY KEY ("broadcast_id")
);

CREATE INDEX "broadcast_broadcast_created_at_idx" ON "broadcast_broadcast" ("broadcast_created_at");
CREATE INDEX "broadcast_broadcast_updated_at_idx" ON "broadcast_broadcast" ("broadcast_updated_at");