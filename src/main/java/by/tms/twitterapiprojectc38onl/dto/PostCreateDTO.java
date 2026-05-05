package by.tms.twitterapiprojectc38onl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateDTO {

    @NotBlank
    @Schema(example = "My first post", description = "Title of post", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank
    @Schema(example = "Description of my first post", description = "Description of post", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @ElementCollection
    @Schema(example = "[\"url1\", \"url2\"]", description = "The array of links to imgs", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> imageUrls;

    @Schema(example = "1", description = "The id of channel, where post will be created", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long channelId;
}
