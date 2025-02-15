CREATE TABLE "broadcast_broadcast_server" (
    "id" CHAR(36) NOT NULL,
    "version" INT NOT NULL,
    "name" VARCHAR(256) NOT NULL,
    "url" VARCHAR(2048) NOT NULL,
    "username" VARCHAR(256) NOT NULL,
    "password" VARCHAR(256) NOT NULL,
    "enabled" BOOLEAN DEFAULT FALSE NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "broadcast_broadcast_server_pk" PRIMARY KEY ("id")
);
