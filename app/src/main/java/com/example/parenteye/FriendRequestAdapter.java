package com.example.parenteye;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FriendRequestAdapter extends ArrayAdapter<Users>  {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    private StorageReference userStorageRef= FirebaseStorage.getInstance().getReference("UserImages/");
    final long ONE_MEGABYTE = 1024 * 1024;
    public FriendRequestAdapter(Activity context, ArrayList<Users> UserRequest){
        super(context,0,UserRequest);
    }
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

      View friendRequestList=convertView;
        if(friendRequestList==null){
            friendRequestList = LayoutInflater.from(getContext()).inflate(
                    R.layout.friendrequest_list, parent, false);

        }

         final Users user= getItem(position);
       final TextView sendername=(TextView) friendRequestList.findViewById(R.id.senderIdname);
        final  ImageView profileimage=(ImageView) friendRequestList.findViewById(R.id.senderprofilepic);
   if(user.getUsername()!=null){
       userRef.child(user.getUsername()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              String username=dataSnapshot.getValue(Users.class).getUsername();
               sendername.setText(username);
               if(dataSnapshot.getValue(Users.class).getProfile_pic_id()!=null) {
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











        return friendRequestList;


    }
}
