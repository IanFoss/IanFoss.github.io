package com.example.cs_360_project_three;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Data access object for inventory items
@Dao
public interface InventoryItemDao {

    @Insert
    long insertItem(InventoryItemEntity inventoryItem);


    @Query("SELECT * FROM inventory_table WHERE name = :name LIMIT 1")
    InventoryItemEntity getItemByName(String name);


    // Returns live data object so grid updates in real time
    @Query("SELECT * FROM inventory_table")
    LiveData<List<InventoryItemEntity>> getAllItems();

    @Update
    void updateItem(InventoryItemEntity item);

    @Delete
    void deleteItem(InventoryItemEntity item);

}
