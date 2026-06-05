package com.example.CS499_Capstone

import android.content.Context
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors

// Repository class for inventory items
class InventoryItemRepository(context: Context) {
    private val itemDao: InventoryItemDao

    // Executor for performing writes off of the main thread
    private val ioExecutor: Executor = Executors.newSingleThreadExecutor()


    // Instantiates database singleton
    init {
        val db = InventoryDatabase.getInstance(context)
        itemDao = db.itemDao()
    }

    /*--------------  CRUD operations using executor for background threads  -----------------*/
    fun insert(item: InventoryItemEntity) {
        ioExecutor.execute(Runnable { itemDao.insertItem(item) })
    }


    fun update(item: InventoryItemEntity) {
        ioExecutor.execute(Runnable { itemDao.updateItem(item) })
    }


    fun delete(item: InventoryItemEntity) {
        ioExecutor.execute(Runnable { itemDao.deleteItem(item) })
    }


    val allItems: LiveData<List<InventoryItemEntity>>
        // Returns live data object of all items in database
        get() = itemDao.allItems


    // Find items in inventory by name
    fun searchItemsByName(searchText: String): LiveData<List<InventoryItemEntity>> {
        return itemDao.searchItemsByName(searchText)
    }

    // Sort inventory items alphabetically
    fun getItemsSortedByName(): LiveData<List<InventoryItemEntity>> {
        return itemDao.sortItemsByName()
    }

    // Sort inventory items by quantity
    fun getItemsSortedByQuantity(): LiveData<List<InventoryItemEntity>> {
        return itemDao.sortItemsByQuantity()
    }

    // Filter inventory list by low-stock items
    fun getLowStockItems(threshold: Int): LiveData<List<InventoryItemEntity>> {
        return itemDao.filterByLowStock(threshold)
    }

    // Filter inventory list by out-of-stock items
    fun getOutOfStockItems(): LiveData<List<InventoryItemEntity>> {
        return itemDao.filterByOutOfStock()
    }


}
