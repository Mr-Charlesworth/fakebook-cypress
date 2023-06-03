package com.example.security.service;

import com.example.security.domain.Post;
import com.example.security.domain.User;
import com.example.security.repository.PostRepository;
import com.example.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Post post, User user) {
       post.setUser(user);
       post.setTimeCreated(Instant.now());
       return postRepository.save(post);
    }

    public void deletePost(Integer id, User deletingUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post does not exist"));
        if (!deletingUser.getRoles().contains(UserService.ADMIN_ROLE) && post.getUser().getId() != deletingUser.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot delete that post");
        }
        postRepository.deleteById(id);
    }

    public void like(Integer postId, User likingUser) {
        Post postToLike = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post does not exist"));
        if (postToLike.getLikingUsers() == null ||
                postToLike.getLikingUsers().stream().noneMatch(user -> user.getId() == likingUser.getId())
        ) {
            postToLike.getLikingUsers().add(likingUser);
//            likingUser.getLikedPosts().add(postToLike);
        } else {
            postToLike.getLikingUsers().removeIf(user -> user.getId() == likingUser.getId());
//            likingUser.getLikedPosts().removeIf(post -> post.getId() == postToLike.getId());
        }
        postRepository.save(postToLike);
//        userRepository.save(likingUser) ;
    }
}
