package com.example.sampleapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sampleapp.Entity.Player.PlayerObj;

public class menutwo extends Fragment implements View.OnClickListener {
    public int tempStrength = 0;

    public void menutwo()
    {
        tempStrength = 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menutwo, container, false);

        //setting total player value count text
        TextView valueCountText = rootView.findViewById(R.id.totalValueCount);
        if( PlayerObj.getInstance() != null){
            String valueCount = Integer.toString(PlayerObj.instance.value);
            if(valueCount != null) {
                valueCountText.setText("Current XP : " + valueCount);
            }
        }

        //setting up buttons
        Button StrengthButton = rootView.findViewById(R.id.StrengthBtn);
        if (StrengthButton != null){
            StrengthButton.setOnClickListener(this);
        }

        Button ConfirmButton = rootView.findViewById(R.id.ConfirmBtn);
        if(ConfirmButton != null){
            ConfirmButton.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.StrengthBtn) {
            tempStrength++;
        } else if (id == R.id.ConfirmBtn) {
            PlayerObj.getInstance().strength += tempStrength;
            tempStrength = 0;
            System.out.println(PlayerObj.getInstance().strength += tempStrength);
        }
    }
}