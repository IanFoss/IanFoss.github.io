package com.example.CS499_Capstone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import kotlin.collections.MutableList

class InventoryAdapter(
    private val context: Context,
    private var itemList: List<InventoryItemEntity>,
    var repo: InventoryItemRepository,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(item: InventoryItemEntity)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }


    // Inflates views for recyclerview header row and item rows
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.getContext())

        if (viewType == VIEW_TYPE_HEADER) {
            val view = inflater.inflate(R.layout.recyclerview_header_row, parent, false)
            return HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.recyclerview_inventory_row, parent, false)
            return InventoryViewHolder(view)
        }
    }


    // Binds InventoryItem data to the viewholders
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Ensure the header row does not get the data

        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            val dataPosition = position - 1
            val item = itemList.get(dataPosition)
            (holder as InventoryViewHolder).bind(item, Runnable {
                repo.delete(item)
            })

            holder.itemView.setOnClickListener(View.OnClickListener { v: View ->
                clickListener.onItemClick(
                    item
                )
            })
        }
    }


    override fun getItemCount(): Int {
        return itemList.size + 1
    }


    fun setItems(newItems: List<InventoryItemEntity>) {
        itemList = newItems.toMutableList()
        notifyDataSetChanged()
    }


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}
