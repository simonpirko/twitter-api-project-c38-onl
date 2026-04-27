package by.tms.twitterapiprojectc38onl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String channelName;

    @NotBlank
    private String channelDescription;

    @NotBlank
    private Long channelOwnerId;

    @NotBlank
    private Long channelModeratorId;

    @OneToMany
    private List<Post> channelPosts;

    @OneToMany
    private List<Account> subscribers;
}
