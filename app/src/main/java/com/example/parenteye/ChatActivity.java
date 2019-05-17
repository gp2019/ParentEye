package com.example.parenteye;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Chat> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mAuth=FirebaseAuth.getInstance();

        activity_main=(RelativeLayout) findViewById(R.id.activity_main);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                Intent FriendIntent=getIntent();
                final String friendId=FriendIntent.getStringExtra(UserFriendsActivity.friendId);
                EditText input=(EditText) findViewById(R.id.input);
                if(!(input.getText().toString().isEmpty())) {
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid()).child(friendId).push().setValue(new Chat(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    FirebaseDatabase.getInstance().getReference().child("Chat").child(friendId).child(mAuth.getCurrentUser().getUid()).push().setValue(new Chat(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));
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

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messagegetime.setText(DateFormat.format("dd/MM/yyyy HH:mm:ss",model.getMessageTime()));
            }
        };
        listofmessage.setAdapter(adapter);

    }
}
