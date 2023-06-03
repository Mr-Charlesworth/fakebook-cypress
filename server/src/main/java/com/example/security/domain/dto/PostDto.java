package com.example.security.domain.dto;

import com.example.security.domain.Post;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class PostDto {

    private Integer id;

    @NotEmpty(message = "Title required")
    private String title;

    @NotEmpty(message = "Content required")
    private String content;

    private Long timeCreated;

    private String username;

    private List<Integer> likingUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getLikingUsers() {
        return likingUsers;
    }

    public void setLikingUsers(List<Integer> likingUsers) {
        this.likingUsers = likingUsers;
    }

    public Post toEntity() {
        Post post = new Post();
        post.setContent(this.content);
        post.setTitle(this.title);
        if (this.id != null) {
            post.setId(this.id);
        }
        return post;
    }
}
