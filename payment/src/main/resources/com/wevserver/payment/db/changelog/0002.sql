CREATE TABLE "payment_payment_attempt" (
    "payment_attempt_id" CHAR(36) NOT NULL,
    "payment_attempt_version" BIGINT NOT NULL,
    "payment_attempt_payment_id" CHAR(36) NOT NULL,
    "payment_attempt_gateway_id" VARCHAR(256) NOT NULL,
    "payment_attempt_gateway_attempt_id" VARCHAR(256),
    "payment_attempt_gateway_attempt_url" VARCHAR(2048),
    "payment_attempt_gateway_attempt_attributes" VARCHAR(65535),
    "payment_attempt_status" VARCHAR(256),
    "payment_attempt_created_at" BIGINT NOT NULL,
    "payment_attempt_created_by" CHAR(36) NOT NULL,
    "payment_attempt_updated_at" BIGINT NOT NULL,
    "payment_attempt_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "payment_payment_attempt_pk" PRIMARY KEY ("payment_attempt_id"),
    CONSTRAINT "payment_payment_attempt_payment_id_fk" FOREIGN KEY ("payment_attempt_payment_id") REFERENCES "payment_payment" ("payment_id") ON DELETE CASCADE
);

CREATE INDEX "payment_payment_attempt_created_at_idx" ON "payment_payment_attempt" ("payment_attempt_created_at");
CREATE INDEX "payment_payment_attempt_updated_at_idx" ON "payment_payment_attempt" ("payment_attempt_updated_at");