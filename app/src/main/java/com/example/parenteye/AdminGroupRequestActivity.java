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

public class AdminGroupRequestActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference CommunityRef = database.getReference("Community");
    DatabaseReference groupReqRef = database.getReference("GroupRequests");
    private ListView requestlists;
    private ArrayList<Users> request=new ArrayList<Users>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_group_request);
        Intialize_variables();



    }

    @Override
    protected void onStart() {
        super.onStart();
        GetAdminRequest();
    }

    private void Intialize_variables(){
        mAuth=FirebaseAuth.getInstance();
        requestlists=(ListView)findViewById(R.id.requestlists);
    }
    private void GetAdminRequest(){
        if(mAuth.getCurrentUser()!=null){
           final GroupRequestAdapter adapter=new GroupRequestAdapter(AdminGroupRequestActivity.this,request);
            requestlists.setAdapter(adapter);
            groupReqRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     request.clear();
                    adapter.notifyDataSetChanged();
                    for(DataSnapshot gpsnapshot:dataSnapshot.getChildren()){
                       final  GroupRequests group_request=gpsnapshot.getValue(GroupRequests.class);
                        CommunityRef.child(group_request.getGroupId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if(TextUtils.equals(dataSnapshot.getValue(Community.class).getAdminId(),mAuth.getCurrentUser().getUid())){
                     //  System.out.println("print now");
                                   final  Users user=new Users();
                                    user.setRoleId(dataSnapshot.getValue(Community.class).getCommunityname()); //this represent group name
                                    user.setLocation(dataSnapshot.getKey()); // this represent group Id
                       userRef.child(group_request.getUserid()).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               user.setUsername(dataSnapshot.getValue(Users.class).getUsername());
                               user.setUserId(group_request.getUserid());
                             if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                                 user.setProfile_pic_id(dataSnapshot.getValue(Users.class).getProfile_pic_id());
                             }
                             request.add(user);
                               adapter.notifyDataSetChanged();

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                  //  Collections.reverse(request);
                 //   adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
