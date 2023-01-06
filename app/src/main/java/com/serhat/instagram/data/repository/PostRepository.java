package com.serhat.instagram.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.serhat.instagram.api.ApiInterface;
import com.serhat.instagram.data.model.Comment;
import com.serhat.instagram.data.model.Post;
import com.serhat.instagram.data.response.ApiResponse;
import com.serhat.instagram.data.response.PostListResponse;
import com.serhat.instagram.data.response.PostResponse;
import com.serhat.instagram.localdb.Session;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private final ApiInterface apiService;
    private MutableLiveData<List<Post>> posts, postsSaved; // feed & profile fragment, saved posts fragment
    private MutableLiveData<Post> post; // post details fragment
    private MutableLiveData<String> message; // post & comment crud messages -> post details & share post fragment
    private MutableLiveData<Boolean> status; // return to back after post deleted, nav to feed after post shared -> post details & share post fragment

    public PostRepository(ApiInterface apiService) {
        this.apiService = apiService;

        posts = new MutableLiveData<>();
        postsSaved = new MutableLiveData<>();
        post = new MutableLiveData<>();
        message = new MutableLiveData<>();
        status = new MutableLiveData<>();
    }

    public MutableLiveData<List<Post>> getPosts() {
        posts = new MutableLiveData<>();
        return posts;
    }

    public MutableLiveData<List<Post>> getPostsSaved() {
        return postsSaved;
    }

    public MutableLiveData<Post> getPost() {
        post = new MutableLiveData<>();
        return post;
    }

    public MutableLiveData<String> getMessage() {
        message = new MutableLiveData<>();
        return message;
    }

    public MutableLiveData<Boolean> getStatus() {
        status = new MutableLiveData<>();
        return status;
    }

    public void getFeed() {
        apiService.getFeed(Session.ACTIVE_USER.getUser_id(), 1).enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getPosts() != null) posts.setValue(response.body().getPosts());
                else posts.setValue(new ArrayList<>());
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void getPostsByUserId(int user_id) {
        apiService.getPostsByUserId(user_id, 0).enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getPosts() != null) posts.setValue(response.body().getPosts());
                else posts.setValue(new ArrayList<>());
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void getPostDetailsById(int post_id) {
        apiService.getPostDetailsById(post_id).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getPost() != null) post.setValue(response.body().getPost());
                else post.setValue(new Post());
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void sharePost(File postPhoto, String postDescription) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), postPhoto);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", postPhoto.getName(), requestFile);

        RequestBody requestDescription = RequestBody.create(MediaType.parse("text/plain"), postDescription);

        apiService.sharePost(requestDescription, Session.ACTIVE_USER.getUser_id(), body).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    status.setValue(true);
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }
                }

                message.setValue("");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void updatePost(Post post) {
        apiService.updatePost(post).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }
                }

                message.setValue("");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void deletePost(int post_id) {
        apiService.deletePost(post_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    status.setValue(true); // return to back if this response is successful
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }
                }

                message.setValue("");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void like( int post_id) {
        apiService.like(Session.ACTIVE_USER.getUser_id(), post_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // liked
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void unlike(int post_id) {
        apiService.unlike(Session.ACTIVE_USER.getUser_id(), post_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // unliked
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void getSavedPosts() {
        apiService.getSavedPosts(Session.ACTIVE_USER.getUser_id()).enqueue(new Callback<PostListResponse>() {
            @Override
            public void onResponse(Call<PostListResponse> call, Response<PostListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getPosts() != null) postsSaved.setValue(response.body().getPosts());
                else postsSaved.setValue(new ArrayList<>());
            }

            @Override
            public void onFailure(Call<PostListResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void save(int post_id) {
        apiService.save(Session.ACTIVE_USER.getUser_id(), post_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // saved
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void unsave(int post_id) {
        apiService.unsave(Session.ACTIVE_USER.getUser_id(), post_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // unsaved
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void shareComment(String commen_text, int comment_post) {
        apiService.shareComment(commen_text, comment_post, Session.ACTIVE_USER.getUser_id()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    getPostDetailsById(comment_post);
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }
                }

                message.setValue("");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void updateComment(Comment comment) {
        apiService.updateComment(comment).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    getPostDetailsById(comment.getComment_post());
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }
                }

                message.setValue("");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void deleteComment(Comment comment) {
        apiService.deleteComment(comment.getComment_id()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    getPostDetailsById(comment.getComment_post());
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }
                }

                message.setValue("");
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }
}
