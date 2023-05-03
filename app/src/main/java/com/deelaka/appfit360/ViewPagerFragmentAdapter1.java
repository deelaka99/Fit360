package com.deelaka.appfit360;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.deelaka.appfit360.foodTracker.FoodHistory;
import com.deelaka.appfit360.foodTracker.FoodHome;
import com.deelaka.appfit360.waterTracker.Home;

public class ViewPagerFragmentAdapter1 extends FragmentStateAdapter {

    private final String[] titles = new String[]{"Home","History"};
    public ViewPagerFragmentAdapter1(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return  new FoodHome();
            case 1:
                return  new FoodHistory();
        }
        return new Home();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
