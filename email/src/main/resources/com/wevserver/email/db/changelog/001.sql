CREATE TABLE "email_email" (
    "email_id" CHAR(36) NOT NULL,
    "email_version" INT NOT NULL,
    "email_from" VARCHAR(256) NOT NULL,
    "email_to" VARCHAR(256) NOT NULL,
    "email_subject" VARCHAR(256) NOT NULL,
    "email_body" VARCHAR(256) NOT NULL,
    "email_provider" VARCHAR(256) NOT NULL,
    "email_provider_data" VARCHAR(65535),
    "email_created_at" BIGINT NOT NULL,
    "email_created_by" CHAR(36) NOT NULL,
    "email_updated_at" BIGINT NOT NULL,
    "email_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "email_email_pk" PRIMARY KEY ("email_id")
);

CREATE INDEX "email_email_created_at_idx" ON "email_email" ("email_created_at");
CREATE INDEX "email_email_updated_at_idx" ON "email_email" ("email_updated_at");