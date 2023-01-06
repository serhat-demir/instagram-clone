package com.serhat.instagram.ui.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.serhat.instagram.R;
import com.serhat.instagram.api.ApiUtils;
import com.serhat.instagram.databinding.ActivityMainBinding;
import com.serhat.instagram.localdb.Session;
import com.serhat.instagram.ui.viewmodel.MainViewModel;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (Session.ACTIVE_USER == null) {
            Intent intent = new Intent(context, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initMenu();

        // update drawer menu after user photo updated
        viewModel.getUser().observe(this, user -> {
            ImageView img = binding.navigationView.getHeaderView(0).findViewById(R.id.navHeaderImgUserPhoto);
            Picasso.get().load(ApiUtils.BASE_URL + getString(R.string.dir_profile_photos) + user.getUser_photo()).into(img);
        });

        // get notifications
        viewModel.getUnseenNotificationCount().observe(this, unseenNotificationCount -> {
            if (unseenNotificationCount > 0) binding.bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_notifications_active);
            else binding.bottomNavigationView.getMenu().getItem(3).setIcon(R.drawable.ic_notifications_empty);
        });

        viewModel.getAllNotifications();
    }

    private void initMenu() {
        // drawer menu
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.navigationView, navHostFragment.getNavController());
        binding.navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, 0, 0);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // load profile picture & username
        View navigationHeader = binding.navigationView.inflateHeaderView(R.layout.navigation_header);
        Picasso.get().load(ApiUtils.BASE_URL + getString(R.string.dir_profile_photos) + Session.ACTIVE_USER.getUser_photo()).into((ImageView) navigationHeader.findViewById(R.id.navHeaderImgUserPhoto));
        ((TextView) navigationHeader.findViewById(R.id.navHeaderLblUserName)).setText(Session.ACTIVE_USER.getUser_name());

        // bottom menu
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navHostFragment.getNavController());
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.feedFragment) navHostFragment.getNavController().navigate(R.id.feedFragment);
            else if (id == R.id.searchFragment) navHostFragment.getNavController().navigate(R.id.searchFragment);
            else if (id == R.id.newPostFragment) navHostFragment.getNavController().navigate(R.id.newPostFragment);
            else if (id == R.id.notificationsFragment) navHostFragment.getNavController().navigate(R.id.notificationsFragment);
            else if (id == R.id.profileFragment) navHostFragment.getNavController().navigate(R.id.profileFragment);
            else return false;

            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);

        if (id == R.id.actionProfile) navHostFragment.getNavController().navigate(R.id.profileFragment);
        else if (id == R.id.actionSavedPosts) navHostFragment.getNavController().navigate(R.id.savedPostsFragment);
        else if (id == R.id.actionSettings) navHostFragment.getNavController().navigate(R.id.settingsFragment);
        else if (id == R.id.actionLogOut) {
            Session.ACTIVE_USER = null;
            viewModel.removeLastSessionUser();

            Intent intent = new Intent(context, SignInActivity.class);
            startActivity(intent);
            finish();
        } else return false;

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}

