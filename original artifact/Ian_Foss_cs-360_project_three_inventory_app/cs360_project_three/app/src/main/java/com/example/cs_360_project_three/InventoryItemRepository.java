package com.example.cs_360_project_three;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



// Repository class for inventory items
public class InventoryItemRepository {

    private final InventoryItemDao itemDao;

    // Executor for performing writes off of the main thread
    private final Executor ioExecutor = Executors.newSingleThreadExecutor();


    // Instantiates database singleton
    public InventoryItemRepository(Context context) {
        InventoryDatabase db = InventoryDatabase.getInstance(context);
        itemDao = db.itemDao();
    }

    /*--------------  CRUD operations using executor for background threads  -----------------*/
    public void insert(InventoryItemEntity item) {
        ioExecutor.execute(() -> itemDao.insertItem(item));
    }


    public void update(InventoryItemEntity item) {
        ioExecutor.execute(() -> itemDao.updateItem(item));
    }


    public void delete(InventoryItemEntity item) {
        ioExecutor.execute(() -> itemDao.deleteItem(item));
    }


    // Returns live data object of all items in database
    public LiveData<List<InventoryItemEntity>> getAllItems() {
        return itemDao.getAllItems();
    }

}
