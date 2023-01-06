package com.serhat.instagram.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.serhat.instagram.data.repository.PostRepository;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewPostViewModel extends ViewModel {
    private PostRepository pRepo;

    private MutableLiveData<String> message;
    private MutableLiveData<Boolean> status;

    @Inject
    public NewPostViewModel(PostRepository pRepo) {
        this.pRepo = pRepo;

        message = pRepo.getMessage();
        status = pRepo.getStatus();
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Boolean> getStatus() {
        return status;
    }

    public void sharePost(File postPhoto, String postDescription) {
        pRepo.sharePost(postPhoto, postDescription);
    }
}
