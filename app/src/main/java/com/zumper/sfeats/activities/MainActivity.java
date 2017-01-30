package com.zumper.sfeats.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zumper.sfeats.R;
import com.zumper.sfeats.adapters.ViewPagerAdapter;
import com.zumper.sfeats.fragments.ListFragment;
import com.zumper.sfeats.fragments.MapFragment;
import com.zumper.sfeats.interfaces.GooglePlacesAPI;

public class MainActivity extends AppCompatActivity{

    public static String EXTRA_DATA = "com.extra.data";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private GooglePlacesAPI googlePlacesAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        final String tabTittleMain = "Map";
        final String tabTittleBook = "List";
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MapFragment(), tabTittleMain);
        viewPagerAdapter.addFragment(new ListFragment(), tabTittleBook);
        viewPager.setAdapter(viewPagerAdapter);
    }




}
