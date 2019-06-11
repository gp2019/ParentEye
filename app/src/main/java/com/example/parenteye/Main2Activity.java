package com.example.parenteye;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class Main2Activity extends AppCompatActivity {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem home;
    private TabItem notification;
    private TabItem friendRequest;
    private TabItem sideMenu;
    private ViewPager viewPager;
    PagerController mPagerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        tabLayout=findViewById(R.id.tablayout);
        home=findViewById(R.id.Home);
        notification=findViewById(R.id.notification);
        friendRequest=findViewById(R.id.friendRequests);
        sideMenu=findViewById(R.id.sideMenu);
        viewPager=findViewById(R.id.viewpager);
        mPagerController =new PagerController(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(mPagerController);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
