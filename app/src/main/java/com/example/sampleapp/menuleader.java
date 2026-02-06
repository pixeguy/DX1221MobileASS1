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

public class menuleader  extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menuleader, container, false);

        TextView nameTxt = rootView.findViewById(R.id.leaderboardText); // NAME column
        TextView timeTxt = rootView.findViewById(R.id.timeText);        // TIME column
        TextView lootTxt = rootView.findViewById(R.id.lootText);        // LOOT column

        List<PlayerRecordEntry> entries = SaveManager.getInstance().loadScoresJson(requireContext());
        SaveManager.getInstance().sortBestTimeToSlowest(entries);

        if (entries == null || entries.isEmpty()) {
            nameTxt.setText("No scores yet!");
            timeTxt.setText("");
            lootTxt.setText("");
            return rootView;
        }

        // Example sort (choose ONE):
        // Collections.sort(entries, (a, b) -> Integer.compare(b.lootValue, a.lootValue)); // highest loot first
        // Collections.sort(entries, (a, b) -> Integer.compare(a.score, b.score));        // fastest time first (if score=time)
        // Collections.sort(entries, (a, b) -> Integer.compare(b.score, a.score));        // highest score first

        StringBuilder names = new StringBuilder();
        StringBuilder times = new StringBuilder();
        StringBuilder loots = new StringBuilder();

        // Headers
        names.append("NAME\n");
        times.append("TIME\n");
        loots.append("LOOT\n");

        names.append("______\n");
        times.append("______\n");
        loots.append("______\n");

        int rank = 1;
        for (PlayerRecordEntry e : entries) {
            // 1. Handle Name (Column 1)
            String name = (e.name == null || e.name.trim().isEmpty()) ? "-" : e.name.trim();
            names.append(rank).append(". ").append(name).append("\n");

            // 2. Handle Time (Column 2)
            // Using your FormatTime helper to convert seconds/score to mm:ss
            String timeStr = FormatTime(e.score);
            times.append(timeStr).append("\n");

            // 3. Handle Loot (Column 3)
            loots.append(e.lootValue).append("\n");

            rank++;

            // Optional: Limit to top 10 to prevent overlapping the UI board
            if (rank > 10) break;
        }


        nameTxt.setText(names.toString());
        timeTxt.setText(times.toString());
        lootTxt.setText(loots.toString());

        return rootView;
    }

    // Helper: format seconds into mm:ss (if your time is already a string, skip this)
    private String FormatTime(float secondsFloat) {
        int totalSeconds = (int)Math.floor(secondsFloat);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
