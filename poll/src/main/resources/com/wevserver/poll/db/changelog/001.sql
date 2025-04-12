CREATE TABLE "poll_poll" (
    "poll_id" CHAR(36) NOT NULL,
    "poll_version" INT NOT NULL,
    "poll_name" VARCHAR(256) NOT NULL,
    "poll_details" VARCHAR(65535) NOT NULL,
    "poll_start_at" BIGINT NOT NULL,
    "poll_end_at" CHAR(36) NOT NULL,
    "poll_created_at" BIGINT NOT NULL,
    "poll_created_by" CHAR(36) NOT NULL,
    "poll_updated_at" BIGINT NOT NULL,
    "poll_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "poll_poll_pk" PRIMARY KEY ("poll_id")
);