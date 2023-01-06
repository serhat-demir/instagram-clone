package com.serhat.instagram.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.serhat.instagram.api.ApiInterface;
import com.serhat.instagram.data.model.User;
import com.serhat.instagram.data.response.ApiResponse;
import com.serhat.instagram.data.response.UserListResponse;
import com.serhat.instagram.data.response.UserResponse;
import com.serhat.instagram.localdb.LocalDataManager;
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

public class UserRepository {
    private final Context context;
    private final ApiInterface apiService;
    private MutableLiveData<List<User>> users; // search fragment
    private MutableLiveData<User> user; // user profile fragment & navigation drawer menu
    private MutableLiveData<String> message; // toast messages
    private MutableLiveData<Boolean> status; // proccess status for navigation

    public UserRepository(Context context, ApiInterface apiService) {
        this.apiService = apiService;
        this.context = context;

        users = new MutableLiveData<>();
        user = new MutableLiveData<>();
        message = new MutableLiveData<>();
        status = new MutableLiveData<>();
    }

    public MutableLiveData<List<User>> getUsers() {
        users = new MutableLiveData<>();
        return users;
    }

    public MutableLiveData<User> getUser() {
        user = new MutableLiveData<>();
        return user;
    }

    public MutableLiveData<String> getMessage() {
        message = new MutableLiveData<>();
        return message;
    }

    public MutableLiveData<Boolean> getStatus() {
        status = new MutableLiveData<>();
        return status;
    }

    public void getLastSessionUser() {
        String user_name = LocalDataManager.getSharedPreference(context, "user_name", "");
        String user_password = LocalDataManager.getSharedPreference(context, "user_password", "");

        if (!user_name.isEmpty() && !user_password.isEmpty()) {
            signIn(user_name, user_password);
        }
    }

    private void setLastSessionUser(String user_name, String user_password) {
        LocalDataManager.setSharedPreference(context, "user_name", user_name);
        LocalDataManager.setSharedPreference(context, "user_password", user_password);
    }

    public void removeLastSessionUser() {
        LocalDataManager.removeSharedPreference(context, "user_name");
        LocalDataManager.removeSharedPreference(context, "user_password");
    }

    public void signIn(String user_name, String user_password) {
        apiService.signIn(user_name, user_password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Session.ACTIVE_USER = response.body().getUser();
                    status.setValue(true);

                    setLastSessionUser(user_name, user_password);
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }

                    message.setValue("");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void signUp(String user_email, String user_name, String user_password) {
        apiService.signUp(user_email, user_name, user_password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Session.ACTIVE_USER = response.body().getUser();
                    status.setValue(true);

                    setLastSessionUser(user_name, user_password);
                } else {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        message.setValue(errorResponse.getString("message"));
                    } catch (Exception e) {
                        message.setValue("Something went wrong.");
                    }

                    message.setValue("");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void changeProfilePhoto(File profilePhoto) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), profilePhoto);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", profilePhoto.getName(), requestFile);

        apiService.changeProfilePhoto(Session.ACTIVE_USER.getUser_id(), body).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    status.setValue(false);
                    status.setValue(null);

                    Session.ACTIVE_USER.setUser_photo(response.body().getUser().getUser_photo());
                    user.setValue(response.body().getUser()); // update drawer menu
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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void removeProfilePhoto() {
        apiService.removeProfilePhoto(Session.ACTIVE_USER.getUser_id(), Session.ACTIVE_USER.getUser_photo()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());
                    status.setValue(true);
                    status.setValue(null);

                    Session.ACTIVE_USER.setUser_photo(response.body().getUser().getUser_photo());
                    user.setValue(response.body().getUser()); // update drawer menu
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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void updateUser(User newUser) {
        apiService.updateUser(newUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    message.setValue(response.body().getMessage());

                    if (!Session.ACTIVE_USER.getUser_password().equals(response.body().getUser().getUser_password())) {
                        removeLastSessionUser(); // remove user data from shared preferences if user password has changed
                    }

                    Session.ACTIVE_USER = response.body().getUser();
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
            public void onFailure(Call<UserResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void filterUsersByName(String user_name) {
        apiService.filterUsersByName(user_name).enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getUsers() != null) users.setValue(response.body().getUsers());
                else users.setValue(new ArrayList<>());
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void getUserDetailsById(int user_id) {
        apiService.getUserDetailsById(user_id).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user.setValue(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                message.setValue("Something went wrong.");
                message.setValue("");
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void follow(int user_id) {
        apiService.follow(Session.ACTIVE_USER.getUser_id(), user_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // follow
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }

    public void unfollow(int user_id) {
        apiService.unfollow(Session.ACTIVE_USER.getUser_id(), user_id).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // unfollow
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("log_error", t.getMessage());
            }
        });
    }
}
