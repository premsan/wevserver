CREATE TABLE "conversation_conversation" (
    "id" CHAR(36) NOT NULL,
    "version" INT NOT NULL,
    "name" VARCHAR(256) NOT NULL,
    "updated_at" BIGINT NOT NULL,
    "updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "conversation_conversation_pk" PRIMARY KEY ("id")
);
