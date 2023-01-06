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

import com.google.android.material.snackbar.Snackbar;
import com.serhat.instagram.R;
import com.serhat.instagram.api.ApiUtils;
import com.serhat.instagram.data.model.User;
import com.serhat.instagram.databinding.FragmentSettingsBinding;
import com.serhat.instagram.localdb.Session;
import com.serhat.instagram.ui.viewmodel.SettingsViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;

    public File selectedFile;
    private ActivityResultLauncher<Intent> selectImageResultLauncher;
    private ActivityResultLauncher<String[]> permissionResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // select image
        selectImageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    binding.frgSettingsImgProfilePhoto.setImageURI(imageUri);

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.setSettingsFragment(this);

        // crud messages
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            if (!message.isEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // change imageview after remove profile photo
        viewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                if (status) {
                    Picasso.get().load(ApiUtils.BASE_URL + getString(R.string.dir_profile_photos) + getString(R.string.default_photo)).into(binding.frgSettingsImgProfilePhoto);
                    binding.frgSettingsLblRemovePhoto.setVisibility(View.GONE);
                } else binding.frgSettingsLblRemovePhoto.setVisibility(View.VISIBLE);
            }
        });

        // load profile photo
        Picasso.get().load(ApiUtils.BASE_URL + getString(R.string.dir_profile_photos) + Session.ACTIVE_USER.getUser_photo()).into(binding.frgSettingsImgProfilePhoto);

        // hide "remove photo" label if user has not got a profile photo
        if (Objects.equals(Session.ACTIVE_USER.getUser_photo(), getString(R.string.default_photo))) binding.frgSettingsLblRemovePhoto.setVisibility(View.GONE);

        return binding.getRoot();
    }

    public void changeProfilePhoto() {
        if (selectedFile != null) viewModel.changeProfilePhoto(selectedFile);
        else Toast.makeText(requireContext(), getString(R.string.fragment_new_post_msg_select_image), Toast.LENGTH_SHORT).show();
    }

    public void removeProfilePhoto(View view) {
        Snackbar.make(view, getString(R.string.fragment_settings_msg_remove_photo), Snackbar.LENGTH_SHORT).setAction(getString(R.string.btn_yes), v -> viewModel.removeProfilePhoto()).show();
    }

    public void updateUser(String user_email, String user_password, String user_fullname, String user_bio, int profile_private) {
        if (user_email.isEmpty()) Toast.makeText(requireContext(), getString(R.string.fragment_settings_msg_email_can_not_be_empty), Toast.LENGTH_SHORT).show();

        User user = Session.ACTIVE_USER;
        user.setUser_email(user_email);
        user.setUser_password(user_password);
        user.setUser_fullname(user_fullname);
        user.setUser_bio(user_bio);
        user.setUser_profile_private(profile_private);

        viewModel.updateUser(user);
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