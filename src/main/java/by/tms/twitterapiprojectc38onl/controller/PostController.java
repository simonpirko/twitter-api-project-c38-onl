package by.tms.twitterapiprojectc38onl.controller;


import by.tms.twitterapiprojectc38onl.dto.PostCreateDTO;
import by.tms.twitterapiprojectc38onl.dto.PostResponseDTO;
import by.tms.twitterapiprojectc38onl.dto.PostUpdateDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.Post;
import by.tms.twitterapiprojectc38onl.repository.PostRepository;
import by.tms.twitterapiprojectc38onl.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Posts")
@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @GetMapping("/{post_id}")
    public PostResponseDTO getPostById(@PathVariable Long post_id) {

        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("Post not find"));

        return postService.mapToResponse(post);
    }

    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostResponseDTO> getAllPosts(){
        return postService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    @Operation(summary = "To add new post", description = "This method return created post")
    public ResponseEntity<Post> createPost(@RequestBody PostCreateDTO postCreateDTO,
                                           @AuthenticationPrincipal Account account){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(postCreateDTO, account));
    }

    @PatchMapping("/{post_id}")
    @PreAuthorize("hasRole('ROLE_AUTHOR')")
    public ResponseEntity<PostResponseDTO> updatePostById(@PathVariable Long post_id,
                                               @RequestBody PostUpdateDTO postUpdateDTO, @AuthenticationPrincipal Account account) {

        return ResponseEntity.ok(postService.updatePostPartial(post_id, postUpdateDTO, account));
    }

    @DeleteMapping("/{post_id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN', 'MODERATOR')")
    @Operation(summary = "Delete post by specified id", description = "this method return nothing")
    public ResponseEntity<Void> deletePostById(@PathVariable Long post_id, @AuthenticationPrincipal Account account) {
        System.out.println("test");
        System.out.println(account);
        postService.delete(post_id, account);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal Account account
    ) {
        postService.likePost(postId, account);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<?> dislikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal Account account
    ) {
        postService.dislikePost(postId, account);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
