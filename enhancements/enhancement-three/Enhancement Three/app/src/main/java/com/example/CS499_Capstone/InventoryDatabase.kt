package com.example.CS499_Capstone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Builder
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

// Database singleton class with entities for the users and inventory items
@Database(entities = [UserEntity::class, InventoryItemEntity::class], version = 4)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun itemDao(): InventoryItemDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE user_table ADD COLUMN passwordHash TEXT NOT NULL DEFAULT ''"
                )

                db.execSQL(
                    "ALTER TABLE user_table ADD COLUMN salt TEXT NOT NULL DEFAULT ''"
                )

                db.execSQL(
                    "ALTER TABLE inventory_table ADD COLUMN category TEXT NOT NULL DEFAULT 'General'"
                )

                db.execSQL(
                    "ALTER TABLE inventory_table ADD COLUMN lowStockThreshold INTEGER NOT NULL DEFAULT 10"
                )

                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_user_table_username ON user_table(username)"
                )

                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_inventory_table_name ON inventory_table(name)"
                )
            }
        }


        fun getInstance(context: Context): InventoryDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventory.db"
                )
                    .addMigrations(MIGRATION_3_4)
                    .addCallback(seedDatabaseCallback(context.applicationContext))
                    .build()

                INSTANCE = instance
                instance
            }

        }

        private fun seedDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)

                    Executors.newSingleThreadExecutor().execute {
                        val database = getInstance(context)
                    if (database.itemDao().getItemCount() == 0) {
                        database.itemDao().insertItems(SeedData.inventoryItems)
                    }
                    }

                }
            }
        }
    }
}
