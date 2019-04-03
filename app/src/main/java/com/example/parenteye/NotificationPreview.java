package com.example.parenteye;

import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class NotificationPreview extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.notification_preview);
        super.onCreate(savedInstanceState);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        NotificationFragment notificationFragment = new NotificationFragment();
        fragmentTransaction.add(R.id.notification_fragment_container, notificationFragment);
        fragmentTransaction.commit();

        if (notificationFragment.HAVE_NOTIFICATIONS == true) {

        } else {
            //Toast.makeText(NotificationPreview.this,"you have no notifications yet",Toast.LENGTH_LONG).show();


        }

    }
}
