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
@Table(name = "conversation_conversation_reply")
public class ConversationReply {

    @Id
    @Column("conversation_reply_id")
    private String id;

    @Version
    @Column("conversation_reply_version")
    private Long version;

    @Column("conversation_reply_conversation_id")
    private String conversationId;

    @Column("conversation_reply_details")
    private String details;

    @Column("conversation_reply_created_at")
    private Long createdAt;

    @Column("conversation_reply_created_by")
    private String createdBy;

    @Column("conversation_reply_updated_at")
    private Long updatedAt;

    @Column("conversation_reply_updated_by")
    private String updatedBy;
}
