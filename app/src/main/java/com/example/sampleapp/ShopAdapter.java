package com.example.sampleapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleapp.Enums.PowerUpList;
import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.Managers.DataManager;
import com.example.sampleapp.Managers.PowerUpManager;
import com.example.sampleapp.Managers.SoundManager;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private final PowerUpList[] powerUps = PowerUpList.values();
    private ActionCallback actionCallback;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_powerup, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        PowerUpList item = powerUps[position];
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.buyButton.setText(item.getPrice() + " GP");
        holder.icon.setImageResource(item.getIconRes());

        int playerCoins = DataManager.getInstance().getInt("value", 0);

        boolean canAfford = playerCoins >= item.getPrice();

        if (!canAfford || PowerUpManager.getInstance().hasPowerUp(item.getName())) {
            holder.buyButton.setEnabled(false);
            holder.buyButton.setAlpha(0.5f); // Make it semi-transparent
        } else {
            holder.buyButton.setEnabled(true);
            holder.buyButton.setAlpha(1.0f);
        }

        holder.buyButton.setOnClickListener(v -> {
            // Handle purchase logic here
            SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);

            int coin = DataManager.getInstance().getInt("value", 0);
            // Double check logic: ensure they still have enough money
            if (coin >= item.getPrice()) {
                coin -= item.getPrice();
                DataManager.getInstance().saveData(context, "value", coin);
                PowerUpManager.getInstance().addPowerUp(item);

                // 1. Notify the Fragment/Activity to update the Top Bar currency
                if (actionCallback != null) {
                    actionCallback.onPurchaseSuccess();
                }

                // 2. This forces every button to re-check 'canAfford' with the new coin balance
                notifyDataSetChanged();
            }

        });
    }

    // At the caret position:
    public interface ActionCallback {
        void onPurchaseSuccess();
    }

    public void setActionCallback(ActionCallback callback) {
        this.actionCallback = callback;
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
