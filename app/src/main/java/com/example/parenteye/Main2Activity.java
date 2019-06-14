package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity {
    private Button search;
    private CircleImageView profileImage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    final long ONE_MEGABYTE = 1024 * 1024;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;


    Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem home;
    private TabItem notification;
    private TabItem friendRequest;
    private TabItem sideMenu;
    private ViewPager viewPager;
    PagerController mPagerController;

    private Button ic_sideMenu;
    private Button Main2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");

        Main2 =(Button)findViewById(R.id.Main2) ;

        Main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sideMenuPage=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(sideMenuPage);
                finish();
            }
        });






        ic_sideMenu =(Button)findViewById(R.id.SideMenuIcon) ;


        ic_sideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sideMenuPage=new Intent(Main2Activity.this,side_menu_bar.class);
                startActivity(sideMenuPage);
                finish();
            }
        });


        search = (Button) findViewById(R.id.tvSearchBar);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchintent=new Intent(Main2Activity.this,SearchActivity.class);
                startActivity(searchintent);
            }
        });


        profileImage=(CircleImageView)findViewById(R.id.profile_image);

        showProfilepic();
        tabLayout=findViewById(R.id.tablayout);
        home=findViewById(R.id.Home);
        notification=findViewById(R.id.notification);
        friendRequest=findViewById(R.id.friendRequests);
        sideMenu=findViewById(R.id.sideMenu);
        viewPager=findViewById(R.id.viewpager);
        mPagerController =new PagerController(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(mPagerController);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }



    public  void showProfilepic(){
        myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);


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
    }
}