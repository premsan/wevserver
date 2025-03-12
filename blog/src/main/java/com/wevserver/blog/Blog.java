package com.wevserver.blog;

import com.wevserver.db.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "blog_blog")
public class Blog implements Auditable {

    @Id
    @Column("blog_id")
    private String id;

    @Version
    @Column("blog_version")
    private Long version;

    @Column("blog_name")
    private String name;

    @Column("blog_details")
    private String details;

    @Column("blog_created_at")
    private Long createdAt;

    @Column("blog_created_by")
    private String createdBy;

    @Column("blog_updated_at")
    private Long updatedAt;

    @Column("blog_updated_by")
    private String updatedBy;
}
