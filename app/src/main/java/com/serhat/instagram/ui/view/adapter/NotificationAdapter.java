package com.serhat.instagram.ui.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.serhat.instagram.R;
import com.serhat.instagram.api.ApiUtils;
import com.serhat.instagram.data.model.Notification;
import com.serhat.instagram.databinding.CardNotificationBinding;
import com.serhat.instagram.ui.view.fragment.NotificationsFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        CardNotificationBinding binding;

        public NotificationViewHolder(@NonNull CardNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void navToResource(View view, Notification notification) {
        if (notification.getUser() != null) { // nav to user profile
            NotificationsFragmentDirections.NotificationsToProfile notificationsToProfile = NotificationsFragmentDirections.notificationsToProfile();
            notificationsToProfile.setUserId(notification.getUser().getUser_id());

            Navigation.findNavController(view).navigate(notificationsToProfile);
        } else { // nav to post details
            NotificationsFragmentDirections.NotificationsToPostDetails notificationsToPostDetails = NotificationsFragmentDirections.notificationsToPostDetails();
            notificationsToPostDetails.setPostId(notification.getPost().getPost_id());

            Navigation.findNavController(view).navigate(notificationsToPostDetails);
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_notification, parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.binding.setNotificationAdapter(this);
        holder.binding.setNotification(notification);

        // load photo
        if (notification.getUser() != null) { // profile photo
            Picasso.get().load(ApiUtils.BASE_URL + context.getResources().getString(R.string.dir_profile_photos) + notification.getUser().getUser_photo()).into(holder.binding.cardNotificationImgNotificationPhoto);
        } else { // post photo
            Picasso.get().load(ApiUtils.BASE_URL + context.getResources().getString(R.string.dir_post_photos) + notification.getPost().getPost_photo()).into(holder.binding.cardNotificationImgNotificationPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}
