package com.example.overcooked.model;

import androidx.room.Room;

import com.example.overcooked.MyApplication;

public class LocalDb {
    static public LocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    LocalDbRepository.class,
                    "overcooked.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
