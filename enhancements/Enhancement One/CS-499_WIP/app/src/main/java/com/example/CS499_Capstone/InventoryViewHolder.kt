package com.example.CS499_Capstone

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemName: TextView
    var itemId: TextView
    var itemQuantity: TextView
    var deleteIcon: ImageView


    // Creates viewholders for each item attribute and a delete button
    init {
        itemId = itemView.findViewById<TextView>(R.id.item_id)
        itemName = itemView.findViewById<TextView>(R.id.item_name)
        itemQuantity = itemView.findViewById<TextView>(R.id.item_quantity)
        deleteIcon = itemView.findViewById<ImageView>(R.id.delete_icon)
    }

    // Binds the data to the viewholders
    fun bind(item: InventoryItemEntity, onDelete: Runnable) {
        itemId.setText(item.id.toString())
        itemName.setText(item.name)
        itemQuantity.setText(item.quantity.toString())
        deleteIcon.setOnClickListener(View.OnClickListener { v: View? -> onDelete.run() })
    }
}
