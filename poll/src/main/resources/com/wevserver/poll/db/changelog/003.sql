CREATE TABLE "poll_poll_vote" (
    "poll_vote_id" VARCHAR(256) NOT NULL,
    "poll_vote_version" INT NOT NULL,
    "poll_vote_poll_option_id" VARCHAR(256) NOT NULL,
    "poll_vote_vote_by" VARCHAR(256) NOT NULL,
    "poll_vote_created_at" BIGINT NOT NULL,
    "poll_vote_created_by" CHAR(36) NOT NULL,
    "poll_vote_updated_at" BIGINT NOT NULL,
    "poll_vote_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "poll_poll_vote_pk" PRIMARY KEY ("poll_vote_id")
);

CREATE INDEX "poll_poll_vote_poll_option_id_idx" ON "poll_poll_vote" ("poll_vote_poll_option_id");