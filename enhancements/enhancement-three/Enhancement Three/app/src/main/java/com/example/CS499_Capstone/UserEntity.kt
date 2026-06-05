package com.example.CS499_Capstone

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table",
    indices = [Index(value = ["username"], unique = true)]
)
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,


    @ColumnInfo(name = "username")
    val username: String,


    @ColumnInfo(name = "passwordHash")
    val password: String,


    @ColumnInfo(name = "salt")
    val salt: String
)
