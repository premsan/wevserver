package com.wevserver.conversation.conversation;

import com.wevserver.db.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversation_conversation")
public class Conversation implements Auditable {

    @Id
    @Column("conversation_id")
    private String id;

    @Version
    @Column("conversation_version")
    private Long version;

    @Column("conversation_name")
    private String name;

    @Column("conversation_created_at")
    private Long createdAt;

    @Column("conversation_created_by")
    private String createdBy;

    @Column("conversation_updated_at")
    private Long updatedAt;

    @Column("conversation_updated_by")
    private String updatedBy;
}
