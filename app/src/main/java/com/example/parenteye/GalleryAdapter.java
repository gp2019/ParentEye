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

import com.example.parenteye.R;
import com.example.parenteye.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<Users> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    private StorageReference userStorageRef= FirebaseStorage.getInstance().getReference("UserImages/");
    private StorageReference postStorageRef = FirebaseStorage.getInstance().getReference("PostImages/");
    final long ONE_MEGABYTE = 1024 * 1024;
    public GalleryAdapter(Activity context, ArrayList<Users> UserRequest){
        super(context,0,UserRequest);

    }
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View Gallerylist=convertView;
        if(Gallerylist==null) {
            Gallerylist = LayoutInflater.from(getContext()).inflate(
                    R.layout.gallery_list, parent, false);
        }


        final Users user= getItem(position);
        final ImageView image1=(ImageView) Gallerylist.findViewById(R.id.image1);
        if(TextUtils.equals(user.getRoleId(),"1")) {
            userStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image1.setImageBitmap(bm);

                }
            });
        }

        else if(TextUtils.equals(user.getRoleId(),"2")) {
            postStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image1.setImageBitmap(bm);

                }
            });

        }    else if(TextUtils.equals(user.getRoleId(),"3")){
            //heere comment photo
        }



        return Gallerylist;


    }
}
