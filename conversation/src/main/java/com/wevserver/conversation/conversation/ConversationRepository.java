package com.wevserver.conversation.conversation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, String> {}
