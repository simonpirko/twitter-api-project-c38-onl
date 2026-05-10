package by.tms.twitterapiprojectc38onl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDTO {

    private Long id;
    private String title;
    private String description;
    private List<String> imageUrls;

    private Long accountId;
    private String username;

    private Long channelId;
    private String channelName;

    private long likesCount;
    private long dislikesCount;
}
