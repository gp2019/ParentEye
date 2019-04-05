package com.example.parenteye;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    final long ONE_MEGABYTE = 1024 * 1024;
    private ImageView Addfriend;
    private TextView addfriendtext;
    private TextView userEmail;
    private  TextView userdate;
    private boolean IsExist=false;
    private String key=null;
    ArrayList<custom_posts_returned> Profile_posts=new ArrayList<custom_posts_returned>();
    ArrayList<custom_posts_returned> Group_posts=new ArrayList<custom_posts_returned>();
    ArrayList<custom_posts_returned> Page_posts=new ArrayList<custom_posts_returned>();

   private Dialog myDialog;
   private  TextView txtclose;
   private Button btnsubmitpass;
   private EditText submitpassword;
   private EditText edituseremail;
   private EditText editAccountname;
   private EditText Edituseraddresse;
   private ImageView birthday;
   private Button btnsubmitupdate;
   private Button btncancelupdate;
    private  String name;
    private  String email;
    private String addresse;
    private ImageView gallery;



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
        userdate=(TextView) findViewById(R.id.userdate);
        useraddresse=(TextView) findViewById(R.id.userAdresse);
        usergender=(TextView) findViewById(R.id.userGender);
        userEmail=(TextView) findViewById(R.id.userEmail);
        Addfriend=(ImageView) findViewById(R.id.addfriend);
        addfriendtext=(TextView) findViewById(R.id.addfriendtext);
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custompopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        submitpassword=(EditText)myDialog.findViewById(R.id.passwordfield);
        btnsubmitpass = (Button)myDialog. findViewById(R.id.btnsubmitpassword);
        edituseremail=(EditText) findViewById(R.id.EdituserEmail);

        editAccountname=(EditText) findViewById(R.id.editAccountname);
        editAccountname.setVisibility(View.GONE);
        Edituseraddresse=(EditText)findViewById(R.id.Edituseraddresse);
        Edituseraddresse.setVisibility(View.GONE);
        birthday=(ImageView) findViewById(R.id.birthday);
        birthday.setEnabled(false);
        btnsubmitupdate=(Button) findViewById(R.id.btnsubmitupdate);
        btnsubmitupdate.setVisibility(View.GONE);
        btncancelupdate=(Button) findViewById(R.id.btncancelupdate);
        btncancelupdate.setVisibility(View.GONE);
        gallery=(ImageView) findViewById(R.id.gallery);
        Addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             final String userId="cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
             if(TextUtils.equals(mAuth.getCurrentUser().getUid(),userId)){
                 //here go updating profile


                 txtclose.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         myDialog.dismiss();
                     }
                 });

                 btnsubmitpass.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                  final String userpass=submitpassword.getText().toString().trim();
                   if(userpass.isEmpty()){
                       submitpassword.setError("you must enter the password");
                   }
                   else{
                       userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if(TextUtils.equals(dataSnapshot.getValue(Users.class).getUserPassword(),userpass)){
                                   myDialog.dismiss();
                                   submitpassword.setText("");
                                   prepareUpdate();
                                   Addfriend.setEnabled(false);

                               }
                               else{
                                   submitpassword.setError("Wrong password!");
                               }
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                   }

                     }
                 });
                 myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                 myDialog.show();


             }
               else{
                 if (IsExist == true) {
                     FriendRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             for (DataSnapshot friendtSnapshot : dataSnapshot.getChildren()) {
                                 FriendRequest fr = friendtSnapshot.getValue(FriendRequest.class);
                                 if (TextUtils.equals(fr.getSenderid(), mAuth.getCurrentUser().getUid()) && TextUtils.equals(fr.getRecieverid(), userId)) {
                                     FriendRequestRef.child(friendtSnapshot.getKey()).removeValue();
                                     Addfriend.setImageResource(R.drawable.addfriendd);
                                     addfriendtext.setText("Add Friend");
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
                     friend.setRecieverid(userId);
                     friend.setState(1);
                     FriendRequestRef.push().setValue(friend);
                     Addfriend.setImageResource(R.drawable.addfriendd);
                     addfriendtext.setText("cancel request");
                    Notifications notifi =new Notifications();
                    notifi.addNotificationsOfFriendRequest(userId);

                     IsExist=true;
                 }

             }
            }});
        btnsubmitupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    updateInfo();


                }

            }
        });

        btncancelupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returntoinfo();
                Addfriend.setEnabled(true);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Intent galleryIntent=new Intent(AccountActivity.this,GalleryActivity.class);
          startActivity(galleryIntent);
          finish();
            }
        });

    }



    @Override
    protected void onStart() {
        ViewUserProfile();
       // GetPagePosts();
       // GetGroupPosts();

        super.onStart();
    }

    private void ViewUserProfile(){
        if(mAuth.getCurrentUser()!=null) {
            final String userID = "cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
           //if the user enter his own profile
            if(TextUtils.equals(mAuth.getCurrentUser().getUid(),userID)){
                Addfriend.setImageResource(R.drawable.updateprofile);
                addfriendtext.setText("Update Profile");
                GetProfileData(userID);

            }

        //if the user enter another user profile
            else {
                // check if they are friends
                IsFriend(userID);
                //check if the cuurent user has sent friend request before
                IssentRequest(userID);
                GetProfileData(userID);
            }
           // GetProfilePosts();
        }
    }


    private void GetProfilePosts()
    {

        final ProfilePostAdapter postadapterr=new ProfilePostAdapter(AccountActivity.this,Profile_posts);
        Post_listview.setAdapter(postadapterr);


        final String username="aya";
        final String profileId="cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile_posts.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Posts newpost=postSnapshot.getValue(Posts.class);
                    if(TextUtils.equals(newpost.getUserId(),profileId)&&TextUtils.equals(newpost.getPlaceTypeId(),"1")){
                        custom_posts_returned custom =new custom_posts_returned();
                        custom.setPost_owner_name(newpost.getUserId());
                        custom.setpost_owner_ID(newpost.getUserId());
                        custom.setPost_Id(postSnapshot.getKey());
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
       final PagePostAdapter pageAdapter=new PagePostAdapter(AccountActivity.this,Page_posts);
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
           custom.setPost_Id(pagepostsnapshot.getKey());
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

    private void GetGroupPosts(){
        final String GropuId="-LaD606SB3PE0sWfy6Pc";
        final String GroupName="group1 test";
 final ProfilePostAdapter groupAdapter=new ProfilePostAdapter(AccountActivity.this,Group_posts);
        Post_listview.setAdapter(groupAdapter);
        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group_posts.clear();
           for(DataSnapshot grouppostsnapshot:dataSnapshot.getChildren()){
            Posts post=grouppostsnapshot.getValue(Posts.class);
   if(TextUtils.equals(post.getPlaceTypeId(),"3")&&TextUtils.equals(post.getPlaceId(),GropuId)){
         custom_posts_returned custom=new custom_posts_returned();
           custom.setPost_owner_name(post.getUserId());
         custom.setpost_owner_ID(post.getUserId());
         custom.setPost_Id(grouppostsnapshot.getKey());
       if(post.getPostcontent()!=null){
           custom.setPost_text(post.getPostcontent());
       }
       if(post.isHasimage()==true){
           custom.setPost_image(post.getImageId());
       }
       Group_posts.add(custom);
  }
           }
           Collections.reverse(Group_posts);
                groupAdapter.notifyDataSetInvalidated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

   private void GetProfileData(String userID){
       userRef.child(userID).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Users user=dataSnapshot.getValue(Users.class);
               Accountname.setText(user.getUsername());
               userEmail.setText(user.getUserEmail());
               useraddresse.setText(user.getLocation());
               userdate.setText(user.getDateofbirth().getDay()+"/"+user.getDateofbirth().getMonth()+"/"+user.getDateofbirth().getYear());

               // userdate.setText(user.getDateofbirth());
               if(user.isGender()==true){
                   usergender.setText("Male");
               }
               else{
                   usergender.setText("Female");
               }
               if(user.getProfile_pic_id()!=null){
                   mStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                       @Override
                       public void onSuccess(byte[] bytes) {
                           final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                           DisplayMetrics dm = new DisplayMetrics();
                           getWindowManager().getDefaultDisplay().getMetrics(dm);
                           Accountprofile.setImageBitmap(bm);

                       }
                   });
               } else {
                   Accountprofile.setImageResource(R.drawable.defaultprofile);
               }
               if(user.getCover_pic_id()!=null){
                   mStorageRef.child(user.getCover_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                       @Override
                       public void onSuccess(byte[] bytes) {
                           final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                           DisplayMetrics dm = new DisplayMetrics();
                           getWindowManager().getDefaultDisplay().getMetrics(dm);
                           AccountCover.setImageBitmap(bm);

                       }
                   });
               } else {
                   AccountCover.setImageResource(R.drawable.cover);
               }



           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
   }
   private void IsFriend(final String userID){
       friendsRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Friends fr = dataSnapshot.getValue(Friends.class);
               if (TextUtils.equals(fr.getUserId(), userID)) {
                   String[] friends = fr.getUserFriends().split(",");
                   for (String friend : friends) {
                       if (TextUtils.equals(friend, mAuth.getCurrentUser().getUid())) {
                           Addfriend.setImageResource(R.drawable.friends);
                           addfriendtext.setText("  Friends");
                           Addfriend.setEnabled(false);
                       }
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
   private void IssentRequest(final String userID){
       FriendRequestRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               FriendRequest friendRequest = dataSnapshot.getValue(FriendRequest.class);
               if (TextUtils.equals(friendRequest.getSenderid(), mAuth.getCurrentUser().getUid()) && TextUtils.equals(friendRequest.getRecieverid(), userID) && friendRequest.getState() == 1) {
                   Addfriend.setImageResource(R.drawable.addfriendd);
                   addfriendtext.setText("cancel request");
                   IsExist = true;
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
   private void prepareUpdate(){
        if(mAuth.getCurrentUser()!=null) {
            Accountname.setVisibility(View.GONE);
            editAccountname.setVisibility(View.VISIBLE);
            editAccountname.setText(Accountname.getText().toString());
            userEmail.setVisibility(View.GONE);
            edituseremail.setVisibility(View.VISIBLE);
            edituseremail.setText(userEmail.getText().toString());
            useraddresse.setVisibility(View.GONE);
            Edituseraddresse.setVisibility(View.VISIBLE);
            Edituseraddresse.setText(useraddresse.getText().toString());
            birthday.setImageResource(R.drawable.calender);
            birthday.setEnabled(true);
            btnsubmitupdate.setVisibility(View.VISIBLE);
            btncancelupdate.setVisibility(View.VISIBLE);

        }


 /*Intent updateprofileintent=new Intent(AccountActivity.this,UpdateProfileActivity.class);
 startActivity(updateprofileintent);
 finish();
 */
   }
    private boolean validate(){
        boolean valid=true;
        name=editAccountname.getText().toString().trim();
        email=edituseremail.getText().toString().trim();
        addresse=Edituseraddresse.getText().toString().trim();
        if(name.isEmpty()){
            editAccountname.setError("name is required");
            valid=false;
        }
        if(name.length()>25){
            editAccountname.setError("name can not be that length");
            valid=false;
        }
        if(email.isEmpty()){
            edituseremail.setError("Email is required");
            valid=false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edituseremail.setError("Email format not valid , please enter valid email");
            valid=false;
        }
        if(email.contains(" ")){
            email = email.replaceAll("\\s","");
        }
        if(addresse.isEmpty()){
            Edituseraddresse.setError("address is required");
            valid=false;
        }
        if(userdate.getText().toString().trim().isEmpty()){
            userdate.setError("Date of birth is required");
            valid=false;
        }
        return valid;

    }
 private void updateInfo(){

        if(mAuth.getCurrentUser()!=null) {
            String currentuserId=mAuth.getCurrentUser().getUid();
            mAuth.signOut();
            userRef.child(currentuserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Users user = dataSnapshot.getValue(Users.class);

                    mAuth.signInWithEmailAndPassword(user.getUserEmail(),user.getUserPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            user.setUserEmail(email);
                                            user.setUsername(name);
                                            user.setLocation(addresse);
                                            userRef.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(AccountActivity.this, "  your info update successfully", Toast.LENGTH_LONG).show();
                                                        returntoinfo();
                                                        Addfriend.setEnabled(true);
                                                    } else {
                                                        Toast.makeText(AccountActivity.this, "Error !! "+task.getException(), Toast.LENGTH_LONG).show();

                                                        returntoinfo();
                                                        Addfriend.setEnabled(true);
                                                    }
                                                }
                                            });

                                        }
                                        else{
                                            Toast.makeText(AccountActivity.this, "Error "+task.getException(), Toast.LENGTH_LONG).show();

                                            System.out.println("Error "+task.getException());
                                        }
                                    }
                                });

                            }
                            else{
                                Toast.makeText(AccountActivity.this, "not sign in !! "+task.getException(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
 }
 private void returntoinfo(){
     editAccountname.setVisibility(View.GONE);
     Accountname.setVisibility(View.VISIBLE);
     edituseremail.setVisibility(View.GONE);
     userEmail.setVisibility(View.VISIBLE);
     Edituseraddresse.setVisibility(View.GONE);
     useraddresse.setVisibility(View.VISIBLE);
     birthday.setImageResource(R.drawable.dob);
     birthday.setEnabled(false);
     btnsubmitupdate.setVisibility(View.GONE);
     btncancelupdate.setVisibility(View.GONE);
     GetProfileData(mAuth.getCurrentUser().getUid());


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
