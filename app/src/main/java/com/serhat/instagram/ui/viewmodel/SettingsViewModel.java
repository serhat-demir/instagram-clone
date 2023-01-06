package com.serhat.instagram.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.serhat.instagram.data.model.User;
import com.serhat.instagram.data.repository.UserRepository;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private UserRepository uRepo;
    private MutableLiveData<String> message;
    private MutableLiveData<Boolean> status;

    @Inject
    public SettingsViewModel(UserRepository uRepo) {
        this.uRepo = uRepo;

        this.message = uRepo.getMessage();
        this.status = uRepo.getStatus();
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Boolean> getStatus() {
        return status;
    }

    public void changeProfilePhoto(File profilePhoto) {
        uRepo.changeProfilePhoto(profilePhoto);
    }

    public void removeProfilePhoto() {
        uRepo.removeProfilePhoto();
    }

    public void updateUser(User newUser) {
        uRepo.updateUser(newUser);
    }
}
