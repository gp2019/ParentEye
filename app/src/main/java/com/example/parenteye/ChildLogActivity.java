package com.example.parenteye;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class ChildLogActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postref = database.getReference("Posts");
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference CommunityRef = database.getReference("Community");
    DatabaseReference PostCommentsRef = database.getReference("CommentsPost");
    DatabaseReference ReactionPostsRef = database.getReference("ReactionPosts");
    DatabaseReference activityLogRef = database.getReference("ActivityLog");
    DatabaseReference FriendsRef = database.getReference("Friends");
    private StorageReference mStorageRef;
    final long ONE_MEGABYTE = 1024 * 1024;
    private ListView activityloglist;
    private ArrayList<Users> Activities=new ArrayList<Users>();
    private Button postlist;
    private Button commentlist;
    private Button friendlist;
    private Dialog myDialog;
    private TextView txtclose;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_log);
        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");


        postlist=(Button)findViewById(R.id.postlist);
        commentlist=(Button) findViewById(R.id.commentlist);
        friendlist=(Button) findViewById(R.id.friendlist);
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.logpopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        title=(TextView) myDialog.findViewById(R.id.title);
        activityloglist=(ListView) myDialog.findViewById(R.id.activitylogId);
        postlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        commentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                GetComments();
            }
        });
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                GetFriends();


            }
        });

    }

    private void GetChildActivityLog(){
        if(mAuth.getCurrentUser()!=null){
            Intent childIntent=getIntent();
            final String selectedchidid=childIntent.getStringExtra(MyChildrenActivity.child_Id);
            final String selectedChildname=childIntent.getStringExtra(MyChildrenActivity.Child_Name);
            Activities.clear();
            final ChildAdapter childrenAdapter=new ChildAdapter(ChildLogActivity.this,Activities);
            activityloglist.setAdapter(childrenAdapter);
            activityLogRef.child(selectedchidid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot logsnapshot:dataSnapshot.getChildren()){
                   ActivityLog log=logsnapshot.getValue(ActivityLog.class);
                   final Users child=new Users();
                   child.setUsername(selectedChildname+log.getActivityContent());
                   child.setUserId(log.getPostId());
                   userRef.child(selectedchidid).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                              child.setProfile_pic_id(dataSnapshot.getValue(Users.class).getProfile_pic_id());
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
    private void AddpostTochildLog(final String ChildId,String PostId){
        final ActivityLog activity=new ActivityLog();
        activity.setPostId(PostId);
        postref.child(PostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String stringId=dataSnapshot.getValue(Posts.class).getUserId();
                String placeId=dataSnapshot.getValue(Posts.class).getPlaceId();
                if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"1")){
                    activity.setActivityContent(" make new post on his profile ");
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"3")){
                    CommunityRef.child(placeId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final  String grouppostowner=dataSnapshot.getValue(Community.class).getCommunityname();
                            activity.setActivityContent(" make new post on "+ grouppostowner+ " group ");
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

        activityLogRef.child(ChildId).push().setValue(activity);
    }
    private void AddCommentToChildLog(final String ChildId, String PostId){
       final ActivityLog activity=new ActivityLog();
        activity.setPostId(PostId);
        postref.child(PostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String stringId=dataSnapshot.getValue(Posts.class).getUserId();
                String placeId=dataSnapshot.getValue(Posts.class).getPlaceId();
                if(TextUtils.equals(ChildId,stringId)){
                    activity.setActivityContent(" make new comment on his post");
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"1")&&!TextUtils.equals(ChildId,stringId)){
                    userRef.child(stringId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String upostownername=dataSnapshot.getValue(Users.class).getUsername();
                            activity.setActivityContent(" make new comment on post on "+ upostownername+" profile");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"2")&&!TextUtils.equals(ChildId,stringId)){
                    CommunityRef.child(placeId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String postPageowner=dataSnapshot.getValue(Community.class).getCommunityname();
                            activity.setActivityContent(" make new comment on post on "+ postPageowner+" page");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"3")&&!TextUtils.equals(ChildId,stringId)){
                    CommunityRef.child(placeId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          final  String grouppostowner=dataSnapshot.getValue(Community.class).getCommunityname();
                            userRef.child(stringId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String postowner=dataSnapshot.getValue(Users.class).getUsername();
                        activity.setActivityContent(" make new comment on "+ postowner+ " post on "+grouppostowner+" group");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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

        activityLogRef.child(ChildId).push().setValue(activity);
    }
    private void AddReactionOnPost(final String ChildId, String PostId){
        final ActivityLog activity=new ActivityLog();
        activity.setPostId(PostId);
        postref.child(PostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String stringId=dataSnapshot.getValue(Posts.class).getUserId();
                String placeId=dataSnapshot.getValue(Posts.class).getPlaceId();
                if(TextUtils.equals(ChildId,stringId)){
                    activity.setActivityContent(" Liked his post");
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"1")&&!TextUtils.equals(ChildId,stringId)){
                    userRef.child(stringId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String upostownername=dataSnapshot.getValue(Users.class).getUsername();
                            activity.setActivityContent(" Liked post on "+ upostownername+" profile");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"2")&&!TextUtils.equals(ChildId,stringId)){
                    CommunityRef.child(placeId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            String postPageowner=dataSnapshot.getValue(Community.class).getCommunityname();
                            activity.setActivityContent(" Liked post on "+ postPageowner+" page");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(TextUtils.equals(dataSnapshot.getValue(Posts.class).getPlaceTypeId(),"3")&&!TextUtils.equals(ChildId,stringId)){
                    CommunityRef.child(placeId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final  String grouppostowner=dataSnapshot.getValue(Community.class).getCommunityname();
                            userRef.child(stringId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String postowner=dataSnapshot.getValue(Users.class).getUsername();
                                    activity.setActivityContent(" Liked post on "+ postowner+ " post on "+grouppostowner+" group");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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

        activityLogRef.child(ChildId).push().setValue(activity);

    }




    private void GetComments(){
        if(mAuth.getCurrentUser()!=null) {
            Intent childIntent = getIntent();
            final String selectedchidid = childIntent.getStringExtra(MyChildrenActivity.child_Id);
            final String selectedChildname = childIntent.getStringExtra(MyChildrenActivity.Child_Name);
            title.setText(selectedChildname + " Comments");
            final ChildAdapter childrenAdapter=new ChildAdapter(ChildLogActivity.this,Activities);
            activityloglist.setAdapter(childrenAdapter);
            PostCommentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Activities.clear();
                    for(DataSnapshot childlogsnapshot:dataSnapshot.getChildren()){
                        if(TextUtils.equals(childlogsnapshot.getValue(PostComments.class).getUserId(),selectedchidid)){
                            final Users Actv=new Users();
                            Actv.setUsername(selectedChildname+" make new comment");
                            Actv.setRoleId(childlogsnapshot.getValue(PostComments.class).getCommentDate());



                            userRef.child(selectedchidid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                     Actv.setProfile_pic_id(dataSnapshot.getValue(Users.class).getProfile_pic_id());

                                            }
                                            Activities.add(Actv);
                                            childrenAdapter.notifyDataSetInvalidated();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } }
                    //Collections.reverse(Activities);

                    }





                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
    private void GetFriends(){
        if(mAuth.getCurrentUser()!=null) {
            Activities.clear();
            Intent childIntent = getIntent();
            final String selectedchidid = childIntent.getStringExtra(MyChildrenActivity.child_Id);
            final String selectedChildname = childIntent.getStringExtra(MyChildrenActivity.Child_Name);
            title.setText(selectedChildname + " Friends");
            final ChildAdapter childrenAdapter = new ChildAdapter(ChildLogActivity.this, Activities);
            activityloglist.setAdapter(childrenAdapter);
            final ArrayList<String> friendsList=new ArrayList<String>();
            FriendsRef.child(selectedchidid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(Friends.class)!=null) {
                        String myfriends = dataSnapshot.getValue(Friends.class).getUserFriends();
                        final String[] myFriendsID = myfriends.split(",");
                        for(String id:myFriendsID){
                            friendsList.add(id);
                        } }
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               for(DataSnapshot usersnapshot:dataSnapshot.getChildren()){
                                   if(friendsList.contains(usersnapshot.getKey())){
                                       final Users Actv=new Users();
                                       Actv.setUsername(usersnapshot.getValue(Users.class).getUsername());
                                       if(usersnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                     Actv.setProfile_pic_id(usersnapshot.getValue(Users.class).getProfile_pic_id());
                     System.out.println("pic is "+usersnapshot.getValue(Users.class).getProfile_pic_id());

                                       }
                                       Activities.add(Actv);
                                       childrenAdapter.notifyDataSetInvalidated();


                                   }

                               }
                               //Collections.reverse(Activities);
                              // childrenAdapter.notifyDataSetInvalidated();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

    }
}
