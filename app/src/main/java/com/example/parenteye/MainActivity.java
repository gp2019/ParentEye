package com.example.parenteye;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

//import android.support.v7.app.AlertController.RecycleListView;
//import android.support.v7.app.AlertController;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private StorageReference mStorageRef2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference myRef2 = database.getReference("Posts");
    DatabaseReference myRef3 = database.getReference("Friends");
    DatabaseReference memberRef = database.getReference("Members");
    private Integer userCompleteProfile = 0;
    private Button logout, show_comment;
    private ListView Post_listview;
    private String friendlist;
    private ArrayList<String> friendspostskeys = new ArrayList<String>();
    private ArrayList<Posts> Postss = new ArrayList<Posts>();
    final long ONE_MEGABYTE = 1024 * 1024;
    private Button makePage;
    private FloatingActionButton floatingActionButton;
    private Button goprofile;
    private Button makeGroup;
    private Button search;
    private Button addchild;
    private Button mychildren;
    private Button viewMyFriends;
    private Button view_page;
    private Button view_group;
    private Button showGroupRequest;
    //design

     Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem home;
    private TabItem notification;
    private TabItem friendRequest;
    private TabItem sideMenu;
    private ViewPager viewPager;

    private ImageView firendrequestid;
    private ArrayList<Posts> myposts=new ArrayList<Posts>();


    Notifications notifi = new Notifications();
    ActivityLog activityLog = new ActivityLog();
    private ImageButton notification_icon,likeNotify,disLikeNotifi,addFriendNotifi,removeFrindNotify;
    private Button ActivitylogBtn;

    private Button commentNotify;



    public static boolean isActivityLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //////////////////////////////design
        tabLayout=findViewById(R.id.tablayout);
        home=findViewById(R.id.Home);
        notification=findViewById(R.id.notification);
        friendRequest=findViewById(R.id.friendRequests);
        sideMenu=findViewById(R.id.sideMenu);
        viewPager=findViewById(R.id.viewpager);


        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
        mStorageRef2 = FirebaseStorage.getInstance().getReference("PostsImages/");
        goprofile = (Button) findViewById(R.id.goprofile);
        firendrequestid=(ImageView) findViewById(R.id.firendrequestid);
        viewMyFriends=(Button)findViewById(R.id.viewFriends);
        Post_listview=(ListView) findViewById(R.id.Post_listview);

        floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    CreatePost(currentUser.getUid());
                }

            }
        });

        show_comment = findViewById(R.id.ShowComments);
        show_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                CreateComment(currentUser.getUid());
            }
        });

        makePage = (Button) findViewById(R.id.makepage);
        logout = (Button) findViewById(R.id.logout);
        makeGroup = (Button) findViewById(R.id.makeGroup);
        search = (Button) findViewById(R.id.tvSearchBar);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    main_login();

                }
            }
        });
        makePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makepage = new Intent(MainActivity.this, AddPageActivity.class);
                startActivity(makepage);
                //finish();
            }
        });
        makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makeGroup = new Intent(MainActivity.this, MakeGroupActivity.class);
                startActivity(makeGroup);
                //finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(search);
                //finish();
            }
        });

        goprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAccount();
            }
        });
        addchild = (Button) findViewById(R.id.addchild);
        addchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addchild = new Intent(MainActivity.this, AddChildActivity.class);
                startActivity(addchild);
                // finish();
            }
        });
        mychildren = (Button) findViewById(R.id.mychildren);
        mychildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mychildren = new Intent(MainActivity.this, MyChildrenActivity.class);
                startActivity(mychildren);
                finish();
            }
        });


        firendrequestid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Intent friendrequest=new Intent(MainActivity.this,FriendRequestActivity.class);
         startActivity(friendrequest);
         finish();
            }
        });


        viewMyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userfriendsIntent=new Intent(MainActivity.this,UserFriendsActivity.class);
                startActivity(userfriendsIntent);
                finish();
            }
        });



        view_page=(Button)findViewById(R.id.view_page);
        view_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageActv=new Intent(MainActivity.this,PageActivity.class);
                startActivity(pageActv);
                finish();
            }
        });
        view_group=(Button)findViewById(R.id.view_group);
        view_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupActv=new Intent(MainActivity.this,GroupActivity.class);
                startActivity(groupActv);
                finish();
            }
        });
        showGroupRequest=(Button) findViewById(R.id.show_group_request) ;
        showGroupRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent admingroupreq=new Intent(MainActivity.this,AdminGroupRequestActivity.class);
                startActivity(admingroupreq);
                finish();
            }
        });
/*****************************************************/


