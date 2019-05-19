package com.example.parenteye;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
    private ArrayList<Users> children=new ArrayList<Users>();
    private ListView childrenList;
    public static final String child_Id="ChildId";
    public static final String Child_Name="CHildName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_children);
        mAuth = FirebaseAuth.getInstance();
        childrenList=(ListView) findViewById(R.id.childrenList);



        childrenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users child=children.get(position);
                Intent childIntent=new Intent(getApplicationContext(),ChildLogActivity.class);
                childIntent.putExtra(child_Id,child.getUserId());
                childIntent.putExtra(Child_Name,child.getUsername());
                startActivity(childIntent);
            }
        });

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
                                            Users mychild = new Users();
                                            mychild.setUserId(usersnapshot.getKey());
                                            mychild.setUsername(user.getUsername());
                                            if (user.getProfile_pic_id() != null) {
                                                mychild.setProfile_pic_id(user.getProfile_pic_id());
                                            }
                                            children.add(mychild);

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
