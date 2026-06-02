package com.example.cs_360_project_three;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


// Database singleton class with entities for the users and inventory items
@Database(entities = {UserEntity.class, InventoryItemEntity.class}, version = 2)
public abstract class InventoryDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract InventoryItemDao itemDao();

    private static volatile InventoryDatabase INSTANCE;

    public static InventoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (InventoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            InventoryDatabase.class,
                            "inventory.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
