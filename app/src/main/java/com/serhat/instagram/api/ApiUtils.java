package com.serhat.instagram.api;

public class ApiUtils {
    public static final String BASE_URL = "http://192.168.1.39/instagram-clone-api/api/v1/";

    public static ApiInterface getApiService() {
        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
