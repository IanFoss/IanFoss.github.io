package com.example.CS499_Capstone

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemName: TextView
    var itemId: TextView
    var itemQuantity: TextView

    var itemCategory: TextView
    var deleteIcon: ImageView


    // Creates viewholders for each item attribute and a delete button
    init {
        itemId = itemView.findViewById<TextView>(R.id.item_id)
        itemName = itemView.findViewById<TextView>(R.id.item_name)
        itemQuantity = itemView.findViewById<TextView>(R.id.item_quantity)
        itemCategory = itemView.findViewById<TextView>(R.id.item_category)
        deleteIcon = itemView.findViewById<ImageView>(R.id.delete_icon)
    }

    // Binds the data to the viewholders
    fun bind(item: InventoryItemEntity, onDelete: Runnable) {
        itemId.text = item.id.toString()
        itemName.text = item.name
        itemQuantity.text = item.quantity.toString()
        itemCategory.text = item.category
        deleteIcon.setOnClickListener(View.OnClickListener { v: View? -> onDelete.run() })
    }
}
