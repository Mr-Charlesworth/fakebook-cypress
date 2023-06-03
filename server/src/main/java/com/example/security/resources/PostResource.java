package com.example.security.resources;

import com.example.security.domain.Post;
import com.example.security.domain.User;
import com.example.security.domain.dto.PostDto;
import com.example.security.service.PostService;
import com.example.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private final PostService postService;
    private final UserService userService;

    public PostResource(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts() {
        return new ResponseEntity<>(
                postService.getAllPosts()
                        .stream()
                        .map(Post::toDto)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> create(JwtAuthenticationToken principal, @RequestBody PostDto postDto) {
        User postingUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        Post savedPost = postService.createPost(postDto.toEntity(), postingUser);
        return new ResponseEntity<>(savedPost.toDto(), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResultResponse> delete(JwtAuthenticationToken principal, @PathVariable("id") Integer id) {
        User deletingUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        postService.deletePost(id, deletingUser);
        return new ResponseEntity<>(new ResultResponse("success"), HttpStatus.OK);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<ResultResponse> like(JwtAuthenticationToken principal, @PathVariable("id") Integer id) {
        User likingUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        postService.like(id, likingUser);
        return new ResponseEntity<>(new ResultResponse("success"), HttpStatus.OK);
    }
}
