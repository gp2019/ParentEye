package com.example.parenteye;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class ActivityLogFragment extends Fragment {

    private RecyclerView recyclerView;

    //public Notifications notifications =new Notifications() ;


    public static ActivityLogAdapter activityLogAdapter;


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


}
