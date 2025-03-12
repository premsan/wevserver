CREATE TABLE "security_web_key" (
    "web_key_id" CHAR(36) NOT NULL,
    "web_key_version" BIGINT NOT NULL,
    "web_key_key_id" VARCHAR(36) NOT NULL,
    "web_key_key_json" VARCHAR(65535) NOT NULL,
    "web_key_created_at" BIGINT NOT NULL,
    CONSTRAINT "security_web_key_pk" PRIMARY KEY ("web_key_id")
);

CREATE INDEX "security_web_key_key_id_idx" ON "security_web_key" ("web_key_id");