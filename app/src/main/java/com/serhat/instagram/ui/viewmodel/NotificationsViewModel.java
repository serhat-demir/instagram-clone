package com.serhat.instagram.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.serhat.instagram.data.model.Notification;
import com.serhat.instagram.data.repository.NotificationRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NotificationsViewModel extends ViewModel {
    private NotificationRepository nRepo;
    private MutableLiveData<List<Notification>> notifications;
    private MutableLiveData<Integer> unseenNotificationCount;

    @Inject
    public NotificationsViewModel(NotificationRepository nRepo) {
        this.nRepo = nRepo;

        notifications = nRepo.getNotifications();
        unseenNotificationCount = nRepo.getUnseenNotificationCount();
    }

    public MutableLiveData<List<Notification>> getNotifications() {
        return notifications;
    }

    public void getAllNotifications() {
        nRepo.getAllNotifications();
    }

    public MutableLiveData<Integer> getUnseenNotificationCount() {
        return unseenNotificationCount;
    }

    public void markAllNotificationsAsSeen() {
        nRepo.markAllNotificationsAsSeen();
    }

    public void deleteAllNotifications() {
        nRepo.deleteAllNotifications();
    }
}
