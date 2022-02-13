package com.example.overcooked.helpers;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.overcooked.feed.BaseActivity;

public class IntentHelper {

    public void toFeedActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), BaseActivity.class);
        fragment.startActivity(intent);
        fragment.getActivity().finish();
    }

}
