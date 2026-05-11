package by.tms.twitterapiprojectc38onl.service;

import by.tms.twitterapiprojectc38onl.dto.CreateChannelDTO;
import by.tms.twitterapiprojectc38onl.dto.UpdateChannelDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.Channel;
import by.tms.twitterapiprojectc38onl.entity.Role;
import by.tms.twitterapiprojectc38onl.repository.ChannelRepository;
import by.tms.twitterapiprojectc38onl.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private PostRepository postRepository;

    public Channel save(CreateChannelDTO channelDTO) {
        Channel channel = new Channel();

        channel.setChannelName(channelDTO.getChannelName());
        channel.setChannelDescription(channelDTO.getChannelDescription());
        channel.setChannelOwnerId(channelDTO.getChannelOwnerId());
        channel.setChannelModeratorId(channelDTO.getChannelModeratorId());

        return channelRepository.save(channel);
    }

    public Channel update(Long id, UpdateChannelDTO channelDTO) throws EntityNotFoundException{
        Optional<Channel> byId = channelRepository.findById(id);

        if (byId.isPresent()) {
            Channel channel = byId.get();
            channel.setChannelName(channelDTO.getChannelName());
            channel.setChannelDescription(channelDTO.getChannelDescription());
            channel.setChannelModeratorId(channelDTO.getChannelModeratorId());

            return channelRepository.save(channel);
        }

        throw new EntityNotFoundException("Channel not found with id: " + id);
    }

    @Transactional
    public void delete(Long channelId, Account requester) throws EntityNotFoundException {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new EntityNotFoundException("Channel not found with id: " + channelId));

        boolean isOwner = requester != null
                && requester.getId() != null
                && channel.getChannelOwnerId() != null
                && channel.getChannelOwnerId().equals(requester.getId());

        boolean isAdminOrModerator = requester != null
                && requester.getRoles() != null
                && (requester.getRoles().contains(Role.ROLE_ADMIN) || requester.getRoles().contains(Role.ROLE_MODERATOR));

        if (!isOwner && !isAdminOrModerator) {
            throw new AccessDeniedException("You are not allowed to delete this channel");
        }

        postRepository.deleteAllByChannel_Id(channelId);
        channelRepository.delete(channel);
    }
}
