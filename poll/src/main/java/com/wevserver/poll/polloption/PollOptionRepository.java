package com.wevserver.poll.polloption;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PollOptionRepository
        extends CrudRepository<PollOption, String>,
                PagingAndSortingRepository<PollOption, String>,
                QueryByExampleExecutor<PollOption> {

    List<PollOption> findByPollId(String pollId);
}
