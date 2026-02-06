package com.example.sampleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleapp.Enums.PowerUpList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private final PowerUpList[] powerUps = PowerUpList.values();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_powerup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PowerUpList item = powerUps[position];
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.buyButton.setText(item.getPrice() + " GP");
        holder.icon.setImageResource(item.getIconRes());

        holder.buyButton.setOnClickListener(v -> {
            // Handle purchase logic here
        });
    }

    @Override
    public int getItemCount() { return powerUps.length; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description;
        Button buyButton;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
            description = itemView.findViewById(R.id.tv_item_description);
            buyButton = itemView.findViewById(R.id.btn_buy);
            icon = itemView.findViewById(R.id.img_item_icon);
        }
    }
}
