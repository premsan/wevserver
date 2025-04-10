CREATE TABLE "poll_poll_option" (
    "poll_option_id" VARCHAR(256) NOT NULL,
    "poll_option_version" INT NOT NULL,
    "poll_option_poll_id" VARCHAR(256) NOT NULL,
    "poll_option_name" VARCHAR(256) NOT NULL,
    "poll_option_created_at" BIGINT NOT NULL,
    "poll_option_created_by" CHAR(36) NOT NULL,
    CONSTRAINT "poll_poll_option_pk" PRIMARY KEY ("poll_option_id")
);

CREATE INDEX "poll_poll_option_poll_id_idx" ON "poll_poll_option" ("poll_option_poll_id");