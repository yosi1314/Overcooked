package com.example.overcooked.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class}, version = 7)
abstract class LocalDbRepository extends RoomDatabase{
    public abstract PostDao postDao();
}