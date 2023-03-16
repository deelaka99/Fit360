package com.deelaka.appfit360.foodTracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.deelaka.appfit360.R;
import com.deelaka.appfit360.ViewPagerFragmentAdapter1;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FoodTrackerActivity extends AppCompatActivity {
    ViewPagerFragmentAdapter1 viewPagerFragmentAdapter1;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    String[] titles = new String[]{"Home","History"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_tracker);

        //Formatting action bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
        getSupportActionBar().setTitle("Fit360 Food Tracker");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerFragmentAdapter1 = new ViewPagerFragmentAdapter1(this);

        viewPager2.setAdapter(viewPagerFragmentAdapter1);

        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
    }
}