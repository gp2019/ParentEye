package com.example.parenteye;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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

    private FloatingActionButton search_friend_buttom,search_community_buttom;

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
    public static final String searched_user_Id="searched_user_Id";
    public static final String searched_page_Id="searched_page_Id";
    public static final String searched_group_Id="searched_group_Id";
    public static final String searched_Item_name="searched_Item_name";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        search_friend_buttom= (FloatingActionButton) findViewById(R.id.search_friend_buttom);
        search_community_buttom= (FloatingActionButton) findViewById(R.id.search_community_buttom);
        listview=(ListView)findViewById(R.id.search_listview);
        search_box_input= (EditText) findViewById(R.id.search_box_input);


        userRef=FirebaseDatabase.getInstance().getReference("Users");
        communityRef=FirebaseDatabase.getInstance().getReference("Community");

        userlist=new ArrayList<>();
        communityList=new ArrayList<>();






//When search on friend

        search_community_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communityList.clear();
                userlist.clear(); //test
                communityRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String  search_input_text =search_box_input.getText().toString();
                        if(! search_input_text.isEmpty()) {

                            for (DataSnapshot ds:dataSnapshot.getChildren()){

                                Community comunity=ds.getValue(Community.class);
                                String communityName=comunity.getCommunityname();
                                comunity.setCommunityId(ds.getKey()); //getting the key from firebase and set to the id
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
                communityList.clear(); //testing
                userlist.clear();
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String  search_input_text =search_box_input.getText().toString();
                        if(! search_input_text.isEmpty()) {

                            for (DataSnapshot ds:dataSnapshot.getChildren()){

                                Users users=ds.getValue(Users.class);
                                String username=users.getUsername();
                                users.setUserId(ds.getKey()); //getting the key from firebase and set to the id
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








        //making the Intent to transfer to another activity

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("clicked "+id);

           if(userlist.get(position)!=null){
                  Users searched_user=userlist.get(position);
                  Intent searched_intent=new Intent(getApplicationContext(),AccountActivity.class);
                  searched_intent.putExtra(searched_user_Id,searched_user.getUserId());
                  startActivity(searched_intent);

                }




                /*  else {

                 Community searched_comm=communityList.get(position);
                  System.out.println("enter  community "+searched_comm.getCommunityId());
                  if(TextUtils.equals(searched_comm.getTypeid(),"1")){
                      Intent searched_intent=new Intent(getApplicationContext(),GroupActivity.class);
                      searched_intent.putExtra(searched_group_Id,searched_comm.getCommunityId());
                      searched_intent.putExtra(searched_Item_name,searched_comm.getCommunityname());
                      System.out.println("group id "+searched_comm.getCommunityId());
                      startActivity(searched_intent);
                  }
                  else if(TextUtils.equals(searched_comm.getTypeid(),"2")){
                      Intent searched_intent=new Intent(getApplicationContext(),PageActivity.class);
                      searched_intent.putExtra(searched_page_Id,searched_comm.getCommunityId());
                      System.out.println("page id "+searched_comm.getCommunityId());
                      searched_intent.putExtra(searched_Item_name,searched_comm.getCommunityname());
                      startActivity(searched_intent);
                  }

              }
*/
            }
        });


    }



}
