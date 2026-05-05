package by.tms.twitterapiprojectc38onl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChannelDTO {
    @NotBlank(message = "The name of channel cannot be blank!")
    @Schema(example = "Channel name 1", description = "Name of channel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String channelName;

    @NotBlank(message = "The description of channel cannot be blank!")
    @Schema(example = "Descriptions of 'Channel name 1'", description = "Description of channel", requiredMode = Schema.RequiredMode.REQUIRED)
    private String channelDescription;
}
