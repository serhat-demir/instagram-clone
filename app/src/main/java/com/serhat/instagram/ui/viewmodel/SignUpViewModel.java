package com.serhat.instagram.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.serhat.instagram.data.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    private UserRepository uRepo;
    private MutableLiveData<String> message;
    private MutableLiveData<Boolean> status;

    @Inject
    public SignUpViewModel(UserRepository uRepo) {
        this.uRepo = uRepo;

        message = uRepo.getMessage();
        status = uRepo.getStatus();
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Boolean> getStatus() {
        return status;
    }

    public void signUp(String user_email, String user_name, String user_password) {
        uRepo.signUp(user_email, user_name, user_password);
    }
}
