package com.example.overcooked;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.overcooked.feed.BaseActivity;
import com.example.overcooked.login.LoginActivity;
import com.example.overcooked.model.Model;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Model.instance.isSignedIn()) {
                Model.instance.mainThread.post(() -> {
                    toFeedActivity();

                });
            } else {
                toLoginActivity();
            }
        });
    }

    private void toLoginActivity() {
        Log.d("TAG", "User is logged in");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Log.d("TAG", "User needs to log in");
        Intent intent = new Intent(this, BaseActivity.class);
        startActivity(intent);
        finish();
    }
}