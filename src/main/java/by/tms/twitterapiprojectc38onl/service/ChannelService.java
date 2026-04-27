package by.tms.twitterapiprojectc38onl.service;

import by.tms.twitterapiprojectc38onl.dto.CreateChannelDTO;
import by.tms.twitterapiprojectc38onl.dto.UpdateChannelDTO;
import by.tms.twitterapiprojectc38onl.entity.Channel;
import by.tms.twitterapiprojectc38onl.repository.ChannelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

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
}
