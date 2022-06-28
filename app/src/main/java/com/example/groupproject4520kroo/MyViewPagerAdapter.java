package com.example.groupproject4520kroo;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

@Keep
public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
            return new EventPreviewFragment();
            case 1:
                return new GroupPreviewFragment();
            default:
                return new EventPreviewFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
