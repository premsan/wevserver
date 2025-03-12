CREATE TABLE "blog_blog" (
    "blog_id" CHAR(36) NOT NULL,
    "blog_version" INT NOT NULL,
    "blog_name" VARCHAR(256) NOT NULL,
    "blog_details" VARCHAR(65535) NOT NULL,
    "blog_created_at" BIGINT NOT NULL,
    "blog_created_by" CHAR(36) NOT NULL,
    "blog_updated_at" BIGINT NOT NULL,
    "blog_updated_by" CHAR(36) NOT NULL,
    CONSTRAINT "blog_blog_pk" PRIMARY KEY ("blog_id")
);

CREATE INDEX "blog_blog_created_at_idx" ON "blog_blog" ("blog_created_at");
CREATE INDEX "blog_blog_updated_at_idx" ON "blog_blog" ("blog_updated_at");