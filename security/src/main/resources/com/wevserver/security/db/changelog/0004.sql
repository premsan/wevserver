CREATE TABLE "security_peer" (
    "peer_id" CHAR(36) NOT NULL,
    "peer_version" BIGINT NOT NULL,
    "peer_host" VARCHAR(256) NOT NULL,
    "peer_path" VARCHAR(256) NOT NULL,
    "peer_inbound" BOOLEAN DEFAULT FALSE NOT NULL,
    "peer_outbound" BOOLEAN DEFAULT FALSE NOT NULL,
    "peer_updated_at" BIGINT NOT NULL,
    "peer_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_peer_pk" PRIMARY KEY ("peer_id")
);

CREATE INDEX "security_peer_path_idx" ON "security_peer" ("peer_path");