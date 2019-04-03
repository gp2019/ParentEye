package com.example.parenteye;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class MyChildrenActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference parentChildrenRef = database.getReference("ParentChildren");
    DatabaseReference userRef = database.getReference("Users");
    private ArrayList<custom_posts_returned> children=new ArrayList<custom_posts_returned>();
    private ListView childrenList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children);
        mAuth = FirebaseAuth.getInstance();
        childrenList=(ListView) findViewById(R.id.childrenList);
 //System.out.println("on create "+mAuth.getCurrentUser().getUid());
       // mAuth.getCurrentUser();


    }

    @Override
    protected void onStart() {
        ReturnAllChildren();
        super.onStart();
    }

    private void ReturnAllChildren() {
        if (mAuth.getCurrentUser()!=null){
           // System.out.println("in my function  "+mAuth.getCurrentUser().getUid());
            final String currentuserId =mAuth.getCurrentUser().getUid();
            final ChildAdapter childrenAdapter=new ChildAdapter(MyChildrenActivity.this,children);
            childrenList.setAdapter(childrenAdapter);
            parentChildrenRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    children.clear();
                    for (DataSnapshot parentchildsnapshot : dataSnapshot.getChildren()) {
                        ParentChildren parentchild = parentchildsnapshot.getValue(ParentChildren.class);
                        if (TextUtils.equals(parentchild.getParentId(), currentuserId)) {
                            final String childId = parentchild.getChildId();
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                                        Users user = usersnapshot.getValue(Users.class);
                                        if (TextUtils.equals(usersnapshot.getKey(), childId)) {
                                            custom_posts_returned custom = new custom_posts_returned();
                                            custom.setPost_owner_name(user.getUsername());
                                            if (user.getProfile_pic_id() != null) {
                                                custom.setpost_owner_ID(user.getProfile_pic_id());
                                            }
                                            children.add(custom);

                                        }
                                    }
                                    //  Collections.reverse(children);
                                    childrenAdapter.notifyDataSetChanged();
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}