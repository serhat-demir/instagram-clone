package com.serhat.instagram.ui.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.serhat.instagram.R;
import com.serhat.instagram.databinding.FragmentNewPostBinding;
import com.serhat.instagram.ui.viewmodel.NewPostViewModel;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewPostFragment extends Fragment {
    private FragmentNewPostBinding binding;
    private NewPostViewModel viewModel;

    public File selectedFile;
    private ActivityResultLauncher<Intent> selectImageResultLauncher;
    private ActivityResultLauncher<String[]> permissionResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        // select image
        selectImageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    binding.frgNewPostImgSelectPhoto.setImageURI(imageUri);

                    String path = imageUri.getPath().substring(5);
                    selectedFile = new File(path);
                }
            }
        });

        // permission
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)) && Boolean.TRUE.equals(result.get(Manifest.permission.READ_EXTERNAL_STORAGE))) {
                Toast.makeText(requireContext(), getString(R.string.msg_permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), getString(R.string.msg_permission_denied), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false);
        binding.setNewPostFragment(this);

        // error messages
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // nav to feed after post shared
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status) {
                Navigation.findNavController(binding.frgNewPostBtnShare).navigate(R.id.newPostToFeed);
            }
        });

        return binding.getRoot();
    }

    public void sharePost(String postDescription) {
        if (!postDescription.isEmpty()) {
            if (selectedFile != null) viewModel.sharePost(selectedFile, postDescription);
            else Toast.makeText(requireContext(), getString(R.string.fragment_new_post_msg_select_image), Toast.LENGTH_SHORT).show();
        } else Toast.makeText(requireContext(), getString(R.string.fragment_new_post_msg_description_can_not_be_empty), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("IntentReset")
    public void selectImage() {
        if (checkPerm()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");

            selectImageResultLauncher.launch(intent);
        } else {
            permissionResultLauncher.launch(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private boolean checkPerm() {
        int permWrite = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permRead = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permWrite == PackageManager.PERMISSION_GRANTED && permRead == PackageManager.PERMISSION_GRANTED;
    }
}