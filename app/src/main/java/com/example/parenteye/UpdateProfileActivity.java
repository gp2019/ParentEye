package com.example.parenteye;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class UpdateProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("Users");
    private EditText usernameEdit;
    private EditText useremailEdit;
    private EditText useraddresseEdit;
    private TextView userdateedit;
    private  String name;
    private  String email;
    private String addresse;
    private Integer filldate=0;
    private Integer currentyear;
    private Integer useryear;
    private DatePickerDialog.OnDateSetListener datepicker;
    private  String choosendate;
    private Date newdate;
    private ProgressDialog progressdialogue;
    private ImageView calenderimgEdit;
    private TextView userdateofbirthword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mAuth = FirebaseAuth.getInstance();
        usernameEdit=(EditText) findViewById(R.id.editusername);
        useremailEdit=(EditText) findViewById(R.id.edituseremail);
        useraddresseEdit=(EditText) findViewById(R.id.edituseraddresse);
        userdateedit=(TextView) findViewById(R.id.userdateEdit);
        calenderimgEdit=(ImageView) findViewById(R.id.calenderimgEdit);
        userdateofbirthword=(TextView) findViewById(R.id.userdateofbirthword);

        calenderimgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                currentyear=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialogue=new DatePickerDialog(UpdateProfileActivity.this,android.R.style.Theme_DeviceDefault_Dialog,datepicker,year,month,day);
                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialogue.show();
            }
        });


        datepicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                choosendate=dayOfMonth +"/" + month +"/" +year;
                useraddresseEdit.setText(choosendate);
                userdateofbirthword.setVisibility(View.GONE);
                filldate=1;
                newdate=new Date(year,month,dayOfMonth);
                useryear=year;


            }


        };




    }

    @Override
    protected void onStart() {
        getuserdata();
        super.onStart();
    }

    private void getuserdata(){
        if(mAuth.getCurrentUser().getUid()!=null){
        userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);
                usernameEdit.setText(user.getUsername());
                useremailEdit.setText(user.getUserEmail());
                useraddresseEdit.setText(user.getLocation());
                userdateedit.setText(user.getDateofbirth().getDay()+"/"+user.getDateofbirth().getMonth()+"/"+user.getDateofbirth().getYear());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
    }
    private void updateInfo(){
        if(mAuth.getCurrentUser()!=null){

        }
    }
    private boolean validate(){
        boolean valid=true;
         name=usernameEdit.getText().toString().trim();
         email=useremailEdit.getText().toString().trim();
         addresse=useraddresseEdit.getText().toString().trim();
        if(name.isEmpty()){
            usernameEdit.setError("name is required");
            valid=false;
        }
        if(name.length()>25){
            useremailEdit.setError("name can not be that length");
            valid=false;
        }
        if(email.isEmpty()){
            useremailEdit.setError("Email is required");
            valid=false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            useremailEdit.setError("Email format not valid , please enter valid email");
            valid=false;
        }
        if(email.contains(" ")){
            email = email.replaceAll("\\s","");
        }
        if(addresse.isEmpty()){
            useraddresseEdit.setError("address is required");
            valid=false;
        }
        return valid;

    }
}
