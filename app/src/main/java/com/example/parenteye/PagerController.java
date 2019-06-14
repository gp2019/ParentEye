package com.example.parenteye;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int tabCounts;
    public PagerController(FragmentManager fm,int tabCounts) {
        super(fm);
        this.tabCounts=tabCounts;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Home();
            case 1:
                return new NotificationFragment();
           /* case 2:
                return new ActivityLogFragment();
                */
            case 2:
                return new friends_fregment();
                default:
                    return null;


        }

    }

    @Override
    public int getCount() {
        return tabCounts;
    }
}
