package com.example.CS499_Capstone

import android.content.Context
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.function.Consumer

class UserRepository(context: Context) {
    private val userDao: UserDao
    private val ioExecutor: Executor = Executors.newSingleThreadExecutor()


    init {
        val db = InventoryDatabase.getInstance(context)
        userDao = db.userDao()
    }


    fun insert(user: UserEntity) {
        ioExecutor.execute(Runnable { userDao.insertUser(user) })
    }

    fun update(user: UserEntity) {
        ioExecutor.execute(Runnable { userDao.updateUser(user) })
    }

    fun usernameExists(username: String, callback: (Boolean) -> Unit) {

        ioExecutor.execute {
            val exists = userDao.usernameExists(username) > 0
            callback(exists)
        }
    }


    // Gets user on background thread
    fun getUser(username: String, callback: (UserEntity?) -> Unit) {
        ioExecutor.execute(Runnable {
            val u = userDao.getUserByUsername(username)
            callback(u)
        })
    }

    // Gets user by username and password to validate if account exists
    fun validateUser(username: String, password: String, callback: (UserEntity?) -> Unit) {
        ioExecutor.execute(Runnable {
            val user = userDao.getUserByUsername(username)

            if (user == null) {
                callback(user) // null if not found
                return@Runnable
            }
            val isValidPassword = PasswordUtils.verifyPassword(
                password,
                user.salt,
                user.password
            )
            if (isValidPassword) {
                callback(user)
            }
            else {
                callback(null)
            }
        })
    }
}
