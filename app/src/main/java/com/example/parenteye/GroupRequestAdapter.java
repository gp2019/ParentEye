package com.example.parenteye;

import android.app.Activity;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupRequestAdapter extends ArrayAdapter<Users> {
    final long ONE_MEGABYTE = 1024 * 1024;
    private StorageReference userStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    DatabaseReference groupReqRef = database.getReference("GroupRequests");
    DatabaseReference membersRef = database.getReference("Members");
    public static final String searched_user_Id="searched_user_Id";
    public static final String searched_group_Id="searched_group_Id";
    public static final String searched_Item_name="searched_Item_name";
    private  Activity adaptercontext;

    public GroupRequestAdapter(Activity context, ArrayList<Users> requests){

        super(context,0,requests);
        adaptercontext=context;
    }



    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View requestList=convertView;

       if(requestList==null)  {
           requestList= LayoutInflater.from(getContext()).inflate(
                   R.layout.request_list, parent, false);
       }

       final Users request= getItem(position);
         TextView sendername=(TextView) requestList.findViewById(R.id.sendername);
        TextView groupname=(TextView) requestList.findViewById(R.id.groupname);
       Button AcceptRequest=(Button) requestList.findViewById(R.id.acceptRequest);
        Button RejectRequest=(Button) requestList.findViewById(R.id.rejectRequest);
       final  CircleImageView senderphoto=(CircleImageView) requestList.findViewById(R.id.senderphoto);
        if(request.getProfile_pic_id()!=null){
            userStorageRef.child(request.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    senderphoto.setImageBitmap(bm);

                }
            });
        }
        sendername.setText(request.getUsername());
        groupname.setText(request.getRoleId()); //RoleId represent group name
        request.getLocation(); //this is group id
        request.getUserId(); //this is userId
        AcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupReqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot resnapshot:dataSnapshot.getChildren()){
                            if(TextUtils.equals(resnapshot.getValue(GroupRequests.class).getGroupId(),request.getLocation())&&TextUtils.equals(resnapshot.getValue(GroupRequests.class).getUserid(),request.getUserId())){
                                groupReqRef.child(resnapshot.getKey()).removeValue();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Members new_member=new Members();
                new_member.setCommunityid( request.getLocation());
                new_member.setUserId(request.getUserId());
                new_member.setTyptId("1");
                membersRef.push().setValue(new_member);

            }
        });

        RejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupReqRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot resnapshot:dataSnapshot.getChildren()){
                            if(TextUtils.equals(resnapshot.getValue(GroupRequests.class).getGroupId(),request.getLocation())&&TextUtils.equals(resnapshot.getValue(GroupRequests.class).getUserid(),request.getUserId())){
                                groupReqRef.child(resnapshot.getKey()).removeValue();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        sendername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RequesterUser_Intent=new Intent(adaptercontext,AccountActivity.class);
                RequesterUser_Intent.putExtra(searched_user_Id,request.getUserId());
                adaptercontext.startActivity(RequesterUser_Intent);

            }
        });
        groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RequesterGroup_intent=new Intent(adaptercontext,GroupActivity.class);
                RequesterGroup_intent.putExtra(searched_group_Id,request.getLocation());
                RequesterGroup_intent.putExtra(searched_Item_name,request.getRoleId());
                adaptercontext.startActivity(RequesterGroup_intent);
            }
        });

        senderphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RequesterUser_Intent=new Intent(adaptercontext,AccountActivity.class);
                RequesterUser_Intent.putExtra(searched_user_Id,request.getUserId());
                adaptercontext.startActivity(RequesterUser_Intent);

            }
        });

        return requestList;


    }
}
