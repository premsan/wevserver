CREATE TABLE "payment_payment" (
    "payment_id" CHAR(36) NOT NULL,
    "payment_version" BIGINT NOT NULL,
    "payment_reference_id" CHAR(36) NOT NULL,
    "payment_currency" VARCHAR(3) NOT NULL,
    "payment_amount" DECIMAL(36, 18) NOT NULL,
    "payment_name" VARCHAR(256) NOT NULL,
    "payment_details" VARCHAR(65535) NOT NULL,
    "payment_created_at" BIGINT NOT NULL,
    "payment_created_by" CHAR(36) NOT NULL,
    "payment_updated_at" BIGINT NOT NULL,
    "payment_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "payment_payment_pk" PRIMARY KEY ("payment_id")
);

CREATE INDEX "payment_payment_reference_id_idx" ON "payment_payment" ("payment_reference_id");
CREATE INDEX "payment_payment_created_at_idx" ON "payment_payment" ("payment_created_at");
CREATE INDEX "payment_payment_updated_at_idx" ON "payment_payment" ("payment_updated_at");