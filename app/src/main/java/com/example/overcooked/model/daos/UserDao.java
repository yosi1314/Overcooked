package com.example.overcooked.model.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.overcooked.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from User")
    List<User> getAll();

    @Query("select * from User where uid=:uid")
    User getUserById(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Update
    void updateUser(User user);
}