//**************************  action listener on notification icon**********************************

        notification_icon = findViewById(R.id.notification_icon);
        likeNotify = findViewById(R.id.like_icon);
        disLikeNotifi=findViewById(R.id.dislike_icon);
        addFriendNotifi = findViewById(R.id.addfriend_icon);
        removeFrindNotify = findViewById(R.id.removeFriend_icon);
        ActivitylogBtn= findViewById(R.id.Activity_log);
        //commentNotify=findViewById(R.id.commentNotify);



        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isActivityLog=false;


                Intent makepage = new Intent(MainActivity.this, FragmentPreview.class);
                startActivity(makepage);

                /*NotificationFragment fragment = new NotificationFragment();
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.notification_fragment_container ,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

            }
        });

        ActivitylogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isActivityLog=true;

                Intent makepage = new Intent(MainActivity.this, FragmentPreview.class);
                startActivity(makepage);

                /*NotificationFragment fragment = new NotificationFragment();
                FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.notification_fragment_container ,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

            }
        });


        //************* action listener on like button******************
        likeNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent makepage=new Intent(MainActivity.this,NotificationFragment.class);
                startActivity(makepage);*/
                // L7zI36Be0qS2pwLic4Jd8RDdWjD2
                String postid1 = "-LcCMOBCnOomGRFyCwfL";
                String postid2 = "-LcCMP9H7kmcuYwvfdZC";
                //String myuserId="currentUser";
                String Aya = "cR6RdBeU5Lg7CEFLhEniBT16ZxM2";

                String Eman = "bb6wXlVRzdUWnij5KkgPltKdCz43";


                //addNotificationsOfLikes(postid1, post_publisher_Id);
                String post_publisher_Id =Eman;
                notifi.addNotificationsOfLikes(postid2, "L7zI36Be0qS2pwLic4Jd8RDdWjD2");
                notifi.addNotificationsOfLikes(postid1, "L7zI36Be0qS2pwLic4Jd8RDdWjD2");

                //addNotificationsOfComments(postid1, LikeridAya);
                notifi.addNotificationsOfComments(postid2, "L7zI36Be0qS2pwLic4Jd8RDdWjD2");
                notifi.addNotificationsOfComments(postid1, "L7zI36Be0qS2pwLic4Jd8RDdWjD2");

                //Add activitylog on likes , comments
                activityLog.addActivityLogOfLikes(postid2);
                activityLog.addActivityLogOfComments(postid1);




                /*Add friend request notification header
                   parm Friend Want To Request Id
                 */

                String FriendWantToRequest_Id= Aya;

               // notifi.addNotificationsOfFriendRequest(FriendWantToRequest_Id);


            }
        });


        addFriendNotifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ibrahem ="OjEqHKzicMOZkAzjl5OLxBcIpai2";
                String FriendWantToRequest_Id= ibrahem;

                notifi.addNotificationsOfFriendRequest(FriendWantToRequest_Id);

                /// son sends a friend request to
                activityLog.addActivityLogOfSendFriendRequest(FriendWantToRequest_Id);


                //son recieve a friend request from
               // activityLog.addLogReceiveFriendRequest(FriendWantToRequest_Id);

            }
        });




        removeFrindNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Aya = "OjEqHKzicMOZkAzjl5OLxBcIpai2";
                String FriendWantToRequest_Id= Aya;



                notifi.DeleteNotificationOfCancelFriendRequest(FriendWantToRequest_Id);
            }
        });


        disLikeNotifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postid2 = "-LcCMOBCnOomGRFyCwfL";

                notifi.DeleteNotificationOfLike(postid2, "L7zI36Be0qS2pwLic4Jd8RDdWjD2");
            }
        });

        GetMyHomePosts();



    }











    //****************************** add notification on like********************************



    @Override
    protected void onStart() {

        if (mAuth.getCurrentUser() == null) {
            main_login();
        }
        super.onStart();
    }

    private void CreatePost(String Uid) {
        Intent login_main = new Intent(MainActivity.this, Create_Post.class);
        login_main.putExtra("userId", Uid);
        login_main.putExtra("typePost", "1");
        startActivity(login_main);
        finish();
    }

    private void CreateComment(String Uid) {
        Intent login_main = new Intent(MainActivity.this, Create_Comment.class);
        login_main.putExtra("userId", Uid);
        login_main.putExtra("typePost", "1");
        startActivity(login_main);
        finish();
    }

    private void main_signup() {
        Intent main_signup = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(main_signup);
        finish();
    }

    private void main_login() {
        Intent main_login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(main_login);
        finish();

    }

    private void GoAccount() {
        Intent goAccount = new Intent(MainActivity.this, AccountActivity.class);
        startActivity(goAccount);
        finish();
    }

    private void GetMyHomePosts(){
        if(mAuth.getCurrentUser()!=null){
            final HomePostsAdapter postadapter=new HomePostsAdapter(MainActivity.this,myposts);
            Post_listview.setAdapter(postadapter);
           final ArrayList<String> communityIds=new ArrayList<String>();
            final ArrayList<String> friendsList=new ArrayList<String>();

            myRef3.child(mAuth.getCurrentUser().getUid()).child("userFriends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null) {
                        String myfriends = dataSnapshot.getValue(String.class);
                        final String[] myFriendsID = myfriends.split(",");
                        for(String id:myFriendsID){
                            friendsList.add(id);
                        } }
                    memberRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue(Members.class)!=null) {
                                for (DataSnapshot membersnapshot : dataSnapshot.getChildren()) {
                                    Members member = membersnapshot.getValue(Members.class);
                                    communityIds.add(member.getCommunityid());
                                    //System.out.println(member.getCommunityid());
                                }
                            }
                            myRef2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    myposts.clear();
                                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                        Posts post = postsnapshot.getValue(Posts.class);
                                        if (friendsList.contains(post.getUserId())&&TextUtils.equals(post.getPlaceTypeId(),"1")|| communityIds.contains(post.getPlaceId())) {
                                            myposts.add(post);
                                        }
                                    }
                                    Collections.reverse(myposts);
                                    postadapter.notifyDataSetChanged();
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
    }



}
