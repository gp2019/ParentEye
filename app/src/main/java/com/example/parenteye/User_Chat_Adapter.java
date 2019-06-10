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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class User_Chat_Adapter extends ArrayAdapter<Users> {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private StorageReference userStorageRef= FirebaseStorage.getInstance().getReference("UserImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference UserRef = database.getReference("Users");
    DatabaseReference membersRef = database.getReference("Members");
    final long ONE_MEGABYTE = 1024 * 1024;
    public User_Chat_Adapter(Activity context, ArrayList<Users> userChatList){
        super(context,0,userChatList);

    }


    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View UsersChatList=convertView;
        if(UsersChatList==null){
            UsersChatList = LayoutInflater.from(getContext()).inflate(
                    R.layout.members_list, parent, false);

        }

        final Users member= getItem(position);
        Button Removebutton=(Button) UsersChatList.findViewById(R.id.RemovememberId);
            Removebutton.setVisibility(View.GONE);

        TextView membername=(TextView) UsersChatList.findViewById(R.id.membername);
        membername.setText(member.getUsername());



        final ImageView profileimage=(ImageView) UsersChatList.findViewById(R.id.memberphoto);

        if(member.getProfile_pic_id()!=null) {
            userStorageRef.child(member.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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





        return UsersChatList;


    }

}
