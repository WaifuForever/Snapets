package com.example.snapets.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String id, username, email, imageUrl, nickname, bio, genre;

    public User(){

    }

    public User(String id, String username, String email, String imageUrl, String nickname, String bio, String genre) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.email = email;
        this.bio = bio;
        this.genre = genre;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("login_id", id);
        map.put("username", username);
        map.put("nickname", nickname);
        map.put("genre", genre);
        map.put("email", email);
        map.put("biografy", bio);

        map.put("image", imageUrl);
        return map;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
