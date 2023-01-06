package com.serhat.instagram.data.response;

import com.google.gson.annotations.SerializedName;
import com.serhat.instagram.data.model.Notification;

import java.util.List;

public class NotificationListResponse {
    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    @SerializedName("unseen_notification_count")
    private Integer unseen_notification_count;

    @SerializedName("notifications")
    private List<Notification> notifications;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUnseen_notification_count() {
        return unseen_notification_count;
    }

    public void setUnseen_notification_count(Integer unseen_notification_count) {
        this.unseen_notification_count = unseen_notification_count;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
