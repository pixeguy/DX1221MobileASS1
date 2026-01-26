package com.example.sampleapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sampleapp.Entity.Player.PlayerRecordEntry;
import com.example.sampleapp.Managers.SaveManager;

import java.util.Collections;
import java.util.List;

public class menuleader  extends Fragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_menuleader, container, false);
        Log.d("FRAGMENT", "Host = " + getActivity().getClass().getSimpleName());
        TextView txt = rootView.findViewById(R.id.leaderboardText);
        PlayerRecordEntry entry = new PlayerRecordEntry();
        entry.score = 50;
        SaveManager.getInstance().addScoreAndSave(requireContext(),entry);
        List<PlayerRecordEntry> entries = SaveManager.getInstance().loadScoresJson(requireContext());

        if (entries == null || entries.isEmpty()) {
            txt.setText("No scores yet!");
            return rootView;
        }

        // (Optional) sort highest score first
        Collections.sort(entries, (a, b) -> Integer.compare(b.score, a.score));

        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (PlayerRecordEntry e : entries) {
            sb.append(rank).append(". ")
                    .append(" - ")
                    .append(e.score)
                    .append("\n");
            rank++;
        }

        txt.setText(sb.toString());
        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
