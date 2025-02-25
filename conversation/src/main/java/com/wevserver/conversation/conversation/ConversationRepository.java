package com.wevserver.conversation.conversation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository
        extends CrudRepository<Conversation, String>,
                PagingAndSortingRepository<Conversation, String> {

    Page<Conversation> findAll(final Pageable pageable);

    Page<Conversation> findByNameStartingWith(
            final String nameStartingWith, final Pageable pageable);
}
