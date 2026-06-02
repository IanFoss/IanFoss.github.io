package com.example.cs_360_project_three;

import android.Manifest;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.pm.PackageManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


// Activity class that shows the inventory item grid
public class InventoryActivity extends AppCompatActivity {
    private InventoryItemRepository itemRepo;
    private InventoryAdapter adapter;
    private List<InventoryItemEntity> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        itemRepo = new InventoryItemRepository(InventoryActivity.this);

        // Instantiates recyclerview, adds divider, sets it's layoutmanager and adapter
        RecyclerView recyclerView = findViewById(R.id.inventory_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Instantiates adapter with the context, item list, item repository class, and the dialog to edit item quantities
        adapter = new InventoryAdapter(InventoryActivity.this, itemList, itemRepo, this::showEditQuantityDialog);
        recyclerView.setAdapter(adapter);

        itemRepo.getAllItems().observe(InventoryActivity.this, items -> {
            adapter.setItems(items);
        });



        // Floating action button to add item to inventory
        FloatingActionButton fab = findViewById(R.id.add_item_fab);
        fab.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        fab.setOnClickListener(v -> {

            // Inflates dialog layout when button it pressed
            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.dialog_add_item, null);

            EditText nameInput = dialogView.findViewById(R.id.edit_name);
            EditText quantityInput = dialogView.findViewById(R.id.edit_quantity);

            // Dialog displays fields for item name and quantity
            // Ensures quantity is an integer
            new AlertDialog.Builder(this)
                    .setTitle("Add Inventory Item")
                    .setView(dialogView)
                    .setPositiveButton("Add", ((dialog, which) -> {
                        String name = nameInput.getText().toString();
                        String quantityString = quantityInput.getText().toString();

                        int quantity = 0;

                        try {
                            quantity = Integer.parseInt(quantityString);
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Create new item entity and insert into database using repository
                        InventoryItemEntity newItem = new InventoryItemEntity(name, quantity);
                        InventoryItemRepository itemRepo = new InventoryItemRepository(InventoryActivity.this);
                        itemRepo.insert(newItem);
                     }))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }


    // Method allows user to edit item quantity through a dialog
    public void showEditQuantityDialog(InventoryItemEntity item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_quantity, null);

        EditText qtyInput = dialogView.findViewById(R.id.change_quantity);
        qtyInput.setText(String.valueOf(item.getQuantity()));
        String title = "Change quantity for " + item.getName() + "?";

        // Updates entity with new quantity
        new AlertDialog.Builder(InventoryActivity.this).setTitle(title).setView(dialogView)
                .setPositiveButton("Save", ((dialog, which) -> {
                    int newQuantity;
                    try {
                        newQuantity = Integer.parseInt(qtyInput.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(InventoryActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    item.setQuantity(newQuantity);
                    itemRepo.update(item);

                    // Sends text message if item quantity is zero
                    if (item.getQuantity() == 0) {
                        String itemName = item.getName();
                        String message = itemName + " is out of stock.";
                        sendLowQuantityMessage("1234", message);
                    }
                })).setNegativeButton("Cancel", null).show();
    }


    // Ensures permission is granted to send text message
    private void sendLowQuantityMessage(String destination, String message) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            SmsManager.getDefault().sendTextMessage(destination, null, message, null, null);
        }
        else {
            return;
        }
    }
}