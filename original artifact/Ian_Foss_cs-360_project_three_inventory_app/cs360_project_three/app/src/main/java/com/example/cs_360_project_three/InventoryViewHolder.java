package com.example.cs_360_project_three;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InventoryViewHolder extends RecyclerView.ViewHolder {

    TextView itemName;
    TextView itemId;
    TextView itemQuantity;
    ImageView deleteIcon;


    // Creates viewholders for each item attribute and a delete button
    public InventoryViewHolder(@NonNull View itemView) {
        super(itemView);
        itemId = itemView.findViewById(R.id.item_id);
        itemName = itemView.findViewById(R.id.item_name);
        itemQuantity = itemView.findViewById(R.id.item_quantity);
        deleteIcon = itemView.findViewById(R.id.delete_icon);
    }

    // Binds the data to the viewholders
    public void bind(InventoryItemEntity item, Runnable onDelete) {
        itemId.setText(String.valueOf(item.getId()));
        itemName.setText(item.getName());
        itemQuantity.setText(String.valueOf(item.getQuantity()));
        deleteIcon.setOnClickListener(v -> onDelete.run());
    }
}
