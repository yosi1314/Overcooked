package com.example.overcooked.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.overcooked.R;

public class LoginActivity extends AppCompatActivity {
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.login_navhost);
        navController = navHost.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(!super.onOptionsItemSelected(item)){
            switch (item.getItemId()) {
                case android.R.id.home:
                    navController.navigateUp();
                    return true;
                default:
                    NavigationUI.onNavDestinationSelected(item, navController);
            }
        } else {
            return true;
        }
        return false;
    }
}