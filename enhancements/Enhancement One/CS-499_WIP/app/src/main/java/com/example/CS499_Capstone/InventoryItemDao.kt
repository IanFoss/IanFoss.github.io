package com.example.CS499_Capstone

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Data access object for inventory items
@Dao
interface InventoryItemDao {
    @Insert
    fun insertItem(inventoryItem: InventoryItemEntity): Long


    @Query("SELECT * FROM inventory_table WHERE name = :name LIMIT 1")
    fun getItemByName(name: String): InventoryItemEntity


    @get:Query("SELECT * FROM inventory_table")
    val allItems: LiveData<MutableList<InventoryItemEntity>>

    @Update
    fun updateItem(item: InventoryItemEntity)

    @Delete
    fun deleteItem(item: InventoryItemEntity)
}
