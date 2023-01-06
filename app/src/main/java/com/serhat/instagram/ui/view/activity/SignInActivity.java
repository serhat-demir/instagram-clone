package com.serhat.instagram.ui.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.serhat.instagram.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
}