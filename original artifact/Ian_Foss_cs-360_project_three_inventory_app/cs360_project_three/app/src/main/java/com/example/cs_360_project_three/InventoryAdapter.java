package com.example.cs_360_project_three;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(InventoryItemEntity item);
    }

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    InventoryItemRepository repo;
    private List<InventoryItemEntity> itemList;
    private final OnItemClickListener clickListener;

    private Context context;

    public InventoryAdapter(Context context, List<InventoryItemEntity> itemList, InventoryItemRepository repo, OnItemClickListener clickListener) {
        this.itemList = itemList;
        this.context = context;
        this.repo = repo;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 ) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }


    // Inflates views for recyclerview header row and item rows
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER) {
            View view = inflater.inflate(R.layout.recyclerview_header_row, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.recyclerview_inventory_row, parent, false);
            return new InventoryViewHolder(view);
        }
    }


    // Binds InventoryItem data to the viewholders
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

         // Ensure the header row does not get the data
         if (getItemViewType(position) == VIEW_TYPE_ITEM) {
             int dataPosition = position - 1;
             InventoryItemEntity item = itemList.get(dataPosition);
             ((InventoryViewHolder) holder).bind(item, () -> {
                repo.delete(item);
             });

             holder.itemView.setOnClickListener(v -> clickListener.onItemClick(item));
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size() + 1 ;
    }


    public void setItems(List<InventoryItemEntity> newItems) {
        this.itemList = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }



}
