package com.example.parenteye;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.google.android.gms.flags.FlagSource.G;

public class GalleryActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postref = database.getReference("Posts");
    DatabaseReference userRef = database.getReference("Users");
    ArrayList<Users> photos=new ArrayList<Users>();
   private ListView gallery_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mAuth = FirebaseAuth.getInstance();
        gallery_list=(ListView) findViewById(R.id.gallery_list);
        GetUsergallery();
    }

    private void GetUsergallery(){
       final GalleryAdapter galleryadapter=new GalleryAdapter(GalleryActivity.this,photos);
        gallery_list.setAdapter(galleryadapter);

       // final String userId="cR6RdBeU5Lg7CEFLhEniBT16ZxM2";
        Intent intent = getIntent();
        final   String userId = intent.getStringExtra("Account_ID");
        if(mAuth.getCurrentUser()!=null){

                    postref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            photos.clear();
                         for(DataSnapshot postsnapshot:dataSnapshot.getChildren()) {
                             if (TextUtils.equals(postsnapshot.getValue(Posts.class).getUserId(), userId)) {
                                 if (postsnapshot.getValue(Posts.class).isHasimage() == true) {
                                     Users photo1 = new Users();
                                     photo1.setRoleId("2");
                                     photo1.setProfile_pic_id(postsnapshot.getValue(Posts.class).getImageId());
                                     photos.add(photo1);
                                 }
                             }
                         }
                             userRef.child(userId).addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                                         Users photo2 = new Users();
                                         photo2.setRoleId("1");
                                         photo2.setProfile_pic_id(dataSnapshot.getValue(Users.class).getProfile_pic_id());
                                         photos.add(photo2);
                                     }
                                     if(dataSnapshot.getValue(Users.class).getCover_pic_id()!=null){
                                         Users photo3 = new Users();
                                         photo3.setRoleId("1");
                                         photo3.setCover_pic_id(dataSnapshot.getValue(Users.class).getCover_pic_id());
                                         photos.add(photo3);
                                     }

                                     Collections.reverse(photos);
                                     galleryadapter.notifyDataSetChanged();

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
