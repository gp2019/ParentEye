package com.example.parenteye;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomePostsAdapter extends ArrayAdapter<Posts> {
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference comStorageRef= FirebaseStorage.getInstance().getReference("PageImages/");
    private StorageReference postStorageRef= FirebaseStorage.getInstance().getReference("PostImages/");
   private StorageReference userStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference comRef = database.getReference("Community");
    ArrayList<Posts> post_returnedd;
    DatabaseReference userRef = database.getReference("Users");



    public HomePostsAdapter(Activity context, ArrayList<Posts> post_returned){

        super(context,0,post_returned);
        post_returnedd=post_returned;

    }



    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View postlist=convertView;

        postlist = LayoutInflater.from(getContext()).inflate(
                R.layout.list_posts, parent, false);

        final Posts post= getItem(position);
        final TextView postownername=(TextView) postlist.findViewById(R.id.postowner);
        TextView postDescription=(TextView) postlist.findViewById(R.id.postDescription);
        final ImageView profileimage=(ImageView) postlist.findViewById(R.id.profile_post);
        final  ImageView postimage=(ImageView) postlist.findViewById(R.id.post_image);
        TextView postdate=(TextView) postlist.findViewById(R.id.postdate);
        postdate.setText("1/1/2001");

       if(TextUtils.equals(post.getPlaceTypeId(),"1")){
           userRef.child(post.getUserId()).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   postownername.setText(dataSnapshot.getValue(Users.class).getUsername());
                   if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                       userStorageRef.child(dataSnapshot.getValue(Users.class).getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                           @Override
                           public void onSuccess(byte[] bytes) {
                               final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               profileimage.setImageBitmap(bm);

                           }
                       });
                   }
                   else{
                       profileimage.setImageResource(R.drawable.user);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }

       else if(TextUtils.equals(post.getPlaceTypeId(),"2")){
           comRef.child(post.getPlaceId()).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  postownername.setText(dataSnapshot.getValue(Community.class).getCommunityname());
                  if(dataSnapshot.getValue(Community.class).getPhotoId()!=null){
                       comStorageRef.child(dataSnapshot.getValue(Community.class).getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                           @Override
                           public void onSuccess(byte[] bytes) {
                               final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               profileimage.setImageBitmap(bm);

                           }
                       });
                   }
                   else{
                       profileimage.setImageResource(R.drawable.user);
                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }
       else{
           userRef.child(post.getUserId()).addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   final Users user=dataSnapshot.getValue(Users.class);
                   if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null){
                       userStorageRef.child(dataSnapshot.getValue(Users.class).getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                           @Override
                           public void onSuccess(byte[] bytes) {
                               final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                               profileimage.setImageBitmap(bm);

                           }
                       });
                   }
                   else{
                       profileimage.setImageResource(R.drawable.user);
                   }

                   comRef.child(post.getPlaceId()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
 postownername.setText(user.getUsername()+" posted in "+dataSnapshot.getValue(Community.class).getCommunityname());
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


        if(post.getPostcontent()!=null){
            postDescription.setVisibility(View.VISIBLE);
            postDescription.setText(post.getPostcontent());
        }
        else {
            postDescription.setVisibility(View.GONE);

        }



        if(post.isHasimage()==true){
            postimage.setVisibility(View.VISIBLE);
            postStorageRef.child(post.getImageId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    postimage.setImageBitmap(bm);
                }
            });


        }
        else{
            postimage.setVisibility(View.GONE);
        }




        return postlist;


    }
}
