package com.wevserver.conversation.conversationreply;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationReplyRepository extends CrudRepository<ConversationReply, String> {}
