

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

public class searchFriendAdapter extends ArrayAdapter<Users> {
    final long ONE_MEGABYTE = 1024 * 1024;
    private Activity context;
    private List<Users> userslist;
    private StorageReference userStorageRef = FirebaseStorage.getInstance().getReference("UserImages/");
    public static final String searched_user_Id="searched_user_Id";

    public searchFriendAdapter(Activity context, List<Users> userslist) {
        super(context, R.layout.display_search_result, userslist);
        this.context = context;
        this.userslist = userslist;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.display_search_result, null, true);

        TextView user_name = (TextView) listView.findViewById(R.id.user_name);
        TextView location = (TextView) listView.findViewById(R.id.location);
        final ImageView userPic = (ImageView) listView.findViewById(R.id.userPic);

        Users user = userslist.get(position);
        user_name.setText(user.getUsername());
        if (user.getLocation()!=null) {
            location.setText(user.getLocation());
        }else {
            location.setText(" ");
        }

        if (user.getProfile_pic_id()!=null) {

            userStorageRef.child(user.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    userPic.setImageBitmap(bm);

                }
            });
        } else if(user.isGender()==true) {
            userPic.setImageResource(R.drawable.profile_boys);

        }
        else {
            userPic.setImageResource(R.drawable.profile_giles);
        }
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userslist.get(position)!=null){
                    Users searched_user=userslist.get(position);
                    Intent searched_intent=new Intent(context,AccountActivity.class);
                    searched_intent.putExtra(searched_user_Id,searched_user.getUserId());
                    context.startActivity(searched_intent);

                }

            }
        });

        return listView;
    }



}

