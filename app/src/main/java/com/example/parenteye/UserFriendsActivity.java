package com.example.parenteye;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFriendsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Friendsref = database.getReference("Friends");
    DatabaseReference userref = database.getReference("Users");
    ArrayList<Users> curremtuserfriends=new ArrayList<Users>();
    private ListView friendsList;
    public static final String friendId="Friend_Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends);
        mAuth=FirebaseAuth.getInstance();
        friendsList=(ListView)findViewById(R.id.friendsList);



        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users friend=curremtuserfriends.get(position);
                Intent chatIntent=new Intent(UserFriendsActivity.this,ChatActivity.class);
                chatIntent.putExtra(friendId,friend.getUserId());
                startActivity(chatIntent);
            }
        });

    }



    @Override
    protected void onStart() {
        GetMyFriends();
        super.onStart();
    }

    private void GetMyFriends(){
        final ArrayList<String> friends_arraylist=new ArrayList<String>();
        final ChildAdapter adapter=new ChildAdapter(UserFriendsActivity.this,curremtuserfriends);
        friendsList.setAdapter(adapter);

        if(mAuth.getCurrentUser()!=null){



            Friendsref.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(Friends.class)!=null) {
                        String userfriends = dataSnapshot.getValue(Friends.class).getUserFriends();
                        String[] friends = userfriends.split(",");
                        for (String id : friends) {
                            friends_arraylist.add(id);
                        }
                        userref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                curremtuserfriends.clear();
                                for (DataSnapshot usersnapshot : dataSnapshot.getChildren()) {
                                    if (friends_arraylist.contains(usersnapshot.getKey())) {
                                        Users user = new Users();
                                        user.setUsername(usersnapshot.getValue(Users.class).getUsername());
                                        user.setUserId(usersnapshot.getKey());
                                        if (usersnapshot.getValue(Users.class).getProfile_pic_id() != null) {
                                            user.setProfile_pic_id(usersnapshot.getValue(Users.class).getProfile_pic_id());
                                        }
                                        curremtuserfriends.add(user);
                                        adapter.notifyDataSetChanged();

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
        }
    }
}
