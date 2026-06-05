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


    @Insert
    fun insertItems(items: List<InventoryItemEntity>)

    @Query("SELECT * FROM inventory_table WHERE name = :name LIMIT 1")
    fun getItemByName(name: String): InventoryItemEntity


    // Search an item by name
    @Query("SELECT * FROM inventory_table WHERE name LIKE '%' || :searchText || '%' ORDER BY name ASC")
    fun searchItemsByName(searchText: String): LiveData<List<InventoryItemEntity>>


    // Sort items alphabetically
    @Query("SELECT * FROM inventory_table ORDER BY name ASC")
    fun sortItemsByName(): LiveData<List<InventoryItemEntity>>

    // Sort items by quantity
    @Query("SELECT * FROM inventory_table ORDER BY quantity ASC")
    fun sortItemsByQuantity(): LiveData<List<InventoryItemEntity>>


    // Sort items by category
    @Query("SELECT * FROM inventory_table ORDER BY category ASC")
    fun sortItemsByCategory(): LiveData<List<InventoryItemEntity>>


    // Filter for low-stock items
    @Query("SELECT * FROM inventory_table WHERE quantity <= lowStockThreshold ORDER BY quantity ASC")
    fun filterByLowStock(): LiveData<List<InventoryItemEntity>>


    // Filter for out-of-stock items
    @Query("SELECT * FROM inventory_table WHERE quantity = 0 ORDER BY name ASC")
    fun filterByOutOfStock(): LiveData<List<InventoryItemEntity>>



    // View all items
    @get:Query("SELECT * FROM inventory_table")
    val allItems: LiveData<List<InventoryItemEntity>>

    @Update
    fun updateItem(item: InventoryItemEntity)

    @Delete
    fun deleteItem(item: InventoryItemEntity)


    // Counts the number of unique items in the database
    @Query("SELECT COUNT(*) FROM inventory_table")
    fun getItemCount(): Int

    // Finds item with matching name in database
    @Query("SELECT COUNT(*) FROM inventory_table WHERE name = :itemName")
    fun itemExists(itemName: String): Int
}
