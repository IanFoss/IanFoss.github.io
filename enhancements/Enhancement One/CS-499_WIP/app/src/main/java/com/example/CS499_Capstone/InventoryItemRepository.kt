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


    val allItems: LiveData<MutableList<InventoryItemEntity>>
        // Returns live data object of all items in database
        get() = itemDao.allItems
}
