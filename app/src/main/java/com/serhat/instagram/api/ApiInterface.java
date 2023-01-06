package com.serhat.instagram.api;

import com.serhat.instagram.data.model.Comment;
import com.serhat.instagram.data.model.Post;
import com.serhat.instagram.data.model.User;
import com.serhat.instagram.data.response.ApiResponse;
import com.serhat.instagram.data.response.NotificationListResponse;
import com.serhat.instagram.data.response.PostListResponse;
import com.serhat.instagram.data.response.PostResponse;
import com.serhat.instagram.data.response.UserListResponse;
import com.serhat.instagram.data.response.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    /*************************************
     * User Repository -> users, follows *
    *************************************/

    @GET("users.php")
    Call<UserListResponse> filterUsersByName(
            @Query("user_name") String user_name
    );

    @GET("users.php")
    Call<UserResponse> getUserDetailsById(
            @Query("user_id") int user_id
    );

    @POST("users.php")
    @FormUrlEncoded
    Call<UserResponse> signIn(
            @Field("user_name") String user_name,
            @Field("user_password") String user_password
    );

    @POST("users.php")
    @FormUrlEncoded
    Call<UserResponse> signUp(
            @Field("user_email") String user_email,
            @Field("user_name") String user_name,
            @Field("user_password") String user_password
    );

    @POST("users.php")
    @Multipart
    Call<UserResponse> changeProfilePhoto(
            @Part("user_id") int user_id,
            @Part MultipartBody.Part image
    );

    @POST("users.php")
    @FormUrlEncoded
    Call<UserResponse> removeProfilePhoto(
            @Field("user_id") int user_id,
            @Field("image_name") String image_name
    );

    @PUT("users.php")
    Call<UserResponse> updateUser(
            @Body User user
    );

    @POST("follow.php")
    @FormUrlEncoded
    Call<ApiResponse> follow(
            @Field("follower_id") int follower_id,
            @Field("following_id") int following_id
    );

    @DELETE("follow.php")
    Call<ApiResponse> unfollow(
            @Query("follower_id") int follower_id,
            @Query("following_id") int following_id
    );

    /*****************************************************
     * Post Repository -> posts, post likes, saved posts *
    *****************************************************/

    @GET("posts.php")
    Call<PostListResponse> getFeed(
            @Query("user_id") int user_id,
            @Query("is_feed") int is_feed
    );

    @GET("posts.php")
    Call<PostListResponse> getPostsByUserId(
            @Query("user_id") int user_id,
            @Query("is_feed") int is_feed
    );

    @GET("posts.php")
    Call<PostResponse> getPostDetailsById(
            @Query("post_id") int post_id
    );

    @POST("posts.php")
    @Multipart
    Call<ApiResponse> sharePost(
            @Part("post_description") RequestBody post_description,
            @Part("post_owner") int post_owner,
            @Part MultipartBody.Part image
    );

    @PUT("posts.php")
    Call<ApiResponse> updatePost(
            @Body Post post
    );

    @DELETE("posts.php")
    Call<ApiResponse> deletePost(
            @Query("post_id") int post_id
    );

    @POST("post_likes.php")
    @FormUrlEncoded
    Call<ApiResponse> like(
            @Field("user_id") int user_id,
            @Field("post_id") int post_id
    );

    @DELETE("post_likes.php")
    Call<ApiResponse> unlike(
            @Query("user_id") int user_id,
            @Query("post_id") int post_id
    );

    @GET("saved_posts.php")
    Call<PostListResponse> getSavedPosts(
            @Query("user_id") int user_id
    );

    @POST("saved_posts.php")
    @FormUrlEncoded
    Call<ApiResponse> save(
            @Field("user_id") int user_id,
            @Field("post_id") int post_id
    );

    @DELETE("saved_posts.php")
    Call<ApiResponse> unsave(
            @Query("user_id") int user_id,
            @Query("post_id") int post_id
    );

    /**********************************
     * Comment Repository -> comments *
    **********************************/

    @POST("comments.php")
    @FormUrlEncoded
    Call<ApiResponse> shareComment(
            @Field("comment_text") String comment_text,
            @Field("comment_post") int comment_post,
            @Field("comment_owner") int comment_owner
    );

    @PUT("comments.php")
    Call<ApiResponse> updateComment(
            @Body Comment comment
    );

    @DELETE("comments.php")
    Call<ApiResponse> deleteComment(
            @Query("comment_id") int comment_id
    );

    /********************************************
     * Notification Repository -> notifications *
    ********************************************/

    @GET("notifications.php")
    Call<NotificationListResponse> getAllNotifications(
            @Query("user_id") int user_id
    );

    @PUT("notifications.php")
    Call<ApiResponse> markAllNotificationsAsSeen(
            @Body User user
    );

    @DELETE("notifications.php")
    Call<ApiResponse> deleteAllNotifications(
            @Query("user_id") int user_id
    );
}
