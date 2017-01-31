package com.zumper.sfeats.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zumper.sfeats.R;
import com.zumper.sfeats.adapters.ViewPagerAdapter;
import com.zumper.sfeats.fragments.ListFragment;
import com.zumper.sfeats.fragments.MapFragment;

public class MainActivity extends AppCompatActivity{

    public static String EXTRA_DATA = "com.extra.data";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String tabTittleMain = "Map";
        final String tabTittleBook = "List";

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MapFragment(), tabTittleMain);
        viewPagerAdapter.addFragment(new ListFragment(), tabTittleBook);
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
