CREATE TABLE "broadcast_broadcast_server" (
    "broadcast_server_id" CHAR(36) NOT NULL,
    "broadcast_server_version" INT NOT NULL,
    "broadcast_server_name" VARCHAR(256) NOT NULL,
    "broadcast_server_url" VARCHAR(2048) NOT NULL,
    "broadcast_server_username" VARCHAR(256) NOT NULL,
    "broadcast_server_password" VARCHAR(256) NOT NULL,
    "broadcast_server_enabled" BOOLEAN DEFAULT FALSE NOT NULL,
    "broadcast_server_created_at" BIGINT NOT NULL,
    "broadcast_server_created_by" CHAR(36) NOT NULL,
    "broadcast_server_updated_at" BIGINT NOT NULL,
    "broadcast_server_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "broadcast_broadcast_server_pk" PRIMARY KEY ("broadcast_server_id")
);

CREATE INDEX "broadcast_broadcast_server_created_at_idx" ON "broadcast_broadcast_server" ("broadcast_server_created_at");
CREATE INDEX "broadcast_broadcast_server_updated_at_idx" ON "broadcast_broadcast_server" ("broadcast_server_updated_at");