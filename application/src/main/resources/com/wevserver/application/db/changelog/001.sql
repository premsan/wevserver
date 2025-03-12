CREATE TABLE "application_entity_audit" (
    "entity_audit_id" CHAR(36) NOT NULL,
    "entity_audit_version" INT NOT NULL,
    "entity_audit_principal_name" VARCHAR(256) NOT NULL,
    "entity_audit_entity_name" VARCHAR(256) NOT NULL,
    "entity_audit_created_at" BIGINT NOT NULL,
    "entity_audit_notified" BOOLEAN DEFAULT FALSE,
    "entity_audit_accessed_at" BIGINT NOT NULL,
    "entity_audit_created_count" BIGINT NOT NULL,
    "entity_audit_updated_count" BIGINT NOT NULL,
    CONSTRAINT "application_entity_audit_pk" PRIMARY KEY ("entity_audit_id")
);

CREATE UNIQUE INDEX "application_entity_audit_principal_name_entity_name_idx" ON "application_entity_audit" ("entity_audit_principal_name", "entity_audit_entity_name");