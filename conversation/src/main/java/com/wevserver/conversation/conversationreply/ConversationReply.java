package com.wevserver.conversation.conversationreply;

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
@Table(name = "conversation_reply")
public class ConversationReply {

    @Id
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("conversation_id")
    private String conversationId;

    @Column("description")
    private String description;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
