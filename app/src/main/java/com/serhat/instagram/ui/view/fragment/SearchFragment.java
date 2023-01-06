package com.serhat.instagram.ui.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.serhat.instagram.R;
import com.serhat.instagram.databinding.FragmentSearchBinding;
import com.serhat.instagram.ui.view.adapter.UserAdapterSearch;
import com.serhat.instagram.ui.viewmodel.SearchViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        binding.setSearchFragment(this);

        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            UserAdapterSearch userAdapter = new UserAdapterSearch(requireContext(), users);
            binding.setUserAdapter(userAdapter);
        });

        viewModel.filterUsersByName(binding.frgSearchTxtUserName.getText().toString().trim());

        return binding.getRoot();
    }

    public void onSearchTextChanged(String text) {
        viewModel.filterUsersByName(text);
    }
}