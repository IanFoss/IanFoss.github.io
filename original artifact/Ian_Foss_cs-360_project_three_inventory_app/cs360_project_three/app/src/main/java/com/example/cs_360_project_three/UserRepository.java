package com.example.cs_360_project_three;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UserRepository {

    private final UserDao userDao;
    private final Executor ioExecutor = Executors.newSingleThreadExecutor();


    public UserRepository(Context context) {
        InventoryDatabase db = InventoryDatabase.getInstance(context);
        userDao = db.userDao();
    }


    public void insert(UserEntity user) {
        ioExecutor.execute(() -> userDao.insertUser(user));
    }

    public void update(UserEntity user) {
        ioExecutor.execute(() -> userDao.updateUser(user));
    }


    // Gets user on background thread
    public void getUser(String username, Consumer<UserEntity> callback) {
        ioExecutor.execute(() -> {
            UserEntity u = userDao.getUserByUsername(username);
            callback.accept(u);
        });
    }

    // Gets user by username and password to validate if account exists
    public void validateUser(String username, String password, Consumer<UserEntity> callback) {
        ioExecutor.execute(() -> {
            UserEntity user = userDao.getUserByCredentials(username, password);
            callback.accept(user);  // null if not found
        });
    }

}
