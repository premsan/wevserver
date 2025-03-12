CREATE TABLE "conversation_conversation_reply" (
    "conversation_reply_id" CHAR(36) NOT NULL,
    "conversation_reply_version" INT NOT NULL,
    "conversation_reply_conversation_id" VARCHAR(36) NOT NULL,
    "conversation_reply_details" VARCHAR(65535) NOT NULL,
    "conversation_reply_created_at" BIGINT NOT NULL,
    "conversation_reply_created_by" CHAR(36) NOT NULL,
    "conversation_reply_updated_at" BIGINT NOT NULL,
    "conversation_reply_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "conversation_conversation_reply_pk" PRIMARY KEY ("conversation_reply_id"),
    CONSTRAINT "conversation_conversation_reply_conversation_id_fk" FOREIGN KEY ("conversation_reply_conversation_id") REFERENCES "conversation_conversation" ("conversation_id") ON DELETE CASCADE
);

CREATE INDEX "conversation_conversation_reply_created_at_idx" ON "conversation_conversation_reply" ("conversation_reply_created_at");
CREATE INDEX "conversation_conversation_reply_updated_at_idx" ON "conversation_conversation_reply" ("conversation_reply_updated_at");