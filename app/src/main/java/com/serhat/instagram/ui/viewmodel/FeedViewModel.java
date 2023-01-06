package com.serhat.instagram.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.serhat.instagram.data.model.Post;
import com.serhat.instagram.data.repository.PostRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FeedViewModel extends ViewModel {
    private PostRepository pRepo;

    private MutableLiveData<List<Post>> posts, postsSaved;
    private MutableLiveData<String> message;

    @Inject
    public FeedViewModel(PostRepository pRepo) {
        this.pRepo = pRepo;

        posts = pRepo.getPosts();
        postsSaved = pRepo.getPostsSaved();
        message = pRepo.getMessage();
    }

    public MutableLiveData<List<Post>> getPosts() {
        return posts;
    }

    public MutableLiveData<List<Post>> getPostsSaved() {
        return postsSaved;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void getFeed() {
        pRepo.getFeed();
    }

    public void updatePost(Post post) {
        pRepo.updatePost(post);
    }

    public void deletePost(int post_id) {
        pRepo.deletePost(post_id);
    }

    public void like(int post_id) {
        pRepo.like(post_id);
    }

    public void unlike(int post_id) {
        pRepo.unlike(post_id);
    }

    public void getSavedPosts() {
        pRepo.getSavedPosts();
    }

    public void save(int post_id) {
        pRepo.save(post_id);
    }

    public void unsave(int post_id) {
        pRepo.unsave(post_id);
    }
}
