package com.example.parenteye;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Chat> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab,selectImage;
    private CreateTime createTime;
    private TextView friendName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    private StorageReference mStorageRef;
    final long ONE_MEGABYTE = 1024 * 1024;
    private CircleImageView friendProfile;
    private TextView friendstate;

    private static int Gallery_pick = 1;
    private DatabaseReference rootref;
    private ProgressDialog loadingbar;
    private StorageReference massageImageStorageRef;
    private TextView frindName;
    private DatabaseReference userDataBaseRefrance;
    DatabaseReference usersRef = database.getReference("Users");
    FirebaseStorage storage = FirebaseStorage.getInstance();//added for test thing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth=FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
        friendProfile=(CircleImageView)findViewById(R.id.profile_image);
        activity_main=(RelativeLayout) findViewById(R.id.activity_main);
        friendName=(TextView)findViewById(R.id.friendName);
        friendstate=(TextView)findViewById(R.id.friendstate);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        Intent FriendIntent=getIntent();
        final String friendId=FriendIntent.getStringExtra(UserFriendsActivity.friendId);

        selectImage=(FloatingActionButton) findViewById(R.id.selectImage);
        massageImageStorageRef=storage.getInstance().getReference().child("Massage_pic");
        //mStorageRef=FirebaseStorage.getInstance().getReference().child("Users");
        rootref=FirebaseDatabase.getInstance().getReference().child("Chat");
        loadingbar=new ProgressDialog(this);





        //showfriendProfilepicAndname();



        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {

                EditText input=(EditText) findViewById(R.id.input);
                if(!(input.getText().toString().isEmpty())) {
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(friendId).push().setValue(new Chat(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),new getCurrentTime().getDateTime(),"text"));
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(friendId).child(mAuth.getCurrentUser().getUid()).push().setValue(new Chat(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail(),new getCurrentTime().getDateTime(),"text"));
                    input.setText("");
                }
                else{

                }

            }
        });


        //check if not sign in then navigate to sign in page

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent loginIntent=new Intent(ChatActivity.this,LoginActivity.class);
            startActivity(loginIntent);
        }

        else{
            Snackbar.make(activity_main,"Welcome"+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();

            //load content
            displaychatmessage();
        }

        userRef.child(friendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String friendname=dataSnapshot.getValue(Users.class).getUsername();
                friendName.setText(friendname);
                if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                    mStorageRef.child(dataSnapshot.getValue(Users.class).getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            DisplayMetrics dm = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            friendProfile.setImageBitmap(bm);
                        }
                    });
                }
                String state=dataSnapshot.getValue(Users.class).getState();
                if(TextUtils.equals(state,"1")){
                    friendstate.setText("Online");
                }
                else{
                    friendstate.setText("Offline");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void displaychatmessage()
    {

        Intent FriendIntent=getIntent();
        final String friendId=FriendIntent.getStringExtra(UserFriendsActivity.friendId);
        ListView listofmessage= (ListView) findViewById(R.id.list_of_message);
        adapter=new FirebaseListAdapter<Chat>(this,Chat.class,R.layout.list_item,
                FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(friendId))
        {
            @Override
            protected void  populateView(View v, Chat model, int postion) {
                //get references to the views of list_item.xml
                TextView messageText,messageUser,messagegetime;

                ImageView messageImage;
                CircleImageView user_profile_image;
                //     String fromUserId=model.getMessageUser();
                //String messageType=model.getmessageType();
                String messageType=model.getMessageType();
                //  messageType = FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(friendId).child("Type"))

                messageImage=(ImageView) v.findViewById(R.id.massageImageView);

                messageText=(TextView) v.findViewById(R.id.message_text);
                messageUser=(TextView) v.findViewById(R.id.message_user);
                messagegetime=(TextView) v.findViewById(R.id.message_time);
                String timePuplisher =model.getTime();
                createTime =new CreateTime(timePuplisher);
                try {
                    createTime.sdf();
                } catch (ParseException e) {
                    e.printStackTrace();
                }



                if( messageType.equals("text")) {
                    messageImage.setVisibility(v.INVISIBLE);
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    // messagegetime.setText(DateFormat.format("dd/MM/yyyy HH:mm:ss",model.getMessageTime()));

                } else
                {

                    messageText.setVisibility(v.INVISIBLE);
                    messageText.setPadding(0,0,0,0);

                    String image=model.getMessageText();
                    Picasso.get().load(image).into(messageImage);
                }



                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                //    messagegetime.setText(DateFormat.format("dd/MM/yyyy HH:mm:ss",model.getMessageTime()));
                messagegetime.setText(createTime.calculateTime());
            }
        };
        listofmessage.setAdapter(adapter);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryintent =new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,Gallery_pick);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Gallery_pick && resultCode == RESULT_OK && data!=null){

            loadingbar.setTitle("Sending Chat Image");
            loadingbar.setMessage("Please Wait ,While Your Chat Massage Is Sending ...  ");
            final EditText input=(EditText) findViewById(R.id.input);

            Uri imageUri =data.getData();
            Intent FriendIntent=getIntent();

            String friendId=FriendIntent.getStringExtra(UserFriendsActivity.friendId);
            final String userId=mAuth.getCurrentUser().getUid();

         /*   final String massageSenderRef="Massage/"+friendId+"/"+userId;
            final String massageReciverRef="Massage/"+userId+"/"+friendId;*/

            final String massageReciverRef=friendId+"/"+userId;
            final String massageSenderRef=userId+"/"+friendId;

            //  FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(friendId).push();
            DatabaseReference user_massage_key=FirebaseDatabase.getInstance().getReference().child("Chat").child(friendId).child(mAuth.getCurrentUser().getUid()).push();

            final String massage_push_id=user_massage_key.getKey();
            StorageReference filePath=massageImageStorageRef.child(massage_push_id +".jpg");
            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){


                        final String downloadUrl=task.getResult().getMetadata().getReference().getDownloadUrl().toString();

                        Map MassageTextBody=new HashMap();
                        MassageTextBody.put("messageText",downloadUrl);
                        //  MassageTextBody.put("seen",false);
                        MassageTextBody.put("messageType","image");
                        MassageTextBody.put("time", ServerValue.TIMESTAMP);
                        MassageTextBody.put("messageUser",userId);

                        Map MassageBodyDetails=new HashMap();
                        MassageBodyDetails.put(massageSenderRef+"/"+massage_push_id,MassageTextBody);
                        MassageBodyDetails.put(massageReciverRef+"/"+massage_push_id,MassageTextBody);


                        rootref.updateChildren(MassageBodyDetails, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                if(databaseError !=null){
                                    Log.d("Chat_log",databaseError.getMessage().toString());
                                }
                                input.setText("");
                                loadingbar.dismiss();

                            }
                        });


                        Toast.makeText(ChatActivity.this, "Pictue sent succesfully   ", Toast.LENGTH_SHORT).show();

                        loadingbar.dismiss();
                    }
                    else {
                        Toast.makeText(ChatActivity.this, "picture not sent try again  ", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }

                }
            });




        }
    }

    /*   public  void showfriendProfilepicAndname(){
        Intent FriendIntent=getIntent();
        final String friendId=FriendIntent.getStringExtra(UserFriendsActivity.friendId);
        usersRef.child(friendId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);


                frindName.setText(user.getUsername());
                if (user.getProfile_pic_id()!=null) {

                    String profileImageId=user.getProfile_pic_id();
                    mStorageRef.child(profileImageId).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            profileImage.setImageBitmap(bm);

                        }
                    });
                } else if(user.isGender()==true) {
                    profileImage.setImageResource(R.drawable.profile_boys);

                }
                else {
                    profileImage.setImageResource(R.drawable.profile_giles);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

}