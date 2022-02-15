package com.example.overcooked.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.overcooked.R;
import com.example.overcooked.helpers.IntentHelper;
import com.example.overcooked.login.LoginActivity;
import com.example.overcooked.model.Model;
import com.google.android.material.navigation.NavigationView;


public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavController navCtl;
    DrawerLayout drawerLayout;

    NavHost navHost;
    IntentHelper intentHelper = new IntentHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.base_navhost);
        navCtl = navHost.getNavController();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigationView);
        navView.setNavigationItemSelectedListener(this);
        navView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();


        //NavigationUI.setupWithNavController(navView, navCtl);
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
        Fragment fragment = null;
        switch (item.getItemId()) {
//            case R.id.menu_feed:
//                fragment = new FeedFragment();
//                break;
//            case R.id.menu_my_posts:
//                break;
//            case R.id.menu_profile:
//                break;
//            case R.id.createPostFragment:
////                Navigation.findNavController(navHost).navigate(R.id.action_global_createPostFragment);
//                NavigationUI.onNavDestinationSelected(item, navCtl);
//                break;
            case android.R.id.home:
                navCtl.navigateUp();
                break;
            case R.id.menu_sign_out:
                Model.instance.signOut(() -> toLoginActivity());
                break;
            default:
                NavigationUI.onNavDestinationSelected(item, navCtl);
                break;

        }

//        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.base_navhost,
//                    fragment).commit();
//        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getForegroundFragment(int id) {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(id);
        Log.d("Menuuu", "" + navHostFragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

}