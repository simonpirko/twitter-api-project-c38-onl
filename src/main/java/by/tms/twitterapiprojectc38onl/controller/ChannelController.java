package by.tms.twitterapiprojectc38onl.controller;

import by.tms.twitterapiprojectc38onl.dto.CreateChannelDTO;
import by.tms.twitterapiprojectc38onl.dto.UpdateChannelDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping
    ResponseEntity<?> createChannel(@Valid @RequestBody CreateChannelDTO channelDTO) {
        channelService.save(channelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateChannel(@PathVariable(name = "id") Long id, @Valid @RequestBody UpdateChannelDTO channelDTO) {
        channelService.update(id, channelDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{channel_id}")
    ResponseEntity<Void> deleteChannel(@PathVariable(name = "channel_id") Long channelId,
                                       @AuthenticationPrincipal Account account) {

        channelService.delete(channelId, account);
        return ResponseEntity.noContent().build();
    }
}
