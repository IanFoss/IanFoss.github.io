package com.example.cs_360_project_three;

import androidx.room.Entity;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import androidx.room.PrimaryKey;


@Entity(tableName = "user_table")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;



    public long getId() {return id;}
    public @NonNull String getUsername() {return username;}
    public @NonNull String getPassword() {return password;}


    public void setId(long id) { this.id = id; }
    public void setUsername(@NonNull String username) {this.username = username;}
    public void setPassword(@NonNull String password) {this.password = password;}


}
