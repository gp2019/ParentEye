package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class PageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference CommunityRef = database.getReference("Community");
    DatabaseReference membersRef = database.getReference("Members");
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference postref = database.getReference("Posts");
    private TextView pagename;
    private ImageView pagePhoto;
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference pagephotoRef;
    private Button Like_unLike;
    private boolean IsExist=false;
    ArrayList<custom_posts_returned> Page_posts=new ArrayList<custom_posts_returned>();
    private ListView Page_Post_listview;
    private FloatingActionButton floatingActionButton;
    private String CommunityId;
    public static final String pageID="pageID";
    private CreateTime createTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        IntializeVariables();



        floatingActionButton = findViewById(R.id.floatingButton);

        // CommunityId = "Lh2x7ArurH4Yu4-XZPW";
        Intent intent = getIntent();
        CommunityId = intent.getStringExtra("searched_page_Id");
        floatingActionButton.setVisibility(View.GONE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    CreatePost(currentUser.getUid());
                }

            }
        });
        Like_unLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = getIntent();
                    final   String PageId = intent.getStringExtra("searched_page_Id");
                   // final String PageId = "-Lg1OLwvGrf5AJFx6-jK"; //will be get automatic later
                    if (IsExist == true) {
                        membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot membersnapshot : dataSnapshot.getChildren()) {
                                    Members member = membersnapshot.getValue(Members.class);
                                    if (TextUtils.equals(mAuth.getCurrentUser().getUid(), member.getUserId()) && TextUtils.equals(member.getTyptId(), "2") && TextUtils.equals(member.getCommunityid(), PageId)) {
                                        membersRef.child(membersnapshot.getKey()).removeValue();
                                        Like_unLike.setText("Like Page");
                                        IsExist = false;
                                        floatingActionButton.setVisibility(View.GONE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    if (IsExist == false) {
                        Members new_member = new Members();
                        new_member.setTyptId("2");
                        new_member.setUserId(mAuth.getCurrentUser().getUid());
                        new_member.setCommunityid(PageId);
                        membersRef.push().setValue(new_member);
                        Like_unLike.setText("UnLike Page");
                        IsExist = true;
                    }

                }
            }
        });
        pagename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                final   String PageId = intent.getStringExtra("searched_page_Id");
         // final String PageId="-Lg1OLwvGrf5AJFx6-jK"; //will be get automatic later
                Intent aboutIntent=new Intent(getApplicationContext(),CommunityAboutandmembersActivity.class);
                aboutIntent.putExtra(pageID,PageId);
                startActivity(aboutIntent);
            }
        });



    }


    private void CreatePost(String Uid) {
        Intent login_main = new Intent(PageActivity.this, Create_Post.class);
        login_main.putExtra("userId", Uid);
        login_main.putExtra("placeTypeId", "2");
        login_main.putExtra("placeId", CommunityId);
        startActivity(login_main);
        finish();
    }

    private void IntializeVariables(){
        mAuth=FirebaseAuth.getInstance();
        pagename=(TextView)findViewById(R.id.pagename_id);
        pagePhoto=(ImageView)findViewById(R.id.pageImg);
        pagephotoRef = FirebaseStorage.getInstance().getReference("PageImages/");
        Like_unLike=(Button)findViewById(R.id.LikeUnLike_ID);
        Page_Post_listview=(ListView)findViewById(R.id.pageList);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getPageInfo();

        CommunityRef.child(CommunityId).child("adminId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (TextUtils.equals(dataSnapshot.getValue(String.class),mAuth.getCurrentUser().getUid())){
                    floatingActionButton.setVisibility(View.VISIBLE);
                }else{
                    floatingActionButton.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
        getPageInfo();
    }

   /* @Override
    protected void onPostResume() {

        super.onPostResume();
        getPageInfo();
    }
    */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPageInfo();
    }

    public  void getPageInfo(){
        if(mAuth.getCurrentUser()!=null){
            Intent intent = getIntent();
            final   String PageId = intent.getStringExtra("searched_page_Id");
         // final String PageId="-Lg1OLwvGrf5AJFx6-jK"; //will be get automatic later
            CheckIsAdmin();
            CommunityRef.child(PageId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       Community page=dataSnapshot.getValue(Community.class);
                    pagename.setText(page.getCommunityname());
                    if(page.getPhotoId()!=null){
                        pagephotoRef.child(page.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                pagePhoto.setImageBitmap(bm);

                            }
                        });

                    }
                    if(TextUtils.equals(page.getAdminId(),mAuth.getCurrentUser().getUid())){

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
         if(TextUtils.equals(mAuth.getCurrentUser().getUid(),member.getUserId())&&TextUtils.equals(member.getTyptId(),"2")&&TextUtils.equals(member.getCommunityid(),PageId)){
          Like_unLike.setText("UnLike Page");
          IsExist=true;

         }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            GetPagePosts();
        }
        }
    private void CheckIsAdmin(){
        Intent intent = getIntent();
        String searchedpageId  = intent.getStringExtra("searched_page_Id");
        CommunityRef.child(searchedpageId).child("adminId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String admin_id=dataSnapshot.getValue(String.class);
                if(TextUtils.equals(admin_id,mAuth.getCurrentUser().getUid())){
                    Like_unLike.setText("you are the admin");
                    Like_unLike.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void GetPagePosts(){
        Intent intent = getIntent();
        final   String PageId = intent.getStringExtra("searched_page_Id");
       // final   String PageName = intent.getStringExtra("searched_Item_name");
        final PagePostAdapter pageAdapter=new PagePostAdapter(PageActivity.this,Page_posts);
        Page_Post_listview.setAdapter(pageAdapter);
        CommunityRef.child(PageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              final String PageName=dataSnapshot.getValue(Community.class).getCommunityname();

                postref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Page_posts.clear();
                        for(DataSnapshot pagepostsnapshot:dataSnapshot.getChildren()){
                            Posts pagepost=pagepostsnapshot.getValue(Posts.class);
                            if(TextUtils.equals(pagepost.getPlaceTypeId(),"2")&&TextUtils.equals(pagepost.getPlaceId(),PageId)){
                                custom_posts_returned custom=new custom_posts_returned();
                                custom.setPost_owner_name(PageName);
                                custom.setpost_owner_ID(pagepost.getPlaceId());
                                custom.setPost_Id(pagepostsnapshot.getKey());
                                String timePuplisher =pagepost.getPostdate();
                                createTime =new CreateTime(timePuplisher);
                                try {
                                    createTime.sdf();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                custom.setPost_date(createTime.calculateTime());
                                if(pagepost.getPostcontent()!=null){
                                    custom.setPost_text(pagepost.getPostcontent());
                                    // System.out.println("content "+ custom.getPost_text());
                                }
                                if(pagepost.isHasimage()==true){
                                    custom.setPost_image(pagepost.getImageId());
                                }
                                Page_posts.add(custom);
                                //   System.out.println("added "+ custom.getpost_owner_ID());
                            }
                        }

                        Collections.reverse(Page_posts);
                        pageAdapter.notifyDataSetChanged();
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
       // final String PageId="-Lg1OLwvGrf5AJFx6-jK"; //will be get automatic later
       // final String PageName="page 2";//will be get automatic later


    }



}
