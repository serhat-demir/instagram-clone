package com.serhat.instagram.ui.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.serhat.instagram.R;
import com.serhat.instagram.databinding.FragmentSavedPostsBinding;
import com.serhat.instagram.ui.view.activity.MainActivity;
import com.serhat.instagram.ui.view.adapter.PostAdapterSaved;
import com.serhat.instagram.ui.viewmodel.SavedPostsViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedPostsFragment extends Fragment {
    private FragmentSavedPostsBinding binding;
    private SavedPostsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SavedPostsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_posts, container, false);

        binding.frgSavedPostsRecyclerViewSwipeRefresh.setOnRefreshListener(() -> {
            viewModel.getSavedPosts();
            binding.frgSavedPostsRecyclerViewSwipeRefresh.setRefreshing(false);
        });

        // post crud messages
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getPostsSaved().observe(getViewLifecycleOwner(), savedPosts -> {
            PostAdapterSaved postAdapter = new PostAdapterSaved(requireContext(), (MainActivity) requireActivity(), savedPosts, viewModel);
            binding.setPostAdapter(postAdapter);

            if (savedPosts.size() == 0) binding.frgSavedPostsLblMsgNoPost.setVisibility(View.VISIBLE);
            else binding.frgSavedPostsLblMsgNoPost.setVisibility(View.GONE);
        });

        viewModel.getSavedPosts();

        return binding.getRoot();
    }
}