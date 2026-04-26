package by.tms.twitterapiprojectc38onl.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChannelDTO {

    @NotBlank(message = "The name of channel cannot be blank!")
    private String channelName;

    @NotBlank(message = "The description of channel cannot be blank!")
    private String channelDescription;

    @NotBlank
    private Long channelOwnerId;

    @NotBlank
    private Long channelModeratorId;
}
