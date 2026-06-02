package com.example.cs_360_project_three;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cs_360_project_three.UserEntity;
@Dao
public interface UserDao {

    @Insert
    long insertUser(UserEntity user);


    // Get one user that matches username
    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    UserEntity getUserByUsername(String username);


    // Query used to validate login credentials
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    UserEntity getUserByCredentials(String username, String password);


    // Query to see if username already exists
    @Query("SELECT COUNT(*) FROM user_table WHERE username = :username")
    int usernameExists(String username);


    @Update
    void updateUser(UserEntity user);

    @Delete
    void deleteUser(UserEntity user);



}


