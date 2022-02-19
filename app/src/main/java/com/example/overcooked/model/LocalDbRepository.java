package com.example.overcooked.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class, User.class}, version = 18)
abstract class LocalDbRepository extends RoomDatabase{
    public abstract PostDao postDao();
    public abstract UserDao userDao();
}
