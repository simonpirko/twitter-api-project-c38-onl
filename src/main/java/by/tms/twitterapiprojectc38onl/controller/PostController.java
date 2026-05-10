package by.tms.twitterapiprojectc38onl.controller;


import by.tms.twitterapiprojectc38onl.entity.Account;
import by.tms.twitterapiprojectc38onl.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    String getPosts() {
        return "Posts";
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
