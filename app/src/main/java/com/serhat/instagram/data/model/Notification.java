package com.serhat.instagram.data.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("notification_id")
    private Integer notification_id;

    @SerializedName("notification_text")
    private String notification_text;

    @SerializedName("received_at")
    private String received_at;

    @SerializedName("is_seen")
    private Integer is_seen;

    @SerializedName("post")
    private Post post;

    @SerializedName("user")
    private User user;

    public Notification() {
    }

    public Integer getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(Integer notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public String getReceived_at() {
        return received_at;
    }

    public void setReceived_at(String received_at) {
        this.received_at = received_at;
    }

    public Integer getIs_seen() {
        return is_seen;
    }

    public void setIs_seen(Integer is_seen) {
        this.is_seen = is_seen;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
