package by.tms.twitterapiprojectc38onl.repository;

import by.tms.twitterapiprojectc38onl.entity.Reaction;
import by.tms.twitterapiprojectc38onl.entity.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByAccountIdAndPostId(Long accountId, Long postId);

    long countByPostIdAndType(Long postId, ReactionType type);
}
