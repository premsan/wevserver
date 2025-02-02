CREATE TABLE "peer_peer" (
    "id" CHAR(36) NOT NULL,
    "version" BIGINT NOT NULL,
    "host" VARCHAR(256) NOT NULL,
    "path" VARCHAR(256) NOT NULL,
    "inbound" BOOLEAN DEFAULT FALSE NOT NULL,
    "outbound" BOOLEAN DEFAULT FALSE NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "peer_peer_pk" PRIMARY KEY ("id")
);

CREATE INDEX "peer_peer_path_idx" ON "peer_peer" ("path");