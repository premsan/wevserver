CREATE TABLE "conversation_conversation" (
    "id" CHAR(36) NOT NULL,
    "version" INT NOT NULL,
    "name" VARCHAR(256) NOT NULL,
    "created_at" BIGINT NOT NULL,
    "created_by" CHAR(36) NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "conversation_conversation_pk" PRIMARY KEY ("id")
);

CREATE INDEX "conversation_conversation_created_at_idx" ON "conversation_conversation" ("created_at");
CREATE INDEX "conversation_conversation_updated_at_idx" ON "conversation_conversation" ("updated_at");