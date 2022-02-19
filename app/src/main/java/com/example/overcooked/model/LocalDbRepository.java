package com.example.overcooked.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.overcooked.model.daos.PostDao;
import com.example.overcooked.model.daos.UserDao;

@Database(entities = {Post.class, User.class}, version = 22)
abstract class LocalDbRepository extends RoomDatabase{
    public abstract PostDao postDao();
    public abstract UserDao userDao();
}
