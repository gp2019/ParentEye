package com.example.parenteye;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class friends_fregment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Friendsref = database.getReference("Friends");
    DatabaseReference userref = database.getReference("Users");
    ArrayList<Users> curremtuserfriends=new ArrayList<Users>();
    private ListView friendsList;
    public static final String friendId="Friend_Id";
    public friends_fregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fregment_user_friends, container, false);




    }

}
