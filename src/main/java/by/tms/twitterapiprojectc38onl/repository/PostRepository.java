package by.tms.twitterapiprojectc38onl.repository;

import by.tms.twitterapiprojectc38onl.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteAllByChannel_Id(Long channelId);
    Collection<Post> findByAccountId(Long accountId);
    Long findAccountIdById(Long postId);
}
