package com.example.parenteye;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class SpecificPostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference postref = database.getReference("Posts");
    DatabaseReference userRef = database.getReference("Users");
    private CircleImageView profile_post;
    private TextView postowner;
    private TextView postdate;
    private TextView postDescription;
    private ImageView post_image;
    private StorageReference mStorageRef;
    final long ONE_MEGABYTE = 1024 * 1024;
    private CreateTime createTime;
    private StorageReference mStorageRef2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_posts);
        IntializeVariables();
        GetSpecificPost();
    }



    private void IntializeVariables(){
        mAuth = FirebaseAuth.getInstance();
        profile_post=(CircleImageView)findViewById(R.id.profile_post);
        postowner=(TextView)findViewById(R.id.postowner);
        postdate=(TextView)findViewById(R.id.postdate);
        postDescription=(TextView)findViewById(R.id.postDescription);
        post_image=(ImageView)findViewById(R.id.post_image);
        mStorageRef = FirebaseStorage.getInstance().getReference("PostImages/");
        mStorageRef2 = FirebaseStorage.getInstance().getReference("UserImages/");
    }
    private void GetSpecificPost(){
      final   String PostID="-LhHb-BqXhirwpH0blPd";
        postref.child(PostID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Posts post=dataSnapshot.getValue(Posts.class);
                if(post.isHasimage()==true){
                    mStorageRef.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            DisplayMetrics dm = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(dm);
                            post_image.setImageBitmap(bm);
                        }
                    });
                }
                else{
                    post_image.setVisibility(View.GONE);
                }if(post.getPostcontent()!=null){
                    postDescription.setText(post.getPostcontent());
                }

                String timePuplisher =post.getPostdate();
                createTime =new CreateTime(timePuplisher);
                try {
                    createTime.sdf();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                postdate.setText(createTime.calculateTime());

                userRef.child(post.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                            mStorageRef2.child(dataSnapshot.getValue(Users.class).getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    final Bitmap bm2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    DisplayMetrics dm2 = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(dm2);
                                    profile_post.setImageBitmap(bm2);
                                }
                            });
                        }
                        postowner.setText(dataSnapshot.getValue(Users.class).getUsername());

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
