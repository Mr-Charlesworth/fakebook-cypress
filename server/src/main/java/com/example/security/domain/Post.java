package com.example.security.domain;

import com.example.security.domain.dto.PostDto;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String content;

    private Instant timeCreated;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> likingUsers;

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

    public Instant getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getLikingUsers() {
        return likingUsers;
    }

    public void setLikingUsers(List<User> likingUsers) {
        this.likingUsers = likingUsers;
    }

    public PostDto dto() {
        PostDto postDto = new PostDto();
        postDto.setId(this.id);
        postDto.setContent(this.content);
        postDto.setTitle(this.title);
        postDto.setTimeCreated(this.timeCreated.toEpochMilli());
        postDto.setUsername(this.user.getUsername());
        if (this.likingUsers == null) {
            postDto.setLikingUsers(Collections.emptyList());
        } else {
            postDto.setLikingUsers(this.likingUsers.stream().map(User::getId).collect(Collectors.toList()));
        }
        return postDto;
    }
}
