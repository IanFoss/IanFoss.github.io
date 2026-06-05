package com.example.CS499_Capstone

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Activity class that shows the inventory item grid
class InventoryActivity : AppCompatActivity() {
    private lateinit var itemRepo: InventoryItemRepository
    private lateinit var adapter: InventoryAdapter

    private var currentInventorySource: LiveData<List<InventoryItemEntity>>? = null
    private val itemList = mutableListOf<InventoryItemEntity>()

    private fun observeInventory(source: LiveData<List<InventoryItemEntity>>) {
        currentInventorySource?.removeObservers(this)

        currentInventorySource = source

        source.observe(this) { items ->
            adapter.setItems(items)
        }
    }


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_inventory)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.main),
            OnApplyWindowInsetsListener { v: View, insets: WindowInsetsCompat ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        itemRepo = InventoryItemRepository(this@InventoryActivity)

        // Instantiates recyclerview, adds divider, sets it's layoutmanager and adapter
        val recyclerView = findViewById<RecyclerView>(R.id.inventory_recycler_view)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setLayoutManager(LinearLayoutManager(this))


        // Instantiates adapter with the context, item list, item repository class, and the dialog to edit item quantities
        adapter = InventoryAdapter(
            this@InventoryActivity,
            itemList,
            itemRepo,
            object : InventoryAdapter.OnItemClickListener {
                override fun onItemClick(item: InventoryItemEntity) {
                showEditQuantityDialog(item)}
            })
        recyclerView.setAdapter(adapter)

         observeInventory(itemRepo.allItems)



        // Floating action button to add item to inventory
        val fabAddItem = findViewById<FloatingActionButton>(R.id.add_item_fab)
         fabAddItem.imageTintList = ColorStateList.valueOf(Color.WHITE)
        fabAddItem.setOnClickListener(View.OnClickListener { v: View ->

            // Inflates dialog layout when button it pressed
            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.dialog_add_item, null)

            val nameInput = dialogView.findViewById<EditText>(R.id.edit_name)
            val quantityInput = dialogView.findViewById<EditText>(R.id.edit_quantity)

            // Dialog displays fields for item name and quantity
            // Ensures quantity is an integer
            AlertDialog.Builder(this)
                .setTitle("Add Inventory Item")
                .setView(dialogView)
                .setPositiveButton(
                    "Add",
                    (DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->

                        var name = ""
                        while(name.isEmpty()) {
                            name = nameInput.text.toString()

                            if (name.isEmpty()) {
                                Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show()
                                return@OnClickListener
                            }
                        }
                        val quantityString = quantityInput.text.toString()

                        var quantity = 0

                        try {
                            quantity = quantityString.toInt()
                            if (quantity < 0) {
                                throw NumberFormatException()
                            }
                        } catch (e: NumberFormatException) {
                            Toast.makeText(this, "Invalid Quantity", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }

                        // Create new item entity and insert into database using repository
                        val newItem = InventoryItemEntity(name = name, quantity = quantity)
                        itemRepo.insert(newItem)
                    })
                )
                .setNegativeButton("Cancel", null)
                .show()
        })

         val fabSearch = findViewById<FloatingActionButton>(R.id.floatingActionButton)
         fabSearch.imageTintList = ColorStateList.valueOf(Color.WHITE)
         fabSearch.setOnClickListener(View.OnClickListener { v: View ->
            showInventoryOptionsDialog()
         })
    }

    fun showInventoryOptionsDialog() {
        val options = arrayOf(
            "Search by name",
            "Sort by name",
            "Sort by quantity",
            "Show low stock",
            "Show out of stock",
            "Show all items"
        )

        AlertDialog.Builder(this)
            .setTitle("Inventory Options")
            .setItems(options) { _, which ->
                when(which) {
                    0 -> showSearchDialog()
                    1 -> observeInventory(itemRepo.getItemsSortedByName())
                    2 -> observeInventory(itemRepo.getItemsSortedByQuantity())
                    3 -> observeInventory(itemRepo.getLowStockItems(10))
                    4 -> observeInventory(itemRepo.getOutOfStockItems())
                    5 -> observeInventory(itemRepo.allItems)
                }
            }.show()
    }


    fun showSearchDialog() {
        val input = EditText(this)
        input.hint = "Enter item name"

        AlertDialog.Builder(this)
            .setTitle("Search Inventory")
            .setView(input)
            .setPositiveButton("Search") { _, _ ->
                val searchText = input.text.toString().trim()

                if (searchText.isBlank()) {
                    Toast.makeText(this, "Search cannot be empty.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                observeInventory(itemRepo.searchItemsByName(searchText))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }





    // Method allows user to edit item quantity through a dialog
    fun showEditQuantityDialog(item: InventoryItemEntity) {
        val dialogView = getLayoutInflater().inflate(R.layout.dialog_update_quantity, null)

        val qtyInput = dialogView.findViewById<EditText>(R.id.change_quantity)
        qtyInput.setText(item.quantity.toString())
        val title = "Change quantity for " + item.name + "?"

        // Updates entity with new quantity
        AlertDialog.Builder(this@InventoryActivity).setTitle(title).setView(dialogView)
            .setPositiveButton(
                "Save",
                (DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    val newQuantity: Int
                    try {
                        newQuantity = qtyInput.getText().toString().toInt()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(this@InventoryActivity, "Invalid Number", Toast.LENGTH_SHORT)
                            .show()
                        return@OnClickListener
                    }
                    item.quantity = newQuantity
                    itemRepo.update(item)

                    // Sends text message if item quantity is zero
                    if (item.quantity == 0) {
                        val itemName = item.name
                        val message = "$itemName is out of stock."
                        sendLowQuantityMessage("1234", message)
                    }
                })
            ).setNegativeButton("Cancel", null).show()
    }


    // Ensures permission is granted to send text message
    private fun sendLowQuantityMessage(destination: String, message: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            SmsManager.getDefault().sendTextMessage(destination, null, message, null, null)
        } else {
            return
        }
    }
}