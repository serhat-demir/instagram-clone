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
import com.serhat.instagram.data.model.Post;
import com.serhat.instagram.databinding.CardPostThumbnailBinding;
import com.serhat.instagram.ui.view.fragment.ProfileFragmentDirections;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapterProfile extends RecyclerView.Adapter<PostAdapterProfile.PostThumbnailViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostAdapterProfile(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public class PostThumbnailViewHolder extends RecyclerView.ViewHolder {
        public CardPostThumbnailBinding binding;

        public PostThumbnailViewHolder(@NonNull CardPostThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void navToPostDetails(View view, int post_id) {
        ProfileFragmentDirections.ProfileToPostDetails profileToPostDetails = ProfileFragmentDirections.profileToPostDetails();
        profileToPostDetails.setPostId(post_id);

        Navigation.findNavController(view).navigate(profileToPostDetails);
    }

    @NonNull
    @Override
    public PostThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardPostThumbnailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_post_thumbnail, parent, false);
        return new PostThumbnailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostThumbnailViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.binding.setPostAdapterProfile(this);
        holder.binding.setPost(post);

        // load photo
        Picasso.get().load(ApiUtils.BASE_URL + context.getResources().getString(R.string.dir_post_photos) + post.getPost_photo()).into(holder.binding.cardPostThumbnailImgPostPhoto);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
