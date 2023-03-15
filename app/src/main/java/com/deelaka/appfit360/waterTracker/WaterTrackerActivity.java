package com.deelaka.appfit360.waterTracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.deelaka.appfit360.R;
import com.deelaka.appfit360.ViewPagerFragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class WaterTrackerActivity extends AppCompatActivity {
    ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private TabLayout tabLayout;
    ViewPager2 viewPager2;
    String[] titles = new String[]{"Home","History"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_tracker);

        getSupportActionBar().hide();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this);

        viewPager2.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
    }
}