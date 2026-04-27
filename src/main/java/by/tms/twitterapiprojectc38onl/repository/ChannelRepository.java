package by.tms.twitterapiprojectc38onl.repository;

import by.tms.twitterapiprojectc38onl.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findByChannelName(String channelName);
}
