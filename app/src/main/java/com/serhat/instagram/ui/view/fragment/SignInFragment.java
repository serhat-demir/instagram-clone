package com.serhat.instagram.ui.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.serhat.instagram.R;
import com.serhat.instagram.databinding.FragmentSignInBinding;
import com.serhat.instagram.ui.view.activity.MainActivity;
import com.serhat.instagram.ui.viewmodel.SignInViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        binding.setSignInFragment(this);

        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status) {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        viewModel.getLastSessionUser();
        return binding.getRoot();
    }

    public void signIn(String user_name, String user_password) {
        if (user_name.isEmpty() || user_password.trim().isEmpty()) Toast.makeText(requireContext(), getString(R.string.msg_fill_the_blanks), Toast.LENGTH_SHORT).show();
        else viewModel.signIn(user_name, user_password);
    }

    public void navToSignUp() {
        Navigation.findNavController(binding.frgSignInLblNavToSignUp).navigate(R.id.signInToSignUp);
    }
}