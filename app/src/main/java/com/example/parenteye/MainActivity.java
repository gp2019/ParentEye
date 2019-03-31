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










/*
        Friends friend=new Friends();
        friend.setUserId("sfqRhCgFi5c5vDfMETgsrODa9Yh1");
        friend.setUserFriends("4tRSpQD4slPQMFQu5TYQXTPhQWT2,KESsF62g1WR48ZXztXXRFjlsqex2,Y07QYSgpGqMTQmuuo4PxeCRWKG93,lGoKGzSP2XMg9tkQsaqq8fa7l5u1,sfqRhCgFi5c5vDfMETgsrODa9Yh1");
        myRef3.push().setValue(friend);
*/



     // 1 sora 3and aya, 1 aya fady, ahmed fady,ayman sora
     /*   Posts post1=new Posts();
        post1.setUserId("w0hp6UeWq5Ul9Zt3rw5V6hoXvFA3");
        post1.setPlaceTypeId("1");
        post1.setPostcontent("This post is done by aya in her timeline");
        post1.setHasimage(true);
        post1.setImageId("");
        Posts post2=new Posts();
        post2.setUserId("w0hp6UeWq5Ul9Zt3rw5V6hoXvFA3");
        post2.setPlaceTypeId("1");
        post2.setHasimage(false);
        post2.setPostcontent("This post is done by aya in her timeline too");
        Posts post3=new Posts();
        post3.setUserId("w0hp6UeWq5Ul9Zt3rw5V6hoXvFA3");
        post3.setPlaceTypeId("3");
        post3.setPostcontent("This post is done by aya in her group ");
        post3.setHasimage(true);
        post3.setImageId("");
        Posts post4=new Posts();
        post4.setUserId("w0hp6UeWq5Ul9Zt3rw5V6hoXvFA3");
        post4.setPlaceTypeId("2");
        post4.setPostcontent("This post is done by aya in her page ");
        post4.setHasimage(true);
        post4.setImageId("");
        Posts post5=new Posts();
        post5.setUserId("4tRSpQD4slPQMFQu5TYQXTPhQWT2");
        post5.setPlaceTypeId("1");
        post5.setPostcontent("This post is done by alaa in her timeline ");
        post5.setHasimage(true);
        post5.setImageId("");
        Posts post6=new Posts();
        post6.setUserId("4tRSpQD4slPQMFQu5TYQXTPhQWT2");
        post6.setPlaceTypeId("2");
        post6.setPostcontent("This post is done by alaa in her page ");
        post6.setHasimage(false);
        Posts post7=new Posts();
        post7.setUserId("KESsF62g1WR48ZXztXXRFjlsqex2");
        post7.setPlaceTypeId("1");
        post7.setPostcontent("This post is done by ahmed in his timeline ");
        post7.setHasimage(false);
        Posts post8=new Posts();
        post8.setUserId("Y07QYSgpGqMTQmuuo4PxeCRWKG93");
        post8.setPlaceTypeId("1");
        post8.setHasimage(true);
        post8.setImageId("");
        post8.setPostcontent("This post is done by ayman in his timeline ");
        Posts post9=new Posts();
        post9.setUserId("lGoKGzSP2XMg9tkQsaqq8fa7l5u1");
        post9.setPlaceTypeId("2");
        post9.setHasimage(false);
        post9.setPostcontent("This post is done by ibrahim in his page ");
        myRef2.push().setValue(post1);
        myRef2.push().setValue(post2);
        myRef2.push().setValue(post3);
        myRef2.push().setValue(post4);
        myRef2.push().setValue(post5);
        myRef2.push().setValue(post6);
        myRef2.push().setValue(post7);
        myRef2.push().setValue(post8);
        myRef2.push().setValue(post9);


*/

    /*   ArrayList<custom_posts_returned> HomePosts=new ArrayList<custom_posts_returned>();
        Post_listview=(ListView)findViewById(R.id.Post_listview);
         PostAdapter postadapter=new PostAdapter(MainActivity.this,HomePosts);
        Post_listview.setAdapter(postadapter);
custom_posts_returned c=new custom_posts_returned();
c.setPost_owner_name("post1");
c.setPost_text("post1 text");
        custom_posts_returned c2=new custom_posts_returned();
        c2.setPost_owner_name("post1");
        c2.setPost_text("post1 text");
        custom_posts_returned c3=new custom_posts_returned();
        c3.setPost_owner_name("post1");
        c3.setPost_text("post1 text");
        custom_posts_returned c1=new custom_posts_returned();
        c1.setPost_owner_name("post1");
        c1.setPost_text("post1 text");
        custom_posts_returned c4=new custom_posts_returned();
        c4.setPost_owner_name("post1");
        c4.setPost_text("post1 text");
        custom_posts_returned c5=new custom_posts_returned();
        c5.setPost_owner_name("post1");
        c5.setPost_text("post1 text");
        custom_posts_returned c6=new custom_posts_returned();
        c6.setPost_owner_name("post1");
        c6.setPost_text("post1 text");
        custom_posts_returned c7=new custom_posts_returned();
        c7.setPost_owner_name("post1");
        c7.setPost_text("post1 text");
        custom_posts_returned c8=new custom_posts_returned();
        c8.setPost_owner_name("post1");
        c8.setPost_text("post1 text");
        custom_posts_returned c9=new custom_posts_returned();
        c9.setPost_owner_name("post1");
        c9.setPost_text("post1 text");
        custom_posts_returned c10=new custom_posts_returned();
        c10.setPost_owner_name("post1");
        c10.setPost_text("post1 text");
        HomePosts.add(c);
        HomePosts.add(c2);
        HomePosts.add(c3);
        HomePosts.add(c1);
        HomePosts.add(c4);
        HomePosts.add(c5);
        HomePosts.add(c6);
        HomePosts.add(c7);
        HomePosts.add(c8);
        HomePosts.add(c9);
        HomePosts.add(c10);
        postadapter.notifyDataSetChanged();

*/

    }

    @Override
    protected void onStart() {


        if(mAuth.getCurrentUser()==null){
            main_login();
        }
        else{
            check_complete_profile();

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
    private void check_complete_profile(){


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot UsersSnapshot: dataSnapshot.getChildren()) {
                     Users user=UsersSnapshot.getValue(Users.class);
                     String key=UsersSnapshot.getKey();
                     if(TextUtils.equals(key,mAuth.getCurrentUser().getUid())){
                         userCompleteProfile=1;
                    }

                }
                if(userCompleteProfile==0){
                    Intent userdataactivity_intent=new Intent(MainActivity.this,UserdataActivity.class);
                    startActivity(userdataactivity_intent);
                    finish();
                }



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }







}
