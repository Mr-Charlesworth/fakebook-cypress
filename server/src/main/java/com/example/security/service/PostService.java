package com.example.security.service;

import com.example.security.domain.Post;
import com.example.security.domain.User;
import com.example.security.domain.dto.PostDto;
import com.example.security.repository.PostRepository;
import com.example.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(Post::dto)
                .collect(Collectors.toList());
    }

    public PostDto createPost(Post post, String username) {
        User postingUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        post.setUser(postingUser);
        post.setTimeCreated(Instant.now());
        return postRepository.save(post).dto();
    }

    public void deletePost(Integer id, String username) {
        User deletingUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post does not exist"));
        if (!deletingUser.getRoles().contains(UserService.ADMIN_ROLE) && post.getUser().getId() != deletingUser.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot delete that post");
        }
        postRepository.deleteById(id);
    }

    public void like(Integer postId, String username) {
        User likingUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        Post postToLike = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post does not exist"));
        if (postToLike.getLikingUsers() == null ||
                postToLike.getLikingUsers().stream().noneMatch(user -> user.getId() == likingUser.getId())
        ) {
            postToLike.getLikingUsers().add(likingUser);
        } else {
            postToLike.getLikingUsers().removeIf(user -> user.getId() == likingUser.getId());
        }
        postRepository.save(postToLike);
    }
}
