package com.example.parenteye;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

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
    private ArrayList<Users> children=new ArrayList<Users>();
    private ArrayList<String> childrenId=new ArrayList<String>();
    private ListView childrenList;
    ProgressBar progressBar;
    private ChildAdapter childrenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children);
        mAuth = FirebaseAuth.getInstance();
        childrenList= findViewById(R.id.childrenList);
        progressBar = findViewById( R.id.progressBar );


    }

    @Override
    protected void onStart() {
        ReturnAllChildren();
        super.onStart();
    }

    private void ReturnAllChildren() {
        if (mAuth.getCurrentUser()!=null){
            final String currentuserId =mAuth.getCurrentUser().getUid();
            parentChildrenRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot parentchildsnapshot : dataSnapshot.getChildren()) {
                        ParentChildren parentchild = parentchildsnapshot.getValue(ParentChildren.class);
                        if (TextUtils.equals(parentchild.getParentId(), currentuserId)) {
                            childrenId.add( parentchild.getChildId() );
                        }
                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    children.clear();
                                    for (String childId : childrenId) {
                                        for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                                            Users user = usersnapshot.getValue(Users.class);
                                            if (TextUtils.equals(usersnapshot.getKey(), childId)) {
                                                Users mychild = new Users();
                                                mychild.setUserId(usersnapshot.getKey());
                                                mychild.setUsername(user.getUsername());
                                                if (user.getProfile_pic_id() != null) {
                                                    mychild.setProfile_pic_id(user.getProfile_pic_id());
                                                }
                                                children.add(mychild);

                                            }
                                        }
                                    }
                                    if (children.size()==0) {
                                        progressBar.setVisibility( View.GONE );
                                    }
                                    else {
                                        progressBar.setVisibility( View.GONE );
                                        if (childrenList.getAdapter() == null) {
                                            childrenAdapter = new ChildAdapter( MyChildrenActivity.this, children );
                                            childrenList.setAdapter(childrenAdapter);

                                        } else {

                                            childrenAdapter.notifyDataSetChanged();
                                        }
                                    }

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
    }
}
