package com.serhat.instagram.ui.view.adapter;

import android.annotation.SuppressLint;
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
import com.serhat.instagram.data.model.User;
import com.serhat.instagram.databinding.CardUserBinding;
import com.serhat.instagram.ui.view.fragment.FollowFragmentDirections;
import com.serhat.instagram.ui.viewmodel.FollowViewModel;
import com.serhat.instagram.localdb.Session;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

public class UserAdapterFollow extends RecyclerView.Adapter<UserAdapterFollow.UserViewHolder> {
    private Context context;
    private List<User> users;
    private FollowViewModel viewModel;

    public UserAdapterFollow(Context context, List<User> users, FollowViewModel viewModel) {
        this.context = context;
        this.users = users;
        this.viewModel = viewModel;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public CardUserBinding binding;

        public UserViewHolder(@NonNull CardUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void navToUserProfile(View view, int user_id) {
        FollowFragmentDirections.FollowToProfile followToProfile = FollowFragmentDirections.followToProfile();
        followToProfile.setUserId(user_id);

        Navigation.findNavController(view).navigate(followToProfile);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void follow(User user) {
        viewModel.follow(user.getUser_id());

        // update ui
        Session.ACTIVE_USER.getFollowing().add(user);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void unfollow(int user_id) {
        viewModel.unfollow(user_id);

        // update ui
        Iterator<User> iter = Session.ACTIVE_USER.getFollowing().iterator();
        while (iter.hasNext()) {
            User u = iter.next();

            if (u.getUser_id() == user_id) {
                iter.remove();
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_user, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.setUserAdapterFollow(this);
        holder.binding.setUser(user);

        // load photo
        Picasso.get().load(ApiUtils.BASE_URL + context.getResources().getString(R.string.dir_profile_photos) + user.getUser_photo()).into(holder.binding.cardUserImgUserPhoto);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
