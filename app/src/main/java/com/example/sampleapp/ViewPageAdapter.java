package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
public class ViewPageAdapter extends FragmentStateAdapter {
    // Array to hold the list of fragments
    private final Fragment[] fragments = new Fragment[]{
            new menuone(),
            new menutwo(),
            new menuleader(),
    };

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Returns the total number of fragments/pages
    @Override
    public int getItemCount() {
        return fragments.length;
    }

    // Returns the fragment for the given position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }
}
