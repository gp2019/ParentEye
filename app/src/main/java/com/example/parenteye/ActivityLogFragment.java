package com.example.parenteye;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ActivityLogFragment extends Fragment {

    private RecyclerView recyclerView;

    //public Notifications notifications =new Notifications() ;


    public static ActivityLogAdapter activityLogAdapter;


    public List<ActivityLog> logActivityList  = new ArrayList<>();


    public ActivityLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_activitylog, container, false);



        recyclerView = view.findViewById(R.id.Activity_log_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        setActivityLogList();


        return view;
    }




    public void setActivityLogList()
    {
        ActivityLog activityLog =new ActivityLog() ;

        activityLog.readLogs();

        activityLogAdapter = new ActivityLogAdapter(getContext(), activityLog.logActivityList);

        recyclerView.setAdapter(activityLogAdapter);
        //notifiList= notifications;


        // notificationAdapter.notifyDataSetChanged();


    }



    public List<ActivityLog> readLogs() {
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //FirebaseHelper firebaseHelper = new FirebaseHelper();


        //String userId = "EzH9MI0WJ7N0AwZNUidC45e2wiP2";


        DatabaseReference Activity_reference = database.getReference("activityLog").child(user.getUid());


        Activity_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logActivityList.clear();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ActivityLog activityLog = snapshot.getValue(ActivityLog.class);
                        logActivityList.add(activityLog);
                    }
                    Collections.reverse(logActivityList);
                    activityLogAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


            return  logActivityList;
    }
    }
