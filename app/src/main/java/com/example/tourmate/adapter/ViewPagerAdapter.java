package com.example.tourmate.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.Navigation;

import com.example.tourmate.AddEventFrag;
import com.example.tourmate.EventListFragment;
import com.example.tourmate.R;
import com.google.android.material.tabs.TabLayout;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTab;
    View tabLayout;

    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context, int totalTab, View tabLayout) {
        super(fm);
        this.context = context;
        this.totalTab = totalTab;
        this.tabLayout = tabLayout;
    }




    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment  fragment = null;
        switch (position)
        {
            case 0:
                fragment = new EventListFragment();
                break;
            case 1:
                fragment = new AddEventFrag();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return totalTab;
    }
}
