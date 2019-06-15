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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private getCurrentTime currentTime;
    private CreateTime createTime;
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

        final Switch switchButton = childrenlist.findViewById(R.id.btCloseAccount);
        final Users child= getItem(position);
        TextView childname=(TextView) childrenlist.findViewById(R.id.childname);
        childname.setText(child.getUsername());

       final  ImageView profileimage = childrenlist.findViewById(R.id.childphoto);

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

        UserRef.child(childArayList.get(position).getUserId()).child("TimeCloseAccount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            currentTime = new getCurrentTime();
                if (dataSnapshot.exists()){
                    String ti = dataSnapshot.getValue(String.class);
                    if (!ti.isEmpty()){
                        createTime = new CreateTime(ti);

                        try {
                            boolean CheckTime =  currentTime.compareTime(currentTime.getDateTime(),ti);
                            createTime.sdf();
                            if (CheckTime==false){
                                counter.setText(""+(createTime.calculateTime().replace("-","")));
                                switchButton.setChecked(true);
                            }
                            else {
                                counter.setText("0");
                                switchButton.setChecked(false);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        counter.setText("0");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchButton.isChecked()){

                    if (Counter>0){
                        currentTime = new getCurrentTime();

                    try {
                        String date = currentTime.getTimeCloseAccount(Counter);
                        UserRef.child(childArayList.get(position).getUserId()).child("closeAccount").setValue(true);
                        UserRef.child(childArayList.get(position).getUserId()).child("TimeCloseAccount").setValue(date);
                        Counter=0;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    }
                }
                else {
                    UserRef.child(childArayList.get(position).getUserId()).child("closeAccount").setValue(false);
                    UserRef.child(childArayList.get(position).getUserId()).child("TimeCloseAccount").setValue("");
                    Counter=0;
                    counter.setText(""+Counter);
                }
            }
        });
        return childrenlist;


    }

}
