package com.example.sampleapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleapp.Entity.Player.PlayerObj;
import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.Managers.DataManager;
import com.example.sampleapp.Managers.SaveManager;
import com.example.sampleapp.Managers.SoundManager;

public class menutwo extends Fragment implements View.OnClickListener {

    View rootView;

    // ======== Strength ========
    public int tempStrength = 0;
    public int showStrength = 0;
    public int strengthCost = 0;
    private TextView strengthValText;
    private TextView strengthCostText;

    // ======== Defence ========
    public int tempDefence = 0;
    public int showDefence = 0;
    public int defenceCost = 0;
    private TextView defenceValText;
    private TextView defenceCostText;

    // ======== Speed ========
    public int tempSpeed = 0;
    public int showSpeed = 0;
    public int speedCost = 0;
    private TextView speedValText;
    private TextView speedCostText;

    // ======== XP ========
    private TextView xpText;
    private int tempXp;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_menutwo, container, false);

        // ========== UI References ==========
        xpText = rootView.findViewById(R.id.tv_currency_amount);

        strengthValText = rootView.findViewById(R.id.StrengthText);
        strengthCostText = rootView.findViewById(R.id.StrengthCost);

        defenceValText = rootView.findViewById(R.id.DefenceText);
        defenceCostText = rootView.findViewById(R.id.DefenceCost);

        speedValText = rootView.findViewById(R.id.SpeedText);
        speedCostText = rootView.findViewById(R.id.SpeedCost);

        int xp = DataManager.getInstance().getInt("value", 0);

        // ========== Update UI ==========
        xpText.setText("" + xp);

        strengthValText.setText("Strength : " + PlayerObj.getInstance().strength + " + " + tempStrength);
        strengthCostText.setText("Cost : " + strengthCost);

        defenceValText.setText("Defence : " + PlayerObj.getInstance().defence + " + " + tempDefence);
        defenceCostText.setText("Cost : " + defenceCost);

        speedValText.setText("Speed : " + PlayerObj.getInstance().speed + " + " + tempSpeed);
        speedCostText.setText("Cost : " + speedCost);

        // ========== Buttons ==========
        rootView.findViewById(R.id.StrengthBtn).setOnClickListener(this);
        rootView.findViewById(R.id.DefenceBtn).setOnClickListener(this);
        rootView.findViewById(R.id.SpeedBtn).setOnClickListener(this);
        rootView.findViewById(R.id.ConfirmBtn).setOnClickListener(this);
        rootView.findViewById(R.id.ResetBtn).setOnClickListener(this);

        // ========== Initial Values ==========
        tempXp = xp;

        // Strength
        tempStrength = 0;
        showStrength = PlayerObj.getInstance().strength;
        strengthCost = showStrength * 15;

        // Defence
        tempDefence = 0;
        showDefence = PlayerObj.getInstance().defence;
        defenceCost = showDefence * 15;

        // Speed
        tempSpeed = 0;
        showSpeed = PlayerObj.getInstance().speed;
        speedCost = showSpeed * 15;

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        PlayerObj player = PlayerObj.getInstance();
        SoundManager.getInstance().PlayAudio(SoundList.Button_Click, 1.0f, 0.9f, 0.8f);

        // ==================== Strength Upgrade ====================
        if (id == R.id.StrengthBtn) {

            if (tempXp - strengthCost >= 0) {
                tempStrength++;
                showStrength = player.strength + tempStrength;

                tempXp -= strengthCost;
                strengthCost = showStrength * 15;

                strengthValText.setText("Strength : " + player.strength + " + " + tempStrength);
                strengthCostText.setText("Cost : " + strengthCost);
                xpText.setText("" + tempXp);
            }

        }
        // ==================== Defence Upgrade ====================
        else if (id == R.id.DefenceBtn) {

            if (tempXp - defenceCost >= 0) {
                tempDefence++;
                showDefence = player.defence + tempDefence;

                tempXp -= defenceCost;
                defenceCost = showDefence * 15;

                defenceValText.setText("Defence : " + player.defence + " + " + tempDefence);
                defenceCostText.setText("Cost : " + defenceCost);
                xpText.setText("" + tempXp);
            }

        }
        // ==================== Speed Upgrade ====================
        else if (id == R.id.SpeedBtn) {

            if (tempXp - speedCost >= 0) {
                tempSpeed++;
                showSpeed = player.speed + tempSpeed;

                tempXp -= speedCost;
                speedCost = showSpeed * 15;

                speedValText.setText("Speed : " + player.speed + " + " + tempSpeed);
                speedCostText.setText("Cost : " + speedCost);
                xpText.setText("" + tempXp);
            }

        }
        // ==================== Confirm Button ====================
        else if (id == R.id.ConfirmBtn) {

            // Apply ALL upgrades
            player.value = tempXp;
            player.strength += tempStrength;
            player.defence += tempDefence;
            player.speed += tempSpeed;

            DataManager.getInstance().saveData(requireContext(), "value", tempXp);
            DataManager.getInstance().saveData(requireContext(), "strength", player.strength);
            DataManager.getInstance().saveData(requireContext(), "defence", player.defence);
            DataManager.getInstance().saveData(requireContext(), "speed", player.speed);


            // Reset everything
            tempStrength = 0;
            tempDefence = 0;
            tempSpeed = 0;

            showStrength = player.strength;
            showDefence = player.defence;
            showSpeed = player.speed;

            strengthCost = showStrength * 15;
            defenceCost = showDefence * 15;
            speedCost = showSpeed * 15;

            // UI Refresh
            strengthValText.setText("Strength : " + player.strength + " + 0");
            strengthCostText.setText("Cost : " + strengthCost);

            defenceValText.setText("Defence : " + player.defence + " + 0");
            defenceCostText.setText("Cost : " + defenceCost);

            speedValText.setText("Speed : " + player.speed + " + 0");
            speedCostText.setText("Cost : " + speedCost);
        }
        else if (id == R.id.ResetBtn) {

            // Reset temporary upgrade values
            tempStrength = 0;
            tempDefence = 0;
            tempSpeed = 0;

            // Reset displayed values
            showStrength = player.strength;
            showDefence = player.defence;
            showSpeed = player.speed;

            // Reset XP
            tempXp = DataManager.getInstance().getInt("value", 0);

            // Reset costs
            strengthCost = showStrength * 15;
            defenceCost = showDefence * 15;
            speedCost = showSpeed * 15;

            // Update UI
            strengthValText.setText("Strength : " + player.strength + " + 0");
            strengthCostText.setText("Cost : " + strengthCost);

            defenceValText.setText("Defence : " + player.defence + " + 0");
            defenceCostText.setText("Cost : " + defenceCost);

            speedValText.setText("Speed : " + player.speed + " + 0");
            speedCostText.setText("Cost : " + speedCost);

            xpText.setText("" + tempXp);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void startCurrencySync() {
        if (xpText != null) {
            int currentVal = DataManager.getInstance().getInt("value", 0);
            // Only update if text is different to prevent layout flickering
            String valString = String.valueOf(currentVal);
            if (!xpText.getText().toString().equals(valString)) {
                xpText.setText(valString);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        startCurrencySync();
    }

}