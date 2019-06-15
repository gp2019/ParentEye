package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Chat> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab;
    private CreateTime createTime;
    private TextView friendName;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    private StorageReference mStorageRef;
    final long ONE_MEGABYTE = 1024 * 1024;
    private CircleImageView friendProfile;
    private TextView friendstate;
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


                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
            //    messagegetime.setText(DateFormat.format("dd/MM/yyyy HH:mm:ss",model.getMessageTime()));
                messagegetime.setText(createTime.calculateTime());
            }
        };
        listofmessage.setAdapter(adapter);

    }
}
