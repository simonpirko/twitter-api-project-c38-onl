package by.tms.twitterapiprojectc38onl.dto;

import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

@Data
public class PostUpdateDTO {

    private String title;
    private String description;

    @ElementCollection
    private List<String> imageUrls;
}
