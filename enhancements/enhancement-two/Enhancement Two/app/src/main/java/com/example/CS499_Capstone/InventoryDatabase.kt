package com.example.CS499_Capstone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors
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
                    .addCallback(seedDatabaseCallback(context.applicationContext))
                    .build()

                INSTANCE = instance
                instance
            }

        }

        private fun seedDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Executors.newSingleThreadExecutor().execute {
                        val database = getInstance(context)

                        database.itemDao().insertItems(SeedData.inventoryItems)
                    }

                }
            }
        }
    }
}
