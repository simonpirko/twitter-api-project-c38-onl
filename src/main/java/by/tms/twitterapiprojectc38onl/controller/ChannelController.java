package by.tms.twitterapiprojectc38onl.controller;

import by.tms.twitterapiprojectc38onl.dto.CreateChannelDTO;
import by.tms.twitterapiprojectc38onl.dto.UpdateChannelDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.Channel;
import by.tms.twitterapiprojectc38onl.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Channels")
@RestController
@RequestMapping("/channels")
public class ChannelController {
    @Autowired
    private ChannelService channelService;

    @PostMapping
    @Operation(summary = "To add new channel", description = "This method return created channel")
    ResponseEntity<Channel> createChannel(@Valid @RequestBody CreateChannelDTO channelDTO, @AuthenticationPrincipal Account account) {
        Channel channel = channelService.save(channelDTO, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @PutMapping("/{id}")
    @Operation(summary = "To update channel by id", description = "This method return updated channel")
    ResponseEntity<?> updateChannel(@PathVariable(name = "id") Long id, @Valid @RequestBody UpdateChannelDTO channelDTO) {
        channelService.update(id, channelDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
