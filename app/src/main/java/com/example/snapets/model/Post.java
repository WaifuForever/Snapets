package com.example.snapets.model;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String uid;
    private String description;
    private String likes;
    private String hashtags;
    private String post_image;
    private String publisher_id;
    private String nComments;
    private String publisher_description;


    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    private String post_id;





    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", uid);
        map.put("description", description);
        map.put("likes", likes);

        map.put("hashtags", hashtags);
        map.put("publisher_id", publisher_id);
        map.put("publisher_description", publisher_description);
        map.put("image", post_image);

        map.put("nComments", nComments);



        return map;
    }

    public Post(){

    }

    public Post (String uid, String description, String likes, String hashtags, String publisher_id, String post_image){
        this.uid = uid;
        this.description = description;
        this.hashtags = hashtags;
        this.likes = likes;
        this.publisher_id = publisher_id;
        this.post_image = post_image;
    }


    public String getPostImage() {
        return post_image;
    }

    public void setPostImage(String post_image) {
        this.post_image = post_image;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPublisher_Id() {
        return this.publisher_id;
    }

    public void setPublisher_id(String publisher_id) {
        this.publisher_id = publisher_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getnComments() {
        return nComments;
    }

    public void setnComments(String nComments) {
        this.nComments = nComments;
    }


    public String getPublisher_description() {
        return publisher_description;
    }

    public void setPublisher_description(String publisher_description) {
        this.publisher_description = publisher_description;
    }
}
