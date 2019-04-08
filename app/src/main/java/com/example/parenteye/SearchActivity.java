package com.example.parenteye;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private ImageButton search_friend_buttom,search_community_buttom,arrow_back;

    //declaring variables
    private ListView listview;
    private EditText search_box_input;

    private ArrayList<Users> list;
    private  ArrayAdapter<Users> adapter;
    List<Users> userlist;

    private DatabaseReference userRef,communityRef;


    private ArrayList<Community> listcom;
    private  ArrayAdapter<Community> adapterCommunity;
    List<Community>communityList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        arrow_back=(ImageButton)findViewById(R.id.arrow_back);
        search_friend_buttom= (ImageButton) findViewById(R.id.search_friend_buttom);
        search_community_buttom= (ImageButton) findViewById(R.id.search_community_buttom);
        listview=(ListView)findViewById(R.id.search_listview);
        search_box_input= (EditText) findViewById(R.id.search_box_input);


        userRef=FirebaseDatabase.getInstance().getReference("Users");
        communityRef=FirebaseDatabase.getInstance().getReference("Community");

        userlist=new ArrayList<>();
        communityList=new ArrayList<>();

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(SearchActivity.this,MainActivity.class);
                startActivity(back);
                //finish();
            }
        });


//When search on friend

        search_community_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communityList.clear();
                communityRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String  search_input_text =search_box_input.getText().toString();
                        if(! search_input_text.isEmpty()) {

                            for (DataSnapshot ds:dataSnapshot.getChildren()){

                                Community comunity=ds.getValue(Community.class);
                                String communityName=comunity.getCommunityname();
                                if (communityName.toLowerCase().contains(search_input_text.toLowerCase())) {
                                    communityList.add(comunity);

                                }

                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"You Must Enter The Right Community Name",Toast.LENGTH_SHORT).show();
                            communityList.clear();
                        }

                        if(! communityList.isEmpty()){
                            searchCommunityAdapter searchCommunityAdapter = new searchCommunityAdapter(SearchActivity.this, communityList);
                            listview.setAdapter(searchCommunityAdapter);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"You Must Enter The Right Community Name",Toast.LENGTH_SHORT).show();
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });



        search_friend_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userlist.clear();
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String  search_input_text =search_box_input.getText().toString();
                        if(! search_input_text.isEmpty()) {

                            for (DataSnapshot ds:dataSnapshot.getChildren()){

                                Users users=ds.getValue(Users.class);
                                String username=users.getUsername();
                                if (username.toLowerCase().contains(search_input_text.toLowerCase())) {
                                    userlist.add(users);

                                }

                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"You Must Enter The Right User Name",Toast.LENGTH_SHORT).show();

                            userlist.clear();
                        }
                        if (! userlist.isEmpty()){
                            searchFriendAdapter searchFriendAdapter=new searchFriendAdapter(SearchActivity.this,userlist);
                            listview.setAdapter(searchFriendAdapter);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"You Must Enter The Right User Name",Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


        //////When search on group or page



    }



}
