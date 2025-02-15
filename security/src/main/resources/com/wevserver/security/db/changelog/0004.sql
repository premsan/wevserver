CREATE TABLE "security_peer" (
    "id" CHAR(36) NOT NULL,
    "version" BIGINT NOT NULL,
    "host" VARCHAR(256) NOT NULL,
    "path" VARCHAR(256) NOT NULL,
    "inbound" BOOLEAN DEFAULT FALSE NOT NULL,
    "outbound" BOOLEAN DEFAULT FALSE NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_peer_pk" PRIMARY KEY ("id")
);

CREATE INDEX "security_peer_path_idx" ON "security_peer" ("path");