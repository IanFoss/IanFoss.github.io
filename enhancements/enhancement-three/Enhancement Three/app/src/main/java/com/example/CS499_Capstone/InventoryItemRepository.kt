package com.example.CS499_Capstone

import android.content.Context
import androidx.lifecycle.LiveData
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.security.auth.callback.Callback

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
    fun insert(item: InventoryItemEntity, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        ioExecutor.execute(Runnable {
            try {itemDao.insertItem(item)
                onSuccess()}
            catch (e: Exception) {
                onError(e)
            }
        })
    }

    fun checkIfNameExists(name: String, callback: (Boolean) -> Unit) {
        ioExecutor.execute {
            val exists = itemDao.itemExists(name) > 0
            callback(exists)
        }
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

    // Sort inventory items by category
    fun getItemsSortedByCategory(): LiveData<List<InventoryItemEntity>> {
        return itemDao.sortItemsByCategory()
    }

    // Filter inventory list by low-stock items
    fun getLowStockItems(): LiveData<List<InventoryItemEntity>> {
        return itemDao.filterByLowStock()
    }

    // Filter inventory list by out-of-stock items
    fun getOutOfStockItems(): LiveData<List<InventoryItemEntity>> {
        return itemDao.filterByOutOfStock()
    }


}
