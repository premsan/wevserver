CREATE TABLE "security_authority" (
    "authority_id" CHAR(36) NOT NULL,
    "authority_version" INT NOT NULL,
    "authority_name" VARCHAR(256) NOT NULL,
    "authority_created_at" BIGINT NOT NULL,
    "authority_created_by" CHAR(36) NOT NULL,
    "authority_updated_at" BIGINT NOT NULL,
    "authority_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_authority_pk" PRIMARY KEY ("authority_id")
);

CREATE UNIQUE INDEX "security_authority_name_idx" ON "security_authority" ("authority_name");
CREATE INDEX "security_authority_created_at_idx" ON "security_authority" ("authority_created_at");
CREATE INDEX "security_authority_updated_at_idx" ON "security_authority" ("authority_updated_at");

CREATE TABLE "security_role" (
    "role_id" CHAR(36) NOT NULL,
    "role_version" INT NOT NULL,
    "role_name" VARCHAR(256) NOT NULL,
    "role_updated_at" BIGINT NOT NULL,
    "role_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_role_pk" PRIMARY KEY ("role_id")
);

CREATE UNIQUE INDEX "security_role_name_idx" ON "security_role" ("role_name");

CREATE TABLE "security_role_authority" (
    "role_authority_id" CHAR(36) NOT NULL,
    "role_authority_version" INT NOT NULL,
    "role_authority_role_id" CHAR(36) NOT NULL,
    "role_authority_authority_id" CHAR(36) NOT NULL,
    "role_authority_updated_at" BIGINT NOT NULL,
    "role_authority_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_role_authority_pk" PRIMARY KEY ("role_authority_id"),
    CONSTRAINT "security_role_authority_role_id_fk" FOREIGN KEY ("role_authority_authority_id") REFERENCES "security_role" ("role_id") ON DELETE CASCADE,
    CONSTRAINT "security_role_authority_authority_id_fk" FOREIGN KEY ("role_authority_authority_id") REFERENCES "security_authority" ("authority_id") ON DELETE CASCADE
);

CREATE INDEX "security_role_authority_role_id_idx" ON "security_role_authority" ("role_authority_role_id");
CREATE INDEX "security_role_authority_authority_id_idx" ON "security_role_authority" ("role_authority_authority_id");
CREATE UNIQUE INDEX "security_role_authority_role_id_authority_id_idx" ON "security_role_authority" ("role_authority_role_id", "role_authority_authority_id");

CREATE TABLE "security_user" (
    "user_id" CHAR(36) NOT NULL,
    "user_version" INT NOT NULL,
    "user_owner_id" CHAR(36),
    "user_email" VARCHAR(256),
    "user_password_hash" VARCHAR(256),
    "user_disabled" BOOLEAN DEFAULT FALSE NOT NULL,
    "user_country" VARCHAR(36),
    "user_language" VARCHAR(36),
    "user_time_zone" VARCHAR(36),
    "user_created_at" BIGINT NOT NULL,
    "user_created_by" CHAR(36) NOT NULL,
    "user_updated_at" BIGINT NOT NULL,
    "user_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_user_pk" PRIMARY KEY ("user_id"),
    CONSTRAINT "security_user_owner_id_idx" FOREIGN KEY ("user_owner_id") REFERENCES "security_user" ("user_id") ON DELETE CASCADE
);

CREATE INDEX "security_user_idx1" ON "security_user" ("user_owner_id");
CREATE UNIQUE INDEX "security_user_email_idx" ON "security_user" ("user_email");
CREATE INDEX "security_user_created_at_idx" ON "security_user" ("user_created_at");
CREATE INDEX "security_user_updated_at_idx" ON "security_user" ("user_updated_at");

CREATE TABLE "security_user_role" (
    "user_role_id" CHAR(36) NOT NULL,
    "user_role_version" INT NOT NULL,
    "user_role_user_id" CHAR(36) NOT NULL,
    "user_role_role_id" CHAR(36) NOT NULL,
    "user_role_updated_at" BIGINT NOT NULL,
    "user_role_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "security_user_role_pk" PRIMARY KEY ("user_role_id"),
    CONSTRAINT "security_user_role_user_id_fk" FOREIGN KEY ("user_role_user_id") REFERENCES "security_user" ("user_id") ON DELETE CASCADE,
    CONSTRAINT "security_user_role_role_id_fk" FOREIGN KEY ("user_role_role_id") REFERENCES "security_role" ("role_id") ON DELETE CASCADE
);

CREATE UNIQUE INDEX "security_user_role_user_id_role_id_idx" ON "security_user_role" ("user_role_user_id", "user_role_role_id");