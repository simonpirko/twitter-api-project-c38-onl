package by.tms.twitterapiprojectc38onl.controller;


import by.tms.twitterapiprojectc38onl.dto.PostCreateDTO;
import by.tms.twitterapiprojectc38onl.dto.PostResponseDTO;
import by.tms.twitterapiprojectc38onl.dto.PostUpdateDTO;
import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.entity.Post;
import by.tms.twitterapiprojectc38onl.repository.PostRepository;
import by.tms.twitterapiprojectc38onl.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public List<PostResponseDTO> getAllPosts(){

        return postService.getAll();
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateDTO postCreateDTO,
                                           @AuthenticationPrincipal Account account){

        return ResponseEntity.ok(postService.create(postCreateDTO, account));
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<PostResponseDTO> updatePostById(@PathVariable Long post_id,
                                               @RequestBody PostUpdateDTO postUpdateDTO) {

        return ResponseEntity.ok(postService.updatePostPartial(post_id, postUpdateDTO));
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long post_id) {
        postService.delete(post_id);
        return ResponseEntity.noContent().build();
    }
}
