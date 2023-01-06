package com.serhat.instagram.ui.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.serhat.instagram.R;
import com.serhat.instagram.api.ApiUtils;
import com.serhat.instagram.data.model.Post;
import com.serhat.instagram.data.model.User;
import com.serhat.instagram.databinding.CardPostBinding;
import com.serhat.instagram.ui.view.activity.MainActivity;
import com.serhat.instagram.ui.view.fragment.SavedPostsFragmentDirections;
import com.serhat.instagram.ui.viewmodel.SavedPostsViewModel;
import com.serhat.instagram.localdb.Session;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

public class PostAdapterSaved extends RecyclerView.Adapter<PostAdapterSaved.PostViewHolder> {
    private Context context;
    private MainActivity activity;
    private List<Post> posts;
    private SavedPostsViewModel viewModel;

    public PostAdapterSaved(Context context, MainActivity activity, List<Post> posts, SavedPostsViewModel viewModel) {
        this.context = context;
        this.activity = activity;
        this.posts = posts;
        this.viewModel = viewModel;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public CardPostBinding binding;

        public PostViewHolder(@NonNull CardPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void navToPostOwnersProfile(View view, int user_id) {
        SavedPostsFragmentDirections.SavedPostsToProfile savedPostsToProfile = SavedPostsFragmentDirections.savedPostsToProfile();
        savedPostsToProfile.setUserId(user_id);

        Navigation.findNavController(view).navigate(savedPostsToProfile);
    }

    public void navToPostDetails(View view, int post_id) {
        SavedPostsFragmentDirections.SavedPostsToPostDetails savedPostsToPostDetails = SavedPostsFragmentDirections.savedPostsToPostDetails();
        savedPostsToPostDetails.setPostId(post_id);

        Navigation.findNavController(view).navigate(savedPostsToPostDetails);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void likePost(int post_id) {
        viewModel.like(post_id);

        // update ui
        viewModel.getSavedPosts();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void unlikePost(int post_id) {
        viewModel.unlike(post_id);

        // update ui
        viewModel.getSavedPosts();
    }

    public boolean isPostLiked(Post post) {
        Iterator<User> iter = post.getLikers().iterator();
        while (iter.hasNext()) {
            User u = iter.next();

            if (u.getUser_id().equals(Session.ACTIVE_USER.getUser_id())) {
                return true;
            }
        }

        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void unsavePost(int post_id) {
        viewModel.unsave(post_id);

        // update ui
        viewModel.getSavedPosts();
    }

    @SuppressLint("NonConstantResourceId")
    public void openPopup(View view, Post post) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_post, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.actionEditPost:
                    showDialog(post, true);
                    break;

                case R.id.actionDeletePost:
                    showDialog(post, false);
                    break;

                default:
                    return false;
            }

            return true;
        });

        popupMenu.show();
    }

    private void showDialog(Post post, boolean open_custom_dialog) { // true = edit, false = delete
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));

        if (open_custom_dialog) { // edit
            View view = activity.getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
            EditText txtContent = view.findViewById(R.id.dialogEditTextContent);
            txtContent.setText(post.getPost_description());

            builder.setView(view);
            builder.setNegativeButton(context.getResources().getString(R.string.btn_cancel), null);
            builder.setPositiveButton(context.getResources().getString(R.string.btn_edit), (dialog, which) -> {
                String newText = txtContent.getText().toString().trim();
                if (newText.isEmpty()) Toast.makeText(context, context.getResources().getString(R.string.dialog_edit_text_msg_text_cannot_be_empty), Toast.LENGTH_SHORT).show();
                else {
                    post.setPost_description(newText);
                    viewModel.updatePost(post);
                    viewModel.getSavedPosts();
                }
            });
        } else { // delete
            builder.setMessage(context.getResources().getString(R.string.card_post_msg_delete));
            builder.setNegativeButton(context.getResources().getString(R.string.btn_no), null);
            builder.setPositiveButton(context.getResources().getString(R.string.btn_yes), (dialog, which) -> {
                viewModel.deletePost(post.getPost_id());
                viewModel.getSavedPosts();
            });
        }

        builder.create().show();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_post, parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.binding.setPostAdapterSaved(this);
        holder.binding.setPost(post);

        // load photo
        Picasso.get().load(ApiUtils.BASE_URL + context.getResources().getString(R.string.dir_profile_photos) + post.getPost_owner().getUser_photo()).into(holder.binding.cardPostImgUserPhoto);
        Picasso.get().load(ApiUtils.BASE_URL + context.getResources().getString(R.string.dir_post_photos) + post.getPost_photo()).into(holder.binding.cardPostImgPostImage);

        // make bold the username in description
        SpannableString ss = new SpannableString(post.getPost_owner().getUser_name() + " " + post.getPost_description());
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, post.getPost_owner().getUser_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.binding.cardPostLblPostDescription.setText(ss);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
