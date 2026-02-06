package com.example.sampleapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sampleapp.Managers.DataManager;

public class menushop extends Fragment implements View.OnClickListener {
    View rootView;
    ShopAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menushop, container, false);
        Log.d("FRAGMENT", "Host = " + getActivity().getClass().getSimpleName());

        // Update UI
        TextView currencyText = rootView.findViewById(R.id.tv_currency_amount);
        currencyText.setText("" + DataManager.getInstance().getInt("value", 0));

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rv_shop_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ShopAdapter();
        recyclerView.setAdapter(adapter);

        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        adapter.setActionCallback(() -> {
            // Update UI
            TextView currencyText = rootView.findViewById(R.id.tv_currency_amount);
            currencyText.setText("" + DataManager.getInstance().getInt("value", 0));
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void startCurrencySync() {
        // 1. Update the Currency Text
        TextView currencyText = rootView.findViewById(R.id.tv_currency_amount);
        if (currencyText != null) {
            int currentVal = DataManager.getInstance().getInt("value", 0);
            // Only update if text is different to prevent layout flickering
            String valString = String.valueOf(currentVal);
            if (!currencyText.getText().toString().equals(valString)) {
                currencyText.setText(valString);

                // 2. IMPORTANT: If the money changed, tell the adapter to
                // re-check what buttons the player can afford
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        startCurrencySync();
    }

    @Override
    public void onClick(View v) {

    }
}
