package com.example.overcooked;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
                    Log.d("TAG", "User is logged in");
                });
            } else {
                Log.d("TAG", "User needs to log in");
            }
        });
    }
}