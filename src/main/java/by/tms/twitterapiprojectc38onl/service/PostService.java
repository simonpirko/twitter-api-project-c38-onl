package by.tms.twitterapiprojectc38onl.service;

import by.tms.twitterapiprojectc38onl.dto.PostCreateDTO;
import by.tms.twitterapiprojectc38onl.dto.PostResponseDTO;
import by.tms.twitterapiprojectc38onl.dto.PostUpdateDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.Channel;
import by.tms.twitterapiprojectc38onl.entity.Post;
import by.tms.twitterapiprojectc38onl.entity.Role;
import by.tms.twitterapiprojectc38onl.exception.AccessDeniedException;
import by.tms.twitterapiprojectc38onl.repository.ChannelRepository;
import by.tms.twitterapiprojectc38onl.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChannelRepository channelRepository;

    public Post create(PostCreateDTO dto, Account account) {

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setImageUrls(dto.getImageUrls());
        post.setAccount(account);
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));
        post.setChannel(channel);

        return postRepository.save(post);
    }

    public PostResponseDTO updatePostPartial(Long id, PostUpdateDTO dto, Account account) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!Objects.equals(post.getAccount().getId(), account.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        if (Objects.nonNull(dto.getTitle())) {
            post.setTitle(dto.getTitle());
        }

        if (Objects.nonNull(dto.getDescription())) {
            post.setDescription(dto.getDescription());
        }

        if (Objects.nonNull(dto.getImageUrls())) {
            post.setImageUrls(dto.getImageUrls());
        }

        postRepository.save(post);

        return mapToResponse(post);
    }

    public List<PostResponseDTO> getAll() {

        return postRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void delete(Long id, Account account) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));


        Set<Role> roles = account.getRoles();

        boolean isAdminOrModerator = roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_MODERATOR);
        boolean isOwner = Objects.equals(post.getAccount().getId(), account.getId());

        if (!isAdminOrModerator && !isOwner) {
            throw new AccessDeniedException("Access denied");
        }

        postRepository.deleteById(id);
    }

    public PostResponseDTO mapToResponse(Post post){

        PostResponseDTO dto = new PostResponseDTO();

        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setImageUrls(post.getImageUrls());

        dto.setAccountId(post.getAccount().getId());
        dto.setUsername(post.getAccount().getUsername());

        if(Objects.nonNull(post.getChannel())){
            dto.setChannelId(post.getChannel().getId());
            dto.setChannelName(post.getChannel().getChannelName());
        }

        return dto;
    }

    public Collection<Post> getPostsByAccountId(Long accountId) {
        return postRepository.findByAccountId(accountId);
    }
}
