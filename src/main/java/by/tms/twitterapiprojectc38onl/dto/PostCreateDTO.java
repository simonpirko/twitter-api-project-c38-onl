package by.tms.twitterapiprojectc38onl.dto;

import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @ElementCollection
    private List<String> imageUrls;

    private Long channelId;
}
