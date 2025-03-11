package com.wevserver.conversation.conversation;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository
        extends AuditableRepository<Conversation>,
                CrudRepository<Conversation, String>,
                PagingAndSortingRepository<Conversation, String> {

    @Override
    default Class<Conversation> entityClass() {

        return Conversation.class;
    }

    Page<Conversation> findAll(final Pageable pageable);

    Page<Conversation> findByNameStartingWith(
            final String nameStartingWith, final Pageable pageable);
}
