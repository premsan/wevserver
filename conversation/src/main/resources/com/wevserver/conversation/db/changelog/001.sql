CREATE TABLE "conversation_conversation" (
    "conversation_id" CHAR(36) NOT NULL,
    "conversation_version" INT NOT NULL,
    "conversation_name" VARCHAR(256) NOT NULL,
    "conversation_created_at" BIGINT NOT NULL,
    "conversation_created_by" CHAR(36) NOT NULL,
    "conversation_updated_at" BIGINT NOT NULL,
    "conversation_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "conversation_conversation_pk" PRIMARY KEY ("conversation_id")
);

CREATE INDEX "conversation_conversation_created_at_idx" ON "conversation_conversation" ("conversation_created_at");
CREATE INDEX "conversation_conversation_updated_at_idx" ON "conversation_conversation" ("conversation_updated_at");