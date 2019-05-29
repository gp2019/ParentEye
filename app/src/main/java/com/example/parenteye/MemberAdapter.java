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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class MemberAdapter extends ArrayAdapter<Users> {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private StorageReference userStorageRef= FirebaseStorage.getInstance().getReference("UserImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference UserRef = database.getReference("Users");
    DatabaseReference membersRef = database.getReference("Members");
    final long ONE_MEGABYTE = 1024 * 1024;
    public MemberAdapter(Activity context, ArrayList<Users> memberList){
        super(context,0,memberList);

    }


    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View MemberList=convertView;
        if(MemberList==null){
            MemberList = LayoutInflater.from(getContext()).inflate(
                    R.layout.members_list, parent, false);

        }

        final Users member= getItem(position);
        Button Removebutton=(Button) MemberList.findViewById(R.id.RemovememberId);

        if(!TextUtils.equals(mAuth.getCurrentUser().getUid(),member.getUserId())){
            Removebutton.setVisibility(View.GONE);
        }
        else{
            Removebutton.setVisibility(View.VISIBLE);
        }
        TextView membername=(TextView) MemberList.findViewById(R.id.membername);
        membername.setText(member.getUsername());



        final ImageView profileimage=(ImageView) MemberList.findViewById(R.id.memberphoto);

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
        Removebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                membersRef.child(member.getRoleId()).removeValue();
            }
        });




        return MemberList;


    }

}
