package com.example.cs_360_project_three;

import androidx.room.Entity;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import androidx.room.PrimaryKey;


// Entity class for the inventory items
@Entity(tableName = "inventory_table")
public class InventoryItemEntity {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    public InventoryItemEntity(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public @NonNull String getName() {
        return name;
    }


    public @NonNull void setName(String name) {
        this.name = name;
    }

    public  @NonNull int getQuantity() {
        return quantity;
    }

    public @NonNull void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


}
