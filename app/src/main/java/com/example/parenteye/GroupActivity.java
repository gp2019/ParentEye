package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference CommunityRef = database.getReference("Community");
    DatabaseReference membersRef = database.getReference("Members");
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference postref = database.getReference("Posts");
    DatabaseReference groupReqRef = database.getReference("GroupRequests");
    private TextView groupname;
    private ImageView groupCover;
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference groupphotoRef;
    private Button join_unjoin;
    private int IsExist=0;
    ArrayList<custom_posts_returned> group_posts=new ArrayList<custom_posts_returned>();
    private ListView goup_Post_listview;
    public static final String pageID="pageID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intialize_variables();
        join_unjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Is Exist"+IsExist);
                final String groupId="-Lg1Gggi2Xo1Qx2-rLG4"; //will be get automatic later
                if (mAuth.getCurrentUser() != null) {
                    if (IsExist == 0) {
                        GroupRequests request=new GroupRequests();
                        request.setUserid(mAuth.getCurrentUser().getUid());
                        request.setGroupId(groupId);
                        groupReqRef.push().setValue(request);
                        IsExist=2;
                        join_unjoin.setText("cancel request");


                    }
                    else if (IsExist == 1) {
                        membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot membersnapshot : dataSnapshot.getChildren()) {
                                    Members member = membersnapshot.getValue(Members.class);
                                    if (TextUtils.equals(mAuth.getCurrentUser().getUid(), member.getUserId()) && TextUtils.equals(member.getTyptId(), "1") && TextUtils.equals(member.getCommunityid(), groupId)) {
                                        membersRef.child(membersnapshot.getKey()).removeValue();
                                        join_unjoin.setText("join Group");
                                        IsExist=0;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else if(IsExist == 2){
                      //  IsExist=0;
                        groupReqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot gpreqsnapshot : dataSnapshot.getChildren()) {
                                    GroupRequests group_req = gpreqsnapshot.getValue(GroupRequests.class);
                  if (TextUtils.equals(mAuth.getCurrentUser().getUid(), group_req.getUserid()) && TextUtils.equals(group_req.getGroupId(), groupId)) {
                                        groupReqRef.child(gpreqsnapshot.getKey()).removeValue();
                                        join_unjoin.setText("join Group");
                                        IsExist=0;
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
        });
        groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String groupId="-Lg1Gggi2Xo1Qx2-rLG4"; //will be get automatic later
                Intent aboutIntent=new Intent(getApplicationContext(),CommunityAboutandmembersActivity.class);
                aboutIntent.putExtra(pageID,groupId);
                startActivity(aboutIntent);
            }
        });

    }
    private void Intialize_variables(){
        mAuth=FirebaseAuth.getInstance();
        groupname=(TextView)findViewById(R.id.group_name);
        groupCover=(ImageView) findViewById(R.id.group_cover);
        groupphotoRef = FirebaseStorage.getInstance().getReference("GroupImages/");
        join_unjoin=(Button)findViewById(R.id.join_unjoinbtn);
        goup_Post_listview=(ListView)findViewById(R.id.groupList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getgroupInfo();
    }

    public  void getgroupInfo(){
        if(mAuth.getCurrentUser()!=null){
            final String groupId="-Lg1Gggi2Xo1Qx2-rLG4"; //will be get automatic later
            CommunityRef.child(groupId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Community group=dataSnapshot.getValue(Community.class);
                    groupname.setText(group.getCommunityname());
                    if(group.getCoverPhotoId()!=null){
                        groupphotoRef.child(group.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                groupCover.setImageBitmap(bm);

                            }
                        });

                    }
                    if(TextUtils.equals(group.getAdminId(),mAuth.getCurrentUser().getUid())){

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            membersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot membersnapshot:dataSnapshot.getChildren()){
                        Members member=membersnapshot.getValue(Members.class);
                        if(TextUtils.equals(mAuth.getCurrentUser().getUid(),member.getUserId())&&TextUtils.equals(member.getTyptId(),"1")&&TextUtils.equals(member.getCommunityid(),groupId)){
                            join_unjoin.setText("Unjoin group");
                            IsExist=1;

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            groupReqRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot reqsnapshot:dataSnapshot.getChildren()){
                        GroupRequests req=reqsnapshot.getValue(GroupRequests.class);
                        if(TextUtils.equals(req.getUserid(),mAuth.getCurrentUser().getUid())&&TextUtils.equals(req.getGroupId(),req.getGroupId())){
                            join_unjoin.setText("cancel request");
                            IsExist=2;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

           // GetPagePosts();
        }
    }


}
