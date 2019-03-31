package com.example.parenteye;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postref = database.getReference("Posts");
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference CommunityRef = database.getReference("Community");
    DatabaseReference FriendRequestRef = database.getReference("FriendRequest");
    DatabaseReference friendsRef = database.getReference("Friends");
    DatabaseReference membersRef = database.getReference("Members");
    private StorageReference mStorageRef;
    private StorageReference mStorageRef2;
    private StorageReference mStorageRef3;
    private StorageReference mStorageRef4;
    private ListView Post_listview;
    private CircleImageView Accountprofile;
    private ImageView AccountCover;
    private TextView Accountname;
    private TextView useraddresse;
    private TextView usergender;
    private TextView friendsNumber;
    final long ONE_MEGABYTE = 1024 * 1024;
    private Button Addfriend;
    private boolean IsExist=false;
    private String key=null;
    ArrayList<custom_posts_returned> Profile_posts=new ArrayList<custom_posts_returned>();
    ArrayList<custom_posts_returned> Group_posts=new ArrayList<custom_posts_returned>();
    ArrayList<custom_posts_returned> Page_posts=new ArrayList<custom_posts_returned>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);



        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
        mStorageRef2 = FirebaseStorage.getInstance().getReference("PostImages/");
        mStorageRef3 = FirebaseStorage.getInstance().getReference("PageImages/");
        mStorageRef4 = FirebaseStorage.getInstance().getReference("GroupImages/");

        Post_listview=(ListView)findViewById(R.id.accountPost_listview);
        Accountprofile=(CircleImageView) findViewById(R.id.Accountprofile);
        AccountCover=(ImageView)findViewById(R.id.Accountcover);
        Accountname=(TextView) findViewById(R.id.Accountname);
        useraddresse=(TextView) findViewById(R.id.userAdresse);
        usergender=(TextView) findViewById(R.id.userGender);
        friendsNumber=(TextView) findViewById(R.id.friendsNumber);
        Addfriend=(Button) findViewById(R.id.addfriend);
        Addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             final String userId="memzoiT3c2TbPsACnDMNcl7jnrs2";
                if (IsExist == true) {
                    FriendRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot friendtSnapshot : dataSnapshot.getChildren()) {
                                FriendRequest fr = friendtSnapshot.getValue(FriendRequest.class);
                                if (TextUtils.equals(fr.getSenderid(), mAuth.getCurrentUser().getUid()) && TextUtils.equals(fr.getRecieverid(), userId)) {
                                    FriendRequestRef.child(friendtSnapshot.getKey()).removeValue();
                                    Addfriend.setText("Add Friend");
                                    IsExist = false;

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // ...
                        }
                    });
                }
                if(IsExist!=true){
                    FriendRequest friend=new FriendRequest();
                    friend.setSenderid(mAuth.getCurrentUser().getUid());
                    friend.setRecieverid("memzoiT3c2TbPsACnDMNcl7jnrs2");
                    friend.setState(1);
                    FriendRequestRef.push().setValue(friend);
                    Addfriend.setText("cancel request");
                    IsExist=true;
                }

            }});


    }



    @Override
    protected void onStart() {
       // GetUserProfiledata();
        GetPagePosts();

        super.onStart();
    }

    private void GetUserProfiledata(){
        final String userID="memzoiT3c2TbPsACnDMNcl7jnrs2";
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Users checkuser=dataSnapshot.getValue(Users.class);
                if(TextUtils.equals(dataSnapshot.getKey(),userID)){
                    Accountname.setText(checkuser.getUsername());
                    useraddresse.setText(checkuser.getLocation());
                    if(checkuser.isGender()==true){
                        usergender.setText("Male");
                    }
                    else{
                        usergender.setText("Female");
                    }
                    if(checkuser.getProfile_pic_id()!=null){
                        mStorageRef.child(checkuser.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                Accountprofile.setImageBitmap(bm);

                            }
                        });
                    }
                    else{
                        Accountprofile.setImageResource(R.drawable.defaultprofile);
                    }
                    if(checkuser.getCover_pic_id()!=null){
                        mStorageRef.child(checkuser.getCover_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                AccountCover.setImageBitmap(bm);

                            }
                        });
                    }
                    else{
                        AccountCover.setImageResource(R.drawable.cover);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        friendsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Friends fr=dataSnapshot.getValue(Friends.class);
                if(TextUtils.equals(fr.getUserId(),userID)){
                    String []friends=fr.getUserFriends().split(",");
                    for(String friend:friends){
                        if(TextUtils.equals(friend,mAuth.getCurrentUser().getUid())){
                            Addfriend.setText("Friends");
                            Addfriend.setEnabled(false);
                        }
                    }

                    friendsNumber.setText(friends.length);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FriendRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FriendRequest friendRequest=dataSnapshot.getValue(FriendRequest.class);
                if(TextUtils.equals(friendRequest.getSenderid(),mAuth.getCurrentUser().getUid())&&TextUtils.equals(friendRequest.getRecieverid(),userID)&&friendRequest.getState()==1){
                    Addfriend.setText("cancel request");
                    IsExist=true;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //GetProfilePosts();
        GetProfilePosts();

    }


    private void GetProfilePosts()
    {

        final ProfilePostAdapter postadapterr=new ProfilePostAdapter(AccountActivity.this,Profile_posts);
        Post_listview.setAdapter(postadapterr);


        final String username="Ibrahim";
        final String profileId="memzoiT3c2TbPsACnDMNcl7jnrs2";
        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile_posts.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Posts newpost=postSnapshot.getValue(Posts.class);
                    if(TextUtils.equals(newpost.getUserId(),profileId)&&TextUtils.equals(newpost.getPlaceTypeId(),"1")){
                        custom_posts_returned custom =new custom_posts_returned();
                        custom.setPost_owner_name(username);
                        custom.setpost_owner_ID(newpost.getUserId());
                        if(newpost.getPostcontent()!=null){
                            custom.setPost_text(newpost.getPostcontent());
                        }
                        if(newpost.isHasimage()==true){
                            custom.setPost_image(newpost.getImageId());
                        }
                        Profile_posts.add(custom);

                    }

                }

                Collections.reverse(Profile_posts);
                postadapterr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });











    }

    private void GetPagePosts(){
        final String PageId="-La0sXFPy2dXzxX-Xws6";
        final String PageName="aya page";
       final PagePostAdapter pageAdapter=new PagePostAdapter(AccountActivity.this,Group_posts);
        Post_listview.setAdapter(pageAdapter);
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



































/*
    private void Group_posts(){
        Group_posts.clear();
      final  PostAdapter postadapter=new PostAdapter(AccountActivity.this,Group_posts);
        Post_listview.setAdapter(postadapter);
        final String GroupId="-LaD606SB3PE0sWfy6Pc";
        postref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               final Posts post=dataSnapshot.getValue(Posts.class);
                if(TextUtils.equals(post.getPlaceTypeId(),"3")&&TextUtils.equals(post.getPlaceId(),GroupId)){
                    final custom_posts_returned custompost=new custom_posts_returned();
                    if(post.getPostcontent()!=null){
                        custompost.setPost_text(post.getPostcontent());
                    }
                    if(post.isHasimage()==true){
                        mStorageRef2.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bm2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm2 = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm2);
                                custompost.setPost_image(bm2);
                             userRef.addChildEventListener(new ChildEventListener() {
                                 @Override
                                 public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    final Users user=dataSnapshot.getValue(Users.class);
                                     if(TextUtils.equals(dataSnapshot.getKey(),post.getUserId())){
                                         custompost.setPost_owner_name(user.getUsername());
                                         if(user.getProfile_pic_id()!=null){
                                             mStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                 @Override
                                                 public void onSuccess(byte[] bytes) {
                                                     Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                     DisplayMetrics dm = new DisplayMetrics();
                                                     getWindowManager().getDefaultDisplay().getMetrics(dm);
                                                     custompost.setProfile_image(bm);
                                                     Group_posts.add(custompost);
                                                     postadapter.notifyDataSetChanged();
                                                 }
                                             });

                                         }
                                         else{
                                             Group_posts.add(custompost);
                                             postadapter.notifyDataSetChanged();
                                         }
                                     }
                                 }

                                 @Override
                                 public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                 }

                                 @Override
                                 public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                 }

                                 @Override
                                 public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                 }
                             });
                            }
                        });

                    }
                    else{
                        userRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                final Users user=dataSnapshot.getValue(Users.class);
                                if(TextUtils.equals(dataSnapshot.getKey(),post.getUserId())){
                                    custompost.setPost_owner_name(user.getUsername());
                                    if(user.getProfile_pic_id()!=null){
                                        mStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                DisplayMetrics dm = new DisplayMetrics();
                                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                                custompost.setProfile_image(bm);
                                                Group_posts.add(custompost);
                                                postadapter.notifyDataSetChanged();
                                            }
                                        });

                                    }
                                    else{
                                        Group_posts.add(custompost);
                                        postadapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void Page_posts(){
        Page_posts.clear();
        final String PageId="-La0sXFPy2dXzxX-Xws6";
        final String PageName="aya page";
        final PostAdapter postadapter=new PostAdapter(AccountActivity.this,Page_posts);
        Post_listview.setAdapter(postadapter);

    postref.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           Posts post=dataSnapshot.getValue(Posts.class);
           if(TextUtils.equals(post.getPlaceTypeId(),"2")&&TextUtils.equals(post.getPlaceId(),PageId)){
               final custom_posts_returned custompost=new custom_posts_returned();
               custompost.setPost_owner_name(PageName);
               if(post.getPostcontent()!=null){
                   custompost.setPost_text(post.getPostcontent());
               }
               if(post.isHasimage()==true){
                   mStorageRef2.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                       @Override
                       public void onSuccess(byte[] bytes) {
                           Bitmap bm2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                           DisplayMetrics dm2 = new DisplayMetrics();
                           getWindowManager().getDefaultDisplay().getMetrics(dm2);
                           custompost.setPost_image(bm2);
                           CommunityRef.addChildEventListener(new ChildEventListener() {
                               @Override
                               public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                   Community com=dataSnapshot.getValue(Community.class);
                                   if(TextUtils.equals(dataSnapshot.getKey(),PageId)){
                                       if(com.getPhotoId()!=null){
                                           mStorageRef3.child(com.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                               @Override
                                               public void onSuccess(byte[] bytes) {
                                                   Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                   DisplayMetrics dm = new DisplayMetrics();
                                                   getWindowManager().getDefaultDisplay().getMetrics(dm);
                                                   custompost.setProfile_image(bm);
                                                   Page_posts.add(custompost);
                                                   postadapter.notifyDataSetChanged();
                                               }
                                           });

                                       }
                                       else{
                                           Page_posts.add(custompost);
                                           postadapter.notifyDataSetChanged();
                                       }
                                   }
                               }

                               @Override
                               public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                               }

                               @Override
                               public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                               }

                               @Override
                               public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {

                               }
                           });
                       }
                   });


               }
               else{
                   CommunityRef.addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                           Community com=dataSnapshot.getValue(Community.class);
                           if(TextUtils.equals(dataSnapshot.getKey(),PageId)){
                               if(com.getPhotoId()!=null){
                                   mStorageRef3.child(com.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                       @Override
                                       public void onSuccess(byte[] bytes) {
                                           Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                           DisplayMetrics dm = new DisplayMetrics();
                                           getWindowManager().getDefaultDisplay().getMetrics(dm);
                                           custompost.setProfile_image(bm);
                                           Page_posts.add(custompost);
                                           postadapter.notifyDataSetChanged();
                                       }
                                   });

                               }
                               else{
                                   Page_posts.add(custompost);
                                   postadapter.notifyDataSetChanged();
                               }
                           }
                       }

                       @Override
                       public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                       }

                       @Override
                       public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                       }

                       @Override
                       public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

               }

           }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    private void checkrequest(final String recieverId){
        FriendRequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FriendRequest friendRequest=dataSnapshot.getValue(FriendRequest.class);
                if(TextUtils.equals(friendRequest.getSenderid(),mAuth.getCurrentUser().getUid())&&TextUtils.equals(friendRequest.getRecieverid(),recieverId)&&friendRequest.getState()==1){
                    //FriendRequestRef.child(dataSnapshot.getKey()).removeValue();
                    Addfriend.setText("Pending");
                    IsExist=true;
                    System.out.println("in function "+IsExist);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void GetProfilePosts(){
        Profile_posts.clear();
        final PostAdapter postadapter=new PostAdapter(AccountActivity.this,Profile_posts);
        Post_listview.setAdapter(postadapter);

        final String username="Ibrahim ahmed";
        final String profileUserId="memzoiT3c2TbPsACnDMNcl7jnrs2";
        postref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Posts checkedpost=dataSnapshot.getValue(Posts.class);
                if(TextUtils.equals(checkedpost.getUserId(),profileUserId)){
                  final  custom_posts_returned custompost=new custom_posts_returned();
                    custompost.setPost_owner_name(username);
                    if(checkedpost.getPostcontent()!=null){
                        custompost.setPost_text(checkedpost.getPostcontent());
                    }
                    if(checkedpost.isHasimage()==true){
                        mStorageRef2.child(checkedpost.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bm2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm2 = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm2);
                                custompost.setPost_image(bm2);
                               userRef.addChildEventListener(new ChildEventListener() {
                                   @Override
                                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                       Users user=dataSnapshot.getValue(Users.class);
                                       if(TextUtils.equals(dataSnapshot.getKey(),profileUserId)){
                                           if(user.getProfile_pic_id()!=null){
                                               mStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                   @Override
                                                   public void onSuccess(byte[] bytes) {
                                                       Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                       DisplayMetrics dm = new DisplayMetrics();
                                                       getWindowManager().getDefaultDisplay().getMetrics(dm);
                                                      custompost.setProfile_image(bm);
                                                       Profile_posts.add(custompost);
                                                        postadapter.notifyDataSetChanged();
                                                   }
                                                   });

                                           }
                                           else{
                                               Profile_posts.add(custompost);
                                               postadapter.notifyDataSetChanged();
                                           }
                                       }

                                   }

                                   @Override
                                   public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                   }

                                   @Override
                                   public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                   }

                                   @Override
                                   public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });

                            }
                        });

                    }else{
                        userRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Users user=dataSnapshot.getValue(Users.class);
                                if(TextUtils.equals(dataSnapshot.getKey(),profileUserId)){
                                    if(user.getProfile_pic_id()!=null){
                                        mStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                DisplayMetrics dm = new DisplayMetrics();
                                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                                custompost.setProfile_image(bm);
                                                Profile_posts.add(custompost);
                                                postadapter.notifyDataSetChanged();
                                            }
                                        });

                                    }
                                    else{
                                        Profile_posts.add(custompost);
                                        postadapter.notifyDataSetChanged();
                                    }
                                }

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
      private void getPageInfo(){
        Addfriend.setText("Like");
       final String pageId="-La0sXFPy2dXzxX-Xws6";
       CommunityRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Community com=dataSnapshot.getValue(Community.class);
               if(TextUtils.equals(com.getTypeid(),"2")&&TextUtils.equals(dataSnapshot.getKey(),pageId)){
                   Accountname.setText(com.getCommunityname());
                   useraddresse.setText(com.getCommunityAbout());
                   if(com.getPhotoId()!=null){
                       mStorageRef3.child(com.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                           @Override
                           public void onSuccess(byte[] bytes) {
                               final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               DisplayMetrics dm = new DisplayMetrics();
                               getWindowManager().getDefaultDisplay().getMetrics(dm);
                               Accountprofile.setImageBitmap(bm);



                           }
                       });

                   }
                   else{
                       Accountprofile.setImageResource(R.drawable.defaultprofile);
                   }
                   if(com.getCoverPhotoId()!=null){
                       mStorageRef3.child(com.getCoverPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                           @Override
                           public void onSuccess(byte[] bytes) {
                               final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               DisplayMetrics dm = new DisplayMetrics();
                               getWindowManager().getDefaultDisplay().getMetrics(dm);
                               AccountCover.setImageBitmap(bm);



                           }
                       });
                   }
                   else{
                       AccountCover.setImageResource(R.drawable.cover);
                   }

               }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
       membersRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Members member=dataSnapshot.getValue(Members.class);
               if (TextUtils.equals(member.getId(), "2") && TextUtils.equals(member.getCommunityid(), pageId) && TextUtils.equals(member.getUserId(), mAuth.getCurrentUser().getUid())) {

                   Addfriend.setText("Liked");
                   Addfriend.setEnabled(false);
               }
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
          Page_posts();
    }

      private void getGroupInfo(){
          Addfriend.setText("Join Group");
         final String groupid="-LaD606SB3PE0sWfy6Pc";

        CommunityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Community com=dataSnapshot.getValue(Community.class);
                if(TextUtils.equals(com.getTypeid(),"1")&&TextUtils.equals(dataSnapshot.getKey(),groupid)){
                    Accountname.setText(com.getCommunityname());
                    useraddresse.setText(com.getCommunityAbout());
                    if(com.getPhotoId()!=null){
                        mStorageRef4.child(com.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                Accountprofile.setImageBitmap(bm);



                            }
                        });

                    }
                    else{
                        Accountprofile.setImageResource(R.drawable.defaultprofile);
                    }
                    if(com.getCoverPhotoId()!=null){
                        mStorageRef4.child(com.getCoverPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
                                AccountCover.setImageBitmap(bm);



                            }
                        });
                    }
                    else{
                        AccountCover.setImageResource(R.drawable.cover);
                    }

                }

                membersRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Members member=dataSnapshot.getValue(Members.class);
 if(TextUtils.equals(member.getTyptId(),"1")&&TextUtils.equals(member.getCommunityid(),groupid)&&TextUtils.equals(member.getUserId(),mAuth.getCurrentUser().getUid())){
     Addfriend.setText("Joined");
 }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


          Group_posts();
      }

*/






}
