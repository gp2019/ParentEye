package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

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
    private Button addchild;
    private Button mychildren;
    private ImageView firendrequestid;


    private ImageButton notification_icon;


    private ImageButton likeNotify;

    private Button commentNotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
        mStorageRef2 = FirebaseStorage.getInstance().getReference("PostsImages/");
        goprofile = (Button) findViewById(R.id.goprofile);
        firendrequestid=(ImageView) findViewById(R.id.firendrequestid);

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

/*****************************************************/


//**************************  action listener on notification icon**********************************

        notification_icon = findViewById(R.id.notification_icon);
        likeNotify = findViewById(R.id.like_icon);
        //commentNotify=findViewById(R.id.commentNotify);

        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makepage = new Intent(MainActivity.this, NotificationPreview.class);
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
                String postid1 = "-LbU2g1M1oso-oW9qOPB";
                String postid2 = "-LbU7gdO1Mkx4ZhulHxA";
                //String myuserId="currentUser";
                String LikeridAya = "cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
                String LikerEman = "bb6wXlVRzdUWnij5KkgPltKdCz43";


                addNotificationsOfLikes(postid1, LikeridAya);
                addNotificationsOfLikes(postid2, LikerEman);

                addNotificationsOfComments(postid1, LikeridAya);

                addNotificationsOfComments(postid2, LikerEman);


            }
        });


    }


    //****************************** add notification on like********************************
    private void addNotificationsOfLikes(String postid, String Likerid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference notification_reference = database.getReference("notifications").child(currentUser.getUid());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", Likerid); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage", "liked your post");
        hashMap.put("postId", postid);
        hashMap.put("isPost", true);


        notification_reference.push().setValue(hashMap);

        //to call ----->  addNotificationsOfLikes(post.getpublisher() //getUserId\\ , post.getPostId())

    }


    //************************  add notification fun on comment***************

    private void addNotificationsOfComments(String postid, String commenter) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        DatabaseReference notification_reference = database.getReference("notifications").child(currentUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", commenter); // or hashMap.put("userid ", firebaseUser.getUid());
        hashMap.put("NotificationMessage", "commented: welcome clever");
        hashMap.put("postId", postid);
        hashMap.put("isPost", true);

        notification_reference.push().setValue(hashMap);

        //+addcomment.getText().toString()
        //to call ----->  addNotificationsOfFollowers(post.getpublisher() //getUserId\\ )
    }


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



}
