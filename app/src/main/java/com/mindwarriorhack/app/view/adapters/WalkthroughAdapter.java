package com.mindwarriorhack.app.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class WalkthroughAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public WalkthroughAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragments=fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
