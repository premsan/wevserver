CREATE TABLE "conversation_reply" (
    "id" CHAR(36) NOT NULL,
    "version" INT NOT NULL,
    "conversation_id" VARCHAR(36) NOT NULL,
    "description" VARCHAR(65535) NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "conversation_reply_pk" PRIMARY KEY ("id")
);

CREATE INDEX "conversation_reply_conversation_id_idx" ON "conversation_reply" ("conversation_id");