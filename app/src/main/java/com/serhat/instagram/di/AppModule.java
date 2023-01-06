package com.serhat.instagram.di;

import android.content.Context;

import com.serhat.instagram.api.ApiInterface;
import com.serhat.instagram.api.ApiUtils;
import com.serhat.instagram.data.repository.NotificationRepository;
import com.serhat.instagram.data.repository.PostRepository;
import com.serhat.instagram.data.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    @Provides
    @Singleton
    public UserRepository provideUserRepository(@ApplicationContext Context context, ApiInterface apiService) {
        return new UserRepository(context, apiService);
    }

    @Provides
    @Singleton
    public PostRepository providePostRepository(ApiInterface apiService) {
        return new PostRepository(apiService);
    }

    @Provides
    @Singleton
    public NotificationRepository provideNotificationRepository(ApiInterface apiService) {
        return new NotificationRepository(apiService);
    }

    @Provides
    @Singleton
    public ApiInterface provideApiInterface() {
        return ApiUtils.getApiService();
    }
}
