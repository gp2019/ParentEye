


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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class searchCommunityAdapter extends ArrayAdapter<Community>{
    final long ONE_MEGABYTE = 1024 * 1024;
    private Activity context;
    private List<Community> communityList;
    private StorageReference groupStorageRef = FirebaseStorage.getInstance().getReference("GroupImages/");
    private StorageReference pageStorageRef = FirebaseStorage.getInstance().getReference("PageImages/");
    public static final String searched_page_Id="searched_page_Id";
    public static final String searched_group_Id="searched_group_Id";
    public static final String searched_Item_name="searched_Item_name";


    public searchCommunityAdapter(Activity context, List<Community> communityList) {
        super(context, R.layout.display_community_result, communityList);
        this.context = context;
        this.communityList = communityList;

    }


    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.display_community_result, null, true);

        TextView community_name = (TextView) listView.findViewById(R.id.community_name);
        TextView type = (TextView) listView.findViewById(R.id.type);
        TextView description = (TextView) listView.findViewById(R.id.description);
        final ImageView communityPic = (ImageView) listView.findViewById(R.id.communityPic);

        Community community = communityList.get(position);
        community_name.setText(community.getCommunityname());


        String typeName  =community.getCommunityType().toString();
        String group= "group";
        if(! typeName.equals(group)){
            type.setText("Page Community");
        }else {
            type.setText("Group Community");
        }




        if (community.getCommunityAbout()!=null) {
            description.setText(community.getCommunityAbout());
        }else {
            description.setText(" ");
        }


        if (community.getPhotoId()!=null) {

            if (! typeName.equals(group)) {
                pageStorageRef.child(community.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        communityPic.setImageBitmap(bm);


                    }
                });
            }else {
                groupStorageRef.child(community.getPhotoId()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        communityPic.setImageBitmap(bm);

                    }
                });
            }
        } else {
            communityPic.setImageResource(R.drawable.search_community);

        }
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Community searched_comm=communityList.get(position);
                System.out.println("enter  community "+searched_comm.getCommunityId());
                if(TextUtils.equals(searched_comm.getTypeid(),"1")){
                    Intent searched_intent=new Intent(getApplicationContext(),GroupActivity.class);
                    searched_intent.putExtra(searched_group_Id,searched_comm.getCommunityId());
                    searched_intent.putExtra(searched_Item_name,searched_comm.getCommunityname());
                    System.out.println("group id "+searched_comm.getCommunityId());
                    context.startActivity(searched_intent);
                }
                else if(TextUtils.equals(searched_comm.getTypeid(),"2")){
                    Intent searched_intent=new Intent(context,PageActivity.class);
                    searched_intent.putExtra(searched_page_Id,searched_comm.getCommunityId());
                    System.out.println("page id "+searched_comm.getCommunityId());
                    searched_intent.putExtra(searched_Item_name,searched_comm.getCommunityname());
                    context.startActivity(searched_intent);
                }
            }
        });
        return listView;
    }



}