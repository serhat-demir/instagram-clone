package com.serhat.instagram.data.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("comment_id")
    private Integer comment_id;

    @SerializedName("comment_text")
    private String comment_text;

    @SerializedName("comment_post")
    private Integer comment_post;

    @SerializedName("comment_owner")
    private User comment_owner;

    @SerializedName("created_at")
    private String created_at;

    public Comment() {
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public Integer getComment_post() {
        return comment_post;
    }

    public void setComment_post(Integer comment_post) {
        this.comment_post = comment_post;
    }

    public User getComment_owner() {
        return comment_owner;
    }

    public void setComment_owner(User comment_owner) {
        this.comment_owner = comment_owner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
