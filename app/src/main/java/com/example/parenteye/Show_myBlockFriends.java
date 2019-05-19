package com.example.parenteye;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Show_myBlockFriends extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    ArrayList<String> friends;
    private DatabaseReference dbRef;
    private ImageView arrow_back;
    private RecyclerView recyclerView;
    private TextView nameofactionbar,noFriends;
    private ArrayAdapterForShowMyBlockFriends mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_show_did_like, container, false);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        progressBar = view.findViewById( R.id.progressBar );
        progressBar.setVisibility( View.GONE );
        arrow_back=view.findViewById(R.id.arrow_back);
        recyclerView=view.findViewById( R.id.User_recyclelistview);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        nameofactionbar=view.findViewById(R.id.create_post);
        nameofactionbar.setText("My Friends");
        noFriends=view.findViewById(R.id.noFriends);
        noFriends.setVisibility(View.GONE);
        arrow_back.setOnClickListener(this);
        new getMyBlockFriends().execute();


        return view;

    }


    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.arrow_back:
                //put the activity
                break;
        }
    }



    class getMyBlockFriends extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility( View.VISIBLE );

        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            friends = new ArrayList<String>();

            mAuth = FirebaseAuth.getInstance();
            dbRef = FirebaseDatabase.getInstance().getReference().child( "Friends" );
            dbRef.child(mAuth.getCurrentUser().getUid()).child("blockFriends").addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    friends.clear();
                    if(dataSnapshot.getValue(String.class)!=null&&!dataSnapshot.getValue(String.class).isEmpty()) {
                        String myBlockfriends = dataSnapshot.getValue(String.class);
                        final String[] myFriendsID = myBlockfriends.split(",");
                        for(String id:myFriendsID){
                            friends.add(id);
                        }
                    }

                    if (friends.size()==0) {
                        progressBar.setVisibility( View.GONE );
                        noFriends.setText("No Found Friends Block");
                        noFriends.setVisibility(View.VISIBLE);
                    }
                    else {
                        noFriends.setVisibility(View.GONE);
                        progressBar.setVisibility( View.GONE );
                        if (recyclerView.getAdapter() == null) {
                            mAdapter = new ArrayAdapterForShowMyBlockFriends( getContext(), friends );
                            recyclerView.setAdapter(mAdapter);

                        } else {
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

            return friends;
        }

    }
}
