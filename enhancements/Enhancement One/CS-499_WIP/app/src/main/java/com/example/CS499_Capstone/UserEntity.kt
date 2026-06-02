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

    @JvmField
    @ColumnInfo(name = "username")
    val username: String,

    @JvmField
    @ColumnInfo(name = "password")
    val password: String
)
