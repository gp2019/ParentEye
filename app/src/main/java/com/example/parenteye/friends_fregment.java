package com.example.parenteye;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class friends_fregment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Friendsref = database.getReference("Friends");
    DatabaseReference userref = database.getReference("Users");
    ArrayList<Users> curremtuserfriends=new ArrayList<Users>();
    private ListView friendsList;
    public static final String friendId="Friend_Id";
    public friends_fregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fregment_user_friends, container, false);
        mAuth=FirebaseAuth.getInstance();
        friendsList=(ListView)view.findViewById(R.id.friendsList);

        GetMyFriends();

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users friend=curremtuserfriends.get(position);
                Intent chatIntent=new Intent(getActivity(),ChatActivity.class);
                chatIntent.putExtra(friendId,friend.getUserId());
                startActivity(chatIntent);
            }
        });


        return view;




    }
    private void GetMyFriends(){
        final ArrayList<String> friends_arraylist=new ArrayList<String>();
        final User_Chat_Adapter adapter=new User_Chat_Adapter(getActivity(),curremtuserfriends);
        friendsList.setAdapter(adapter);

        if(mAuth.getCurrentUser()!=null){



            Friendsref.child(mAuth.getCurrentUser().getUid()).child("userFriends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null) {
                        String userfriends = dataSnapshot.getValue(String.class);
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
                                        System.out.println("inside friends fragment");
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
