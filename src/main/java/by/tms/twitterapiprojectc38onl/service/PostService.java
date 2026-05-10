package by.tms.twitterapiprojectc38onl.service;

import by.tms.twitterapiprojectc38onl.dto.PostCreateDTO;
import by.tms.twitterapiprojectc38onl.dto.PostResponseDTO;
import by.tms.twitterapiprojectc38onl.dto.PostUpdateDTO;
import by.tms.twitterapiprojectc38onl.entity.*;
import by.tms.twitterapiprojectc38onl.repository.ChannelRepository;
import by.tms.twitterapiprojectc38onl.repository.ReactionRepository;
import by.tms.twitterapiprojectc38onl.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    public PostResponseDTO create(PostCreateDTO dto, Account account) {

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setImageUrls(dto.getImageUrls());
        post.setAccount(account);

        if (dto.getChannelId() != null){
            post.setChannel(channelRepository.getReferenceById(dto.getChannelId()));
        }

        postRepository.save(post);

        return mapToResponse(post);
    }

    public PostResponseDTO updatePostPartial(Long id, PostUpdateDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            post.setDescription(dto.getDescription());
        }

        if (dto.getImageUrls() != null) {
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

    public void delete(Long id){

        if(!postRepository.existsById(id)){
            throw new RuntimeException("Post not found");
        }

        postRepository.deleteById(id);
    }

    public void likePost(Long postId, Account account) {
        setPostReaction(postId, account, ReactionType.LIKE);
    }

    public void dislikePost(Long postId, Account account) {
        setPostReaction(postId, account, ReactionType.DISLIKE);
    }

    private void setPostReaction(Long postId, Account account, ReactionType reactionType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Reaction reaction = reactionRepository.findByAccountIdAndPostId(account.getId(), postId)
                .orElseGet(() -> {
                    Reaction newReaction = new Reaction();
                    newReaction.setAccount(account);
                    newReaction.setPost(post);
                    return newReaction;
                });

        reaction.setType(reactionType);
        reactionRepository.save(reaction);

        mapToResponse(post);
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

        dto.setLikesCount(reactionRepository.countByPostIdAndType(post.getId(), ReactionType.LIKE));
        dto.setDislikesCount(reactionRepository.countByPostIdAndType(post.getId(), ReactionType.DISLIKE));

        if(post.getChannel() != null){
            dto.setChannelId(post.getChannel().getId());
            dto.setChannelName(post.getChannel().getChannelName());
        }

        return dto;
    }


}