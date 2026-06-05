package com.example.CS499_Capstone

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserEntity): Long


    // Get one user that matches username
    @Query("SELECT * FROM user_table WHERE username = :username LIMIT 1")
    fun getUserByUsername(username: String): UserEntity


    // Query used to validate login credentials
    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password LIMIT 1")
    fun getUserByCredentials(username: String, password: String): UserEntity?


    // Query to see if username already exists
    @Query("SELECT COUNT(*) FROM user_table WHERE username = :username")
    fun usernameExists(username: String): Int


    @Update
    fun updateUser(user: UserEntity)

    @Delete
    fun deleteUser(user: UserEntity)
}


