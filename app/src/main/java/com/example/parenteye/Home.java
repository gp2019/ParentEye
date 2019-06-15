package com.example.parenteye;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;


/*
fragment
 */
public class Home extends Fragment {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    DatabaseReference myRef2 = database.getReference("Posts");
    DatabaseReference myRef3 = database.getReference("Friends");
    DatabaseReference memberRef = database.getReference("Members");
    DatabaseReference CommunityRef = database.getReference("Community");
    private ListView Post_listview;
    private ArrayList<Posts> myposts=new ArrayList<Posts>();
    private CreateTime createTime;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);


        Post_listview=(ListView) view.findViewById(R.id.Post_listview);
        GetMyHomePosts();
        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

    }

    private void GetMyHomePosts(){
        if(mAuth.getCurrentUser()!=null){

            final HomePostsAdapter postadapter=new HomePostsAdapter(getActivity(),myposts);
            Post_listview.setAdapter(postadapter);
            final ArrayList<String> communityIds=new ArrayList<String>();
            final ArrayList<String> friendsList=new ArrayList<String>();

            myRef3.child(mAuth.getCurrentUser().getUid()).child("userFriends").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null) {
                        String myfriends = dataSnapshot.getValue(String.class);
                        final String[] myFriendsID = myfriends.split(",");
                        for(String id:myFriendsID){
                            friendsList.add(id);
                            System.out.println(

                            );
                        } }
                    memberRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot membersnaphot:dataSnapshot.getChildren()){
                                if(TextUtils.equals(membersnaphot.getValue(Members.class).getUserId(),mAuth.getCurrentUser().getUid())){
                                    communityIds.add(membersnaphot.getValue(Members.class).getCommunityid());
                                }
                            }
                          /*  if (dataSnapshot.getValue(Members.class)!=null) {
                                for (DataSnapshot membersnapshot : dataSnapshot.getChildren()) {
                                    Members member = membersnapshot.getValue(Members.class);
                                    communityIds.add(member.getCommunityid());
                                    //System.out.println(member.getCommunityid());
                                }
                            }*/
                            CommunityRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot comsnaphot:dataSnapshot.getChildren()){
                                        if(TextUtils.equals(comsnaphot.getValue(Community.class).getAdminId(),mAuth.getCurrentUser().getUid())){
                                            communityIds.add(comsnaphot.getKey());
                                            System.out.println("Helooooooooooooooooooooooooooooooooooooo"+comsnaphot.getKey());
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            myRef2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    myposts.clear();
                                    for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                        Posts post = postsnapshot.getValue(Posts.class);
                                        if (friendsList.contains(post.getUserId())&&TextUtils.equals(post.getPlaceTypeId(),"1")|| communityIds.contains(post.getPlaceId())) {
                                            String timePuplisher =post.getPostdate();
                                            createTime =new CreateTime(timePuplisher);
                                            try {
                                                createTime.sdf();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            post.setPostdate(createTime.calculateTime());
                                            myposts.add(post);
                                        }
                                    }
                                    Collections.reverse(myposts);
                                    postadapter.notifyDataSetChanged();
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

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }
    }

}
