package com.serhat.instagram.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {
    @SerializedName("user_id")
    private Integer user_id;

    @SerializedName("user_email")
    private String user_email;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_password")
    private String user_password;

    @SerializedName("user_fullname")
    private String user_fullname;

    @SerializedName("user_photo")
    private String user_photo;

    @SerializedName("user_bio")
    private String user_bio;

    @SerializedName("user_profile_private")
    private Integer user_profile_private;

    @SerializedName("followers")
    private List<User> followers;

    @SerializedName("following")
    private List<User> following;

    public User() {
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readInt();
        }
        user_email = in.readString();
        user_name = in.readString();
        user_password = in.readString();
        user_fullname = in.readString();
        user_photo = in.readString();
        user_bio = in.readString();
        if (in.readByte() == 0) {
            user_profile_private = null;
        } else {
            user_profile_private = in.readInt();
        }
        followers = in.createTypedArrayList(User.CREATOR);
        following = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Boolean followerListContains(int user_id) {
        for (User u : followers) {
            if (u.user_id == user_id) {
                return true;
            }
        }

        return false;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getUser_bio() {
        return user_bio;
    }

    public void setUser_bio(String user_bio) {
        this.user_bio = user_bio;
    }

    public Integer getUser_profile_private() {
        return user_profile_private;
    }

    public void setUser_profile_private(Integer user_profile_private) {
        this.user_profile_private = user_profile_private;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void setFollowing(List<User> following) {
        this.following = following;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (user_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(user_id);
        }
        dest.writeString(user_email);
        dest.writeString(user_name);
        dest.writeString(user_password);
        dest.writeString(user_fullname);
        dest.writeString(user_photo);
        dest.writeString(user_bio);
        if (user_profile_private == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(user_profile_private);
        }
        dest.writeTypedList(followers);
        dest.writeTypedList(following);
    }
}
