package com.zumper.sfeats.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zumper.sfeats.fragments.ListFragment;
import com.zumper.sfeats.fragments.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhermanu on 1/29/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    Fragment map, list;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(map == null)
                    map = new MapFragment();
                return map;
            case 1:
                if(list == null)
                    list = new ListFragment();
                return list ;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
