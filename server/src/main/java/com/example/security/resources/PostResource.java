package com.example.security.resources;

import com.example.security.domain.dto.PostDto;
import com.example.security.domain.dto.ResultResponseDto;
import com.example.security.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private final PostService postService;

    public PostResource(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> create(JwtAuthenticationToken principal, @Valid @RequestBody PostDto postDto) {
        PostDto savedPost = postService.createPost(postDto.toEntity(), principal.getName());
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponseDto> delete(JwtAuthenticationToken principal, @PathVariable("id") Integer id) {
        postService.deletePost(id, principal.getName());
        return new ResponseEntity<>(new ResultResponseDto("success"), HttpStatus.OK);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ResultResponseDto> like(JwtAuthenticationToken principal, @PathVariable("id") Integer id) {
        postService.like(id, principal.getName());
        return new ResponseEntity<>(new ResultResponseDto("success"), HttpStatus.OK);
    }
}
