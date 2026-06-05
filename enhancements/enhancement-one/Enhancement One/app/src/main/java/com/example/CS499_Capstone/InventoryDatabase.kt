package com.example.CS499_Capstone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import kotlin.concurrent.Volatile

// Database singleton class with entities for the users and inventory items
@Database(entities = [UserEntity::class, InventoryItemEntity::class], version = 2)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itemDao(): InventoryItemDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getInstance(context: Context): InventoryDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventory.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCE = instance
                instance
            }

        }
    }
}
