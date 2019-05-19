package com.example.parenteye;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChildAdapter extends ArrayAdapter<Users> {
    private StorageReference userStorageRef= FirebaseStorage.getInstance().getReference("UserImages/");
    private int Counter=0;
    private ArrayList<Users> childArayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference UserRef = database.getReference("Users");
    private Date time;
    final long ONE_MEGABYTE = 1024 * 1024;
    public ChildAdapter(Activity context, ArrayList<Users> childreturned){
        super(context,0,childreturned);
        this.childArayList=childreturned;
    }


    @NonNull
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View childrenlist=convertView;
        if(childrenlist==null){
            childrenlist = LayoutInflater.from(getContext()).inflate(
                    R.layout.child_list, parent, false);

        }

        final Users child= getItem(position);
    //    TextView Datetime=(TextView) childrenlist.findViewById(R.id.datetime);
        TextView childname=(TextView) childrenlist.findViewById(R.id.childname);
        childname.setText(child.getUsername());
      /*  if(child.getRoleId()!=null){
            Datetime.setVisibility(View.VISIBLE);
            Datetime.setText(child.getRoleId());
          //  System.out.println("date is "+child.getRoleId());
        }
        else{
            Datetime.setVisibility(View.GONE);
        }
*/



       final  ImageView profileimage=(ImageView) childrenlist.findViewById(R.id.childphoto);

        if(child.getProfile_pic_id()!=null) {
            userStorageRef.child(child.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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


        final EditText counter = childrenlist.findViewById(R.id.counter);

        final ImageButton btPlus = childrenlist.findViewById(R.id.btPlus);

        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Counter>=0) {
                    counter.setText("" + (++Counter));
                }
            }
        });

        final ImageButton btMins = childrenlist.findViewById(R.id.btMins);

        btMins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Counter>=0) {
                    counter.setText("" + (--Counter));
                }
            }
        });

        final Switch switchButton = childrenlist.findViewById(R.id.btCloseAccount);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchButton.isChecked()){

                    if (Counter>0){
                    getCurrentTime currentTime = new getCurrentTime();

                    try {
                        String date= null;
                        date = currentTime.getTimeCloseAccount(Counter);
                        //boolean x=currentTime.compareTime(currentTime.getDateTime(),date);
                        UserRef.child(childArayList.get(position).getUserId()).child("CloseAccount").setValue(true);
                        UserRef.child(childArayList.get(position).getUserId()).child("TimeCloseAccount").setValue(date);
                        Counter=0;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    }
                }
                else {

                }
            }
        });
        return childrenlist;


    }

}
