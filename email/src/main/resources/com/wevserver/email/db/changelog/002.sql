CREATE TABLE "email_email_digest" (
    "email_digest_id" CHAR(36) NOT NULL,
    "email_digest_version" INT NOT NULL,
    "email_digest_principal_name" VARCHAR(256) NOT NULL,
    "email_digest_body" VARCHAR(65535),
    "email_digest_sent_at" BIGINT NOT NULL,
    "email_digest_created_at" BIGINT NOT NULL,
    "email_digest_created_by" CHAR(36) NOT NULL,
    "email_digest_updated_at" BIGINT NOT NULL,
    "email_digest_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "email_email_digest_pk" PRIMARY KEY ("email_digest_id")
);

CREATE INDEX "email_email_digest_principal_name_idx" ON "email_email_digest" ("email_digest_principal_name");
CREATE INDEX "email_email_digest_sent_at_idx" ON "email_email_digest" ("email_digest_id");
CREATE INDEX "email_email_digest_created_at_idx" ON "email_email_digest" ("email_digest_created_at");
CREATE INDEX "email_email_digest_updated_at_idx" ON "email_email_digest" ("email_digest_updated_at");