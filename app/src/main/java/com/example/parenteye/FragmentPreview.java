package com.example.parenteye;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
landing activity for fregments
clever
 */
public class FragmentPreview extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.fragment_preview);
        super.onCreate(savedInstanceState);

        TextView freagmantText = findViewById(R.id.fregmant_text);

        ActivityLogFragment activityLogFragment = new ActivityLogFragment();

       // NotificationFragment notificationFragment = new NotificationFragment();

       //check if the the fragment is a notification or a activity log to open
        // and change the header text

        freagmantText.setText("Son's Activity Log");
//        if(mainActivity.isActivityLog)
//        {
//            freagmantText.setText("Son's Activity Log");
//
//        }else{
//            freagmantText.setText("Notifications");
//        }

        FragmentManager fragmentTransaction = getSupportFragmentManager();
        fragmentTransaction.beginTransaction().add(R.id.Activity_log_fragment_container, activityLogFragment).commit();


    }
}
