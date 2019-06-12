package com.example.parenteye;

import android.app.Dialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CommunityAboutandmembersActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference CommunityRef = database.getReference("Community");
    DatabaseReference membersRef = database.getReference("Members");
    DatabaseReference userRef = database.getReference("Users");
    private TextView CommAbout;
    private TextView adminname;
    private ListView Comm_members;
    private ArrayList<Users> Members=new ArrayList<Users>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_aboutandmembers);
        IntializeVariables();

    }
    private void IntializeVariables(){
        mAuth=FirebaseAuth.getInstance();
        CommAbout=(TextView)findViewById(R.id.commabout);
        adminname=(TextView) findViewById(R.id.adminname);
        Comm_members=(ListView)findViewById(R.id.comm_members);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getCommunitydetails();
    }
 /*   @Override
    public void onBackPressed() {
        PageActivity page=new PageActivity();
        page.getPageInfo();
    }
    */
    private void getCommunitydetails(){
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = getIntent();
            String CommID = intent.getStringExtra("pageID");
            CommunityRef.child(CommID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String about = dataSnapshot.getValue(Community.class).getCommunityAbout();
                    CommAbout.setText(about);
                    String adminId = dataSnapshot.getValue(Community.class).getAdminId();

                    if(TextUtils.equals(mAuth.getCurrentUser().getUid(),adminId)){
                        adminname.setText("you are the Admin");
                    }
                    else {
                        userRef.child(adminId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String Adminname = dataSnapshot.getValue(Users.class).getUsername();
                                adminname.setText(Adminname + " is Admin");
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
            getCommunitymembers();
        }
    }

    private void getCommunitymembers(){
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = getIntent();
           final String CommID = intent.getStringExtra("pageID");
            final MemberAdapter memberAdapter=new MemberAdapter(CommunityAboutandmembersActivity.this,Members);
            Comm_members.setAdapter(memberAdapter);
            membersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Members.clear();
                    memberAdapter.notifyDataSetChanged();
                    for(DataSnapshot Memnapshot:dataSnapshot.getChildren()){
                  Members member=Memnapshot.getValue(Members.class);
                        final    String memberKey=Memnapshot.getKey();
                        if(TextUtils.equals(member.getCommunityid(),CommID)){
                        userRef.child(member.getUserId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               final Users user=new Users();
                                user.setUsername(dataSnapshot.getValue(Users.class).getUsername());
                                user.setRoleId(memberKey); //here Role id represent key of the member record
                                if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                                    user.setProfile_pic_id(dataSnapshot.getValue(Users.class).getProfile_pic_id());
                                }
                                CommunityRef.child(CommID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user.setUserId(dataSnapshot.getValue(Community.class).getAdminId()); //user id represent admin id
                                        Members.add(user);
                                        memberAdapter.notifyDataSetChanged();
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
