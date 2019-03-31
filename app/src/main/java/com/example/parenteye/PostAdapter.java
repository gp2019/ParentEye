package com.example.parenteye;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<custom_posts_returned> {
    int count=0;
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference userStorageRef= FirebaseStorage.getInstance().getReference("UserImages/");
    private StorageReference postStorageRef= FirebaseStorage.getInstance().getReference("PostImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    ArrayList<custom_posts_returned> post_returnedd;
    public PostAdapter(Activity context, ArrayList<custom_posts_returned> post_returned){

        super(context,0,post_returned);
        post_returnedd=post_returned;

    }

    @Override
    public int getCount() {
        System.out.println("new size is "+post_returnedd.size());
        return post_returnedd.size();

    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View postlist=convertView;

            postlist = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_posts, parent, false);



       final custom_posts_returned post= getItem(position);
        count=count+1;
        TextView postownername=(TextView) postlist.findViewById(R.id.postowner);
        postownername.setText(post.getPost_owner_name());
        TextView postDescription=(TextView) postlist.findViewById(R.id.postDescription);
        if(post.getPost_text()!=null){

            postDescription.setVisibility(View.VISIBLE);


            postDescription.setText(post.getPost_text());

        }
        else {
            postDescription.setVisibility(View.GONE);

        }

        TextView postdate=(TextView) postlist.findViewById(R.id.postdate);
        postdate.setText("1/1/2001");


        final ImageView profileimage=(ImageView) postlist.findViewById(R.id.profile_post);
        final  ImageView postimage=(ImageView) postlist.findViewById(R.id.post_image);

        if(post.hasprofieimage()==true) {
            userRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(TextUtils.equals(dataSnapshot.getKey(),post.getProfile_image())){
                        Users user=dataSnapshot.getValue(Users.class);
                        if(user.getProfile_pic_id()!=null){
                            userStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    profileimage.setImageBitmap(bm);

                                }
                            });
                        }
                        else{
                            profileimage.setImageResource(R.drawable.defaultprofile);
                        }

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
        else{
            profileimage.setImageResource(R.drawable.defaultprofile);

        }
        if(post.haspostimage()==true){
            postimage.setVisibility(View.VISIBLE);
            postStorageRef.child(post.getPost_image()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    postimage.setImageBitmap(bm);

                }
            });


        }
        else{

            // postimage.setImageResource(R.drawable.defaultprofile);
            postimage.setVisibility(View.GONE);
        }




        return postlist;


    }

}
