package com.deelaka.appfit360;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.deelaka.appfit360.waterTracker.History;
import com.deelaka.appfit360.waterTracker.Home;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"Home","History"};
    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return  new Home();
            case 1:
                return  new History();
        }
        return new Home();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
