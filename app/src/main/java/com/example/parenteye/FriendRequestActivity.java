package com.example.parenteye;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FriendRequestActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference friendrequestRef = database.getReference("FriendRequest");
    ArrayList<Users> request=new ArrayList<Users>();
    private ListView friendrequest_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        mAuth = FirebaseAuth.getInstance();
        friendrequest_list=(ListView)findViewById(R.id.friendrequest_list);
    }

    @Override
    protected void onStart() {
        GetMyFriendRequest();
        super.onStart();
    }

    private void GetMyFriendRequest(){
        final FriendRequestAdapter friendrequestadapter=new FriendRequestAdapter(FriendRequestActivity.this,request);
        friendrequest_list.setAdapter(friendrequestadapter);

        if(mAuth.getCurrentUser()!=null){
            friendrequestRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    request.clear();
                    for(DataSnapshot friendreqsnapshot:dataSnapshot.getChildren()){
                        if(TextUtils.equals(friendreqsnapshot.getValue(FriendRequest.class).getRecieverid(),mAuth.getCurrentUser().getUid())){
                            String senderId=friendreqsnapshot.getValue(FriendRequest.class).getSenderid();
                           Users user=new Users();
                           user.setUsername(senderId);
                           request.add(user);
                        }
                    }
                    Collections.reverse(request);
                    friendrequestadapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
