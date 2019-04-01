package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
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
    private Integer userCompleteProfile=0;
    private Button logout,show_comment;
    private ListView Post_listview;
    private String friendlist;
    private ArrayList<String> friendspostskeys=new ArrayList<String>();
    private ArrayList<Posts> Postss=new ArrayList<Posts>();
    final long ONE_MEGABYTE = 1024 * 1024;
    private Button makePage;
    private FloatingActionButton floatingActionButton;
    private Button goprofile;
    private Button makeGroup;
    private Button addchild;
    private Button mychildren;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
        mStorageRef2 = FirebaseStorage.getInstance().getReference("PostsImages/");
        goprofile=(Button)findViewById(R.id.goprofile);

        floatingActionButton = findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!=null)
                {
                    CreatePost(currentUser.getUid());

                }

            }
        });

        show_comment=findViewById( R.id.ShowComments );
        show_comment.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                CreateComment(currentUser.getUid());
            }
        } );

        makePage=(Button) findViewById(R.id.makepage);
        logout=(Button) findViewById(R.id.logout);
        makeGroup=(Button) findViewById(R.id.makeGroup);
        logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mAuth.getCurrentUser()!=null){
                   mAuth.signOut();
                   main_login();

               }
           }
       });
        makePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Intent makepage=new Intent(MainActivity.this,AddPageActivity.class);
         startActivity(makepage);
         //finish();
            }
        });
      makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent makeGroup=new Intent(MainActivity.this,MakeGroupActivity.class);
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
        addchild=(Button) findViewById(R.id.addchild);
        addchild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addchild=new Intent(MainActivity.this,AddChildActivity.class);
                startActivity(addchild);
                // finish();
            }
        });
        mychildren=(Button) findViewById(R.id.mychildren);
        mychildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mychildren=new Intent(MainActivity.this,MyChildrenActivity.class);
                startActivity(mychildren);
                finish();
            }
        });








    }

    @Override
    protected void onStart() {

        if(mAuth.getCurrentUser()==null){
            main_login();
        }
        super.onStart();
    }

    private void CreatePost(String Uid){
        Intent login_main=new Intent(MainActivity.this,Create_Post.class);
        login_main.putExtra("userId",Uid);
        login_main.putExtra("typePost","1");
        startActivity(login_main);
        finish();
    }

    private void CreateComment(String Uid){
        Intent login_main=new Intent(MainActivity.this,Create_Comment.class);
        login_main.putExtra("userId",Uid);
        login_main.putExtra("typePost","1");
        startActivity(login_main);
        finish();
    }

    private void main_signup(){
        Intent main_signup=new Intent(MainActivity.this,SignupActivity.class);
        startActivity(main_signup);
        finish();
    }
    private void main_login(){
        Intent main_login=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(main_login);
        finish();

    }
    private void GoAccount(){
        Intent goAccount=new Intent(MainActivity.this,AccountActivity.class);
        startActivity(goAccount);
        finish();
    }








}
