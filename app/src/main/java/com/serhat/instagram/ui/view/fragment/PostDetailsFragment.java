package com.serhat.instagram.ui.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.serhat.instagram.R;
import com.serhat.instagram.api.ApiUtils;
import com.serhat.instagram.data.model.Post;
import com.serhat.instagram.data.model.User;
import com.serhat.instagram.databinding.FragmentPostDetailsBinding;
import com.serhat.instagram.ui.view.activity.MainActivity;
import com.serhat.instagram.ui.view.adapter.CommentAdapter;
import com.serhat.instagram.ui.viewmodel.PostDetailsViewModel;
import com.serhat.instagram.localdb.Session;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PostDetailsFragment extends Fragment {
    private FragmentPostDetailsBinding binding;
    private PostDetailsViewModel viewModel;

    private List<Post> postsSaved;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PostDetailsViewModel.class);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_details, container, false);
        binding.setPostDetailsFragment(this);

        PostDetailsFragmentArgs bundle = PostDetailsFragmentArgs.fromBundle(getArguments());
        int post_id = bundle.getPostId();

        binding.frgPostDetailsSwipeRefresh.setOnRefreshListener(() -> {
            viewModel.getPostDetailsById(post_id);
            binding.frgPostDetailsSwipeRefresh.setRefreshing(false);
        });

        // post & comment crud messages
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // return to back after post deleted
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status) {
                requireActivity().onBackPressed();
            }
        });

        viewModel.getPostsSaved().observe(getViewLifecycleOwner(), posts -> {
            postsSaved = posts;

            // load post details after saved posts
            viewModel.getPostDetailsById(post_id);
        });

        viewModel.getPost().observe(getViewLifecycleOwner(), post -> {
            if (post.getPost_id() == null) {
                Toast.makeText(requireContext(), getString(R.string.msg_something_went_wrong), Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            } else {
                binding.setPost(post);

                // load photos
                Picasso.get().load(ApiUtils.BASE_URL + getString(R.string.dir_profile_photos) + post.getPost_owner().getUser_photo()).into(binding.frgPostDetailsImgUserPhoto);
                Picasso.get().load(ApiUtils.BASE_URL + getString(R.string.dir_post_photos) + post.getPost_photo()).into(binding.frgPostDetailsImgPostImage);

                // make bold the username in description
                SpannableString ss = new SpannableString(post.getPost_owner().getUser_name() + " " + post.getPost_description());
                ss.setSpan(new StyleSpan(Typeface.BOLD), 0, post.getPost_owner().getUser_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.frgPostDetailsLblPostDescription.setText(ss);

                // set like & save icons
                if (isPostLiked(post)) binding.frgPostDetailsImgLike.setImageDrawable(requireContext().getDrawable(R.drawable.ic_liked));
                else binding.frgPostDetailsImgLike.setImageDrawable(requireContext().getDrawable(R.drawable.ic_like));

                if (isPostSaved(post)) binding.frgPostDetailsImgSave.setImageDrawable(requireContext().getDrawable(R.drawable.ic_saved));
                else binding.frgPostDetailsImgSave.setImageDrawable(requireContext().getDrawable(R.drawable.ic_save));

                // load comments
                CommentAdapter commentAdapter = new CommentAdapter(requireContext(), (MainActivity) requireActivity(), post.getComments(), viewModel);
                binding.setCommentAdapter(commentAdapter);
            }
        });

        viewModel.getSavedPosts();

        return binding.getRoot();
    }

    public void navToPostOwnersProfile(View view, int user_id) {
        PostDetailsFragmentDirections.PostDetailsToProfile postDetailsToProfile = PostDetailsFragmentDirections.postDetailsToProfile();
        postDetailsToProfile.setUserId(user_id);

        Navigation.findNavController(view).navigate(postDetailsToProfile);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void likePost(int post_id) {
        viewModel.like(post_id);

        // update ui
        viewModel.getPostDetailsById(post_id);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void unlikePost(int post_id) {
        viewModel.unlike(post_id);

        // update ui
        viewModel.getPostDetailsById(post_id);
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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void savePost(Post post) {
        viewModel.save(post.getPost_id());

        // update ui
        viewModel.getSavedPosts();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void unsavePost(int post_id) {
        viewModel.unsave(post_id);

        // update ui
        viewModel.getSavedPosts();
    }

    public boolean isPostSaved(Post post) {
        Iterator<Post> iter = postsSaved.iterator();
        while (iter.hasNext()) {
            Post p = iter.next();

            if (p.getPost_id().equals(post.getPost_id())) {
                return true;
            }
        }

        return false;
    }

    @SuppressLint("NonConstantResourceId")
    public void openPopup(View view, Post post) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.app_name));

        if (open_custom_dialog) { // edit
            View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
            EditText txtContent = view.findViewById(R.id.dialogEditTextContent);
            txtContent.setText(post.getPost_description());

            builder.setView(view);
            builder.setNegativeButton(getString(R.string.btn_cancel), null);
            builder.setPositiveButton(getString(R.string.btn_edit), (dialog, which) -> {
                String newText = txtContent.getText().toString().trim();
                if (newText.isEmpty()) Toast.makeText(requireContext(), getString(R.string.dialog_edit_text_msg_text_cannot_be_empty), Toast.LENGTH_SHORT).show();
                else {
                    post.setPost_description(newText);
                    viewModel.updatePost(post);
                    viewModel.getPostDetailsById(post.getPost_id());
                }
            });
        } else { // delete
            builder.setMessage(getString(R.string.card_post_msg_delete));
            builder.setNegativeButton(getString(R.string.btn_no), null);
            builder.setPositiveButton(getString(R.string.btn_yes), (dialog, which) -> viewModel.deletePost(post.getPost_id()));
        }

        builder.create().show();
    }

    public void shareComment(String comment_text, int post_id) {
        if (comment_text.isEmpty()) Toast.makeText(requireContext(), getString(R.string.fragment_post_details_msg_comment_cannot_be_empty), Toast.LENGTH_SHORT).show();
        else {
            viewModel.shareComment(comment_text, post_id);
            binding.frgPostDetailsTxtComment.setText("");
        }
    }
}