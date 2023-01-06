package com.serhat.instagram.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.serhat.instagram.api.ApiInterface;
import com.serhat.instagram.data.model.Notification;
import com.serhat.instagram.data.response.ApiResponse;
import com.serhat.instagram.data.response.NotificationListResponse;
import com.serhat.instagram.localdb.Session;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {
    private final ApiInterface apiService;
    private MutableLiveData<List<Notification>> notifications;
    private MutableLiveData<Integer> unseenNotificationCount;

    public NotificationRepository(ApiInterface apiService) {
        this.apiService = apiService;

        notifications = new MutableLiveData<>();
        unseenNotificationCount = new MutableLiveData<>();
    }

    public MutableLiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public MutableLiveData<Integer> getUnseenNotificationCount() {
        return unseenNotificationCount;
    }

    public void getAllNotifications() {
        apiService.getAllNotifications(Session.ACTIVE_USER.getUser_id()).enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getNotifications() != null) {
                    unseenNotificationCount.setValue(response.body().getUnseen_notification_count());
                    notifications.setValue(response.body().getNotifications());
                }
                else {
                    unseenNotificationCount.setValue(0);
                    notifications.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void markAllNotificationsAsSeen() {
        apiService.markAllNotificationsAsSeen(Session.ACTIVE_USER).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // getAllNotifications();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void deleteAllNotifications() {
        apiService.deleteAllNotifications(Session.ACTIVE_USER.getUser_id()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getAllNotifications();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }
}
