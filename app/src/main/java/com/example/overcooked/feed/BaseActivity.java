package com.example.overcooked.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.overcooked.R;
import com.example.overcooked.helpers.IntentHelper;
import com.example.overcooked.login.LoginActivity;
import com.example.overcooked.model.Model;
import com.example.overcooked.model.Post;
import com.example.overcooked.model.User;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BaseActivityViewModel viewModel;
    NavController navCtl;
    DrawerLayout drawerLayout;
    NavHost navHost;

    ImageView profileImgImv;
    TextView displayNameTv;
    TextView emailTv;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        viewModel = new ViewModelProvider(this).get(BaseActivityViewModel.class);

        navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtl = navHost.getNavController();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigationView);
        navView.setNavigationItemSelectedListener(this);
        navView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                initUser();
                profileImgImv = findViewById(R.id.menu_img_imv);
                displayNameTv = findViewById(R.id.menu_display_name_tv);
                emailTv = findViewById(R.id.menu_email_tv);

                setUserDetails();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                if (getCurrentFocus() != null) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        };

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    private void initUser() {
        user = viewModel.getUserById(Model.instance.getCurrentUserUID()).getValue();
    }

    private void setUserDetails() {
        if (user == null) return;

        displayNameTv.setText(user.getDisplayName());
        emailTv.setText(user.getEmail());
        if(user.getImg() != null){
            Picasso.get().load(user.getImg()).into(profileImgImv);
        } else {
            profileImgImv.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        if(item.getItemId() != navCtl.getCurrentDestination().getId()){
            switch (item.getItemId()) {
                case android.R.id.home:
                    navCtl.navigateUp();
                    break;
                case R.id.menu_feed:
                    navCtl.navigate(R.id.action_global_feedFragment);
                    break;
                case R.id.menu_my_posts:
                    bundle.putString("userUid", Model.instance.getCurrentUserUID());
                    navCtl.navigate(R.id.action_global_feedFragment, bundle);
                    break;
                case R.id.menu_sign_out:
                    Model.instance.signOut(() -> toLoginActivity());
                    break;
                default:
                    NavigationUI.onNavDestinationSelected(item, navCtl);
                    break;
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}