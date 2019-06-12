package com.example.parenteye;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends Activity {
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
    private Dialog updateprofileDialoge;
    private  TextView txtclose;
    private Button btnsubmitpass;
    private EditText submitpassword;
    private EditText edituseremail;
    private EditText editAccountname;
    private EditText Edituseraddresse;
    private Button btnsubmitupdate;
    private Button btncancelupdate;
    private  String name;
    private  String email;
    private String addresse;
    private ImageView gallery;
    private TextView updatetextclose;
    private Spinner spinner1;
    private Dialog AccountfriendsDialog;
    private  TextView AccountfriendsDialogtxtclose;
    private  ListView AccountfriendsDialogList;
    private CreateTime createTime;
    String[] cities = new String[]{"Cairo", "Alexandria", "Giza","Port Said","Suez","Luxor","al-Mansura","El-Mahalla El-Kubra","Tanta","Asyut",
            "tIsmailia","Fayyum","Zagazig"," Aswan","Damietta","Damanhur","al-Minya","Beni Suef"," Qena","Sohag","Hurghada","6th of October City","Shibin El Kom",
            "Banha"," Kafr el-Sheikh","Arish","Mallawi","10th of Ramadan City","Bilbais","Marsa Matruh","Idfu","Mit Ghamr","Al-Hamidiyya","Desouk",
            "Qalyub","Abu Kabir","Kafr el-Dawwar","Girga","Akhmim","Matareya"};
    private  ProgressDialog progressdialogue;
    private ImageView friends_chat;
    ArrayList<Users> AccountFriends=new ArrayList<Users>();
    public static final String Account_ID="Account_ID";
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
        updateprofileDialoge=new Dialog(this);
        updateprofileDialoge.setContentView(R.layout.updateprofilepopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        updatetextclose=(TextView) updateprofileDialoge.findViewById(R.id.txtclose);
        submitpassword=(EditText)myDialog.findViewById(R.id.passwordfield);
        btnsubmitpass = (Button)myDialog. findViewById(R.id.btnsubmitpassword);
        edituseremail=(EditText) updateprofileDialoge.findViewById(R.id.EdituserEmail);

        editAccountname=(EditText) updateprofileDialoge.findViewById(R.id.editAccountname);

        btnsubmitupdate=(Button) updateprofileDialoge. findViewById(R.id.btnsubmitupdate);
        btncancelupdate=(Button) updateprofileDialoge.findViewById(R.id.btncancelupdate);
        gallery=(ImageView) findViewById(R.id.gallery);
        spinner1=(Spinner)  updateprofileDialoge.findViewById(R.id.spinner1);
        friends_chat=(ImageView)findViewById(R.id.friends_chat);
        AccountfriendsDialog=new Dialog(this);
        AccountfriendsDialog.setContentView(R.layout.account_friends_popup);
        AccountfriendsDialogtxtclose =(TextView) AccountfriendsDialog.findViewById(R.id.txtclose);
        AccountfriendsDialogList=(ListView) AccountfriendsDialog.findViewById(R.id.accout_friends);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);

        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city=(String)parent.getItemAtPosition(position);
                addresse="Egypt/"+city;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                addresse="Egypt";
            }
        });
        Addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
              final   String userId = intent.getStringExtra("searched_user_Id");
               //final String userId="OjEqHKzicMOZkAzjl5OLxBcIpai2";
                if(TextUtils.equals(mAuth.getCurrentUser().getUid(),userId)){
                    //here go updating profile


                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissProgressDialog();
                            myDialog.dismiss();
                        }
                    });
                    updatetextclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateprofileDialoge.dismiss();
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
                                            putuserdatainupdateform();
                                            updateprofileDialoge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            updateprofileDialoge.show();

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
                                        Notifications notifi =new Notifications();
                                        notifi.DeleteNotificationOfRejectOrAcceptFriendRequest(userId);
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
                    showDialogue();
                    updateInfo();
                }

            }
        });

        btncancelupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissProgressDialog();
                updateprofileDialoge.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                  final   String userID = intent.getStringExtra("searched_user_Id");
                Intent galleryIntent=new Intent(AccountActivity.this,GalleryActivity.class);
                galleryIntent.putExtra(Account_ID,userID);
                startActivity(galleryIntent);
                finish();
            }
        });

        friends_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
               final   String userID = intent.getStringExtra("searched_user_Id");
                showAccountFriends(userID);

            }
        });
        AccountfriendsDialogtxtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountfriendsDialog.dismiss();
            }
        });
    }



    @Override
    protected void onStart() {
        ViewUserProfile();


        super.onStart();
    }

    private void ViewUserProfile(){
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = getIntent();
            final   String userID = intent.getStringExtra("searched_user_Id");
           // final String userID = "OjEqHKzicMOZkAzjl5OLxBcIpai2";
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

            GetProfilePosts();

            // GetProfilePosts();

        }
    }


    private void GetProfilePosts()
    {

        final ProfilePostAdapter postadapterr=new ProfilePostAdapter(AccountActivity.this,Profile_posts);
        Post_listview.setAdapter(postadapterr);

        Intent intent = getIntent();
        final   String profileId = intent.getStringExtra("searched_user_Id");
        //final String username="aya";
       // final String profileId="OjEqHKzicMOZkAzjl5OLxBcIpai2";
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
                        custom.setCountComment(newpost.getCountComment());
                        custom.setCountLike(newpost.getCountLike());
                        String timePuplisher =newpost.getPostdate();
                        createTime =new CreateTime(timePuplisher);
                        try {
                            createTime.sdf();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        custom.setPost_date(createTime.calculateTime());
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
      friendsRef.child(userID).child("userFriends").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.getValue(String.class)!=null){
                  String friends=dataSnapshot.getValue(String.class);
                  String[] arrayfriends = friends.split(",");
                  for (String friend : arrayfriends) {
                      if (TextUtils.equals(friend, mAuth.getCurrentUser().getUid())) {
                          Addfriend.setImageResource(R.drawable.friends);
                          addfriendtext.setText("  Friends");
                          Addfriend.setEnabled(false);
                      }
                  }
              }
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
                else if(TextUtils.equals(friendRequest.getSenderid(),userID) && TextUtils.equals(friendRequest.getRecieverid(), mAuth.getCurrentUser().getUid()) && friendRequest.getState() == 1) {
                    Addfriend.setImageResource(R.drawable.addfriendd);
                    addfriendtext.setText("pending");
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
    }
    private boolean validate(){
        boolean valid=true;
        name=editAccountname.getText().toString().trim();
        email=edituseremail.getText().toString().trim();
        //  addresse=Edituseraddresse.getText().toString().trim();
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

       /* if(addresse.isEmpty()){
            Edituseraddresse.setError("address is required");
            valid=false;
        }*/
        if(addresse==null){
            Toast.makeText(AccountActivity.this,"address is required",Toast.LENGTH_LONG).show();
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
                                                        dismissProgressDialog();
                                                        updateprofileDialoge.dismiss();
                                                        Toast.makeText(AccountActivity.this, "  your info update successfully", Toast.LENGTH_LONG).show();


                                                    } else {
                                                        dismissProgressDialog();
                                                        Toast.makeText(AccountActivity.this, "Error !! "+task.getException(), Toast.LENGTH_LONG).show();
                                                        updateprofileDialoge.dismiss();

                                                    }
                                                }
                                            });

                                        }
                                        else{
                                            dismissProgressDialog();
                                            Toast.makeText(AccountActivity.this, "Error "+task.getException(), Toast.LENGTH_LONG).show();

                                            System.out.println("Error "+task.getException());
                                        }
                                    }
                                });

                            }
                            else{
                                dismissProgressDialog();
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
    private void putuserdatainupdateform(){
        editAccountname.setText(Accountname.getText().toString());
        edituseremail.setText(userEmail.getText().toString());
        //  Edituseraddresse.setText(useraddresse.getText().toString());
    }
    private void showDialogue(){
        if(progressdialogue==null){
            progressdialogue=new ProgressDialog(this);
            progressdialogue.setTitle("loading...");
            progressdialogue.setMessage("please wait...");

        }
        progressdialogue.show();
    }
    private void dismissProgressDialog() {
        if (progressdialogue != null && progressdialogue.isShowing()) {
            progressdialogue.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    private void showAccountFriends(String Account_Owner_ID) {
        if (mAuth.getCurrentUser() != null) {
            final ArrayList<String> friends_arraylist=new ArrayList<String>();
           final  User_Chat_Adapter friendsAdapter = new User_Chat_Adapter(AccountActivity.this, AccountFriends);
            AccountfriendsDialogList.setAdapter(friendsAdapter);
            friendsRef.child(Account_Owner_ID).child("userFriends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null) {
                        String userfriends = dataSnapshot.getValue(String.class);
                        String[] friends = userfriends.split(",");
                        for (String id : friends) {
                            friends_arraylist.add(id);
                        }
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AccountFriends.clear();
                                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                                    if (friends_arraylist.contains(usersnapshot.getKey())) {
                                        Users user = new Users();
                                        user.setUsername(usersnapshot.getValue(Users.class).getUsername());
                                        user.setUserId(usersnapshot.getKey());
                                        if (usersnapshot.getValue(Users.class).getProfile_pic_id() != null) {
                                            user.setProfile_pic_id(usersnapshot.getValue(Users.class).getProfile_pic_id());
                                        }
                                        AccountFriends.add(user);
                                        friendsAdapter.notifyDataSetChanged();

                                    }
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

            AccountfriendsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            AccountfriendsDialog.show();

        }
    }



}
