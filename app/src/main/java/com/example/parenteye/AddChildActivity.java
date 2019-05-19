package com.example.parenteye;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddChildActivity extends AppCompatActivity {
    private  static  final  String TAG="AddChildActivity";
    private FirebaseAuth mAuth;
    private EditText signup_username;
    private EditText signup_email;
    private EditText signup_password;
    private EditText signup_reEnterpassword;
   // private EditText signup_addresse;
    private Integer filldate=0;
    private TextView date;
    private Integer currentyear;
    private Integer useryear;
    private DatePickerDialog.OnDateSetListener datepicker;
    private RadioGroup radio_group_gender;
    private RadioButton choosenRadiobutton;
    private  String choosendate;
    private   Date newdate;
    private CircleImageView circleimage;
    private boolean isMale;
    private Button signup;
    private TextView signup_login;
    private Integer PICK_IMAGE_REQUEST=71;
    private Uri filepath;
    private String imagekey;
    private StorageReference mStorageRef;
    private  ProgressDialog progressdialogue;
    private String name,Email,password,Reenterpass,Addresse;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference UserRef = database.getReference("Users");
    private DatabaseReference parentChildrenRef = database.getReference("ParentChildren");
    private TextView Alredyregisteredtext;
    private Spinner spinner1;
    String[] cities = new String[]{"Cairo", "Alexandria", "Giza","Port Said","Suez","Luxor","al-Mansura","El-Mahalla El-Kubra","Tanta","Asyut",
            "tIsmailia","Fayyum","Zagazig"," Aswan","Damietta","Damanhur","al-Minya","Beni Suef"," Qena","Sohag","Hurghada","6th of October City","Shibin El Kom",
            "Banha"," Kafr el-Sheikh","Arish","Mallawi","10th of Ramadan City","Bilbais","Marsa Matruh","Idfu","Mit Ghamr","Al-Hamidiyya","Desouk",
            "Qalyub","Abu Kabir","Kafr el-Dawwar","Girga","Akhmim","Matareya"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        signup_username=(EditText)findViewById(R.id.usernameedittext);
        signup_username.setHint("Child name");
        signup_email=(EditText) findViewById(R.id.signup_email);
        signup_email.setHint("child Email");
        signup_password=(EditText) findViewById(R.id.signup_password);
        signup_password.setHint("Child Password");
        signup_reEnterpassword=(EditText) findViewById(R.id.signup_reenterpassword);
        signup_login=(TextView) findViewById(R.id.signup_login);
        signup_login.setVisibility(View.GONE);
        Alredyregisteredtext=(TextView) findViewById(R.id.Alredyregisteredtext);
        Alredyregisteredtext.setVisibility(View.GONE);
       // signup_addresse=(EditText)findViewById(R.id.addresse_edittext);
        date=(TextView) findViewById(R.id.date);
        radio_group_gender=(RadioGroup) findViewById(R.id.radipgp_gender);
        circleimage=(CircleImageView) findViewById(R.id.profile_image);
        signup=(Button) findViewById(R.id.signup_btn);
        signup.setText("Add");
        spinner1=(Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city=(String)parent.getItemAtPosition(position);
                Addresse="Egypt/"+city;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Addresse="Egypt";
            }
        });



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                currentyear=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialogue=new DatePickerDialog(AddChildActivity.this,android.R.style.Theme_DeviceDefault_Dialog,datepicker,year,month,day);
                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialogue.show();
            }
        });


        datepicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                choosendate=dayOfMonth +"/" + month +"/" +year;
                date.setText(choosendate);
                filldate=1;
                newdate=new Date(year,month,dayOfMonth);
                useryear=year;


            }


        };


        circleimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE_REQUEST);
            }
        });






        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (mAuth.getCurrentUser() != null) {
                        showDialogue();
                        int genderId = radio_group_gender.getCheckedRadioButtonId();
                        choosenRadiobutton = (RadioButton) findViewById(genderId);
                        if (TextUtils.equals(choosenRadiobutton.getText().toString(), "Male")) {
                            isMale = true;

                        } else {
                            isMale = false;

                        }

                        upload_profile_pic();
                       final String CurrentParent=mAuth.getCurrentUser().getUid();
                        final Users newuser = new Users();
                        newuser.setUsername(name);
                        newuser.setUserEmail(Email);
                        newuser.setUserPassword(password);
                        newuser.setLocation(Addresse);
                        newuser.setDateofbirth(newdate);
                        newuser.setGender(isMale);
                        newuser.setRoleId("2");
                        newuser.setProfile_pic_id(imagekey);
                        newuser.setCloseAccount(false);
                        newuser.setTimeCloseAccount("");
                        // newuser.setCover_pic_id("");

                        mAuth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    newuser.setUserId(mAuth.getCurrentUser().getUid());
                                    UserRef.child(mAuth.getCurrentUser().getUid()).setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ParentChildren parentchild=new ParentChildren();
                                                parentchild.setParentId(CurrentParent);
                                                parentchild.setChildId(mAuth.getCurrentUser().getUid());
                                                parentChildrenRef.push().setValue(parentchild).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            dismissProgressDialog();
                                                            Toast.makeText(AddChildActivity.this, "Congratulation, Your Child Account Created Successfully", Toast.LENGTH_LONG).show();
                                                            mAuth.signOut();
                                                            UserRef.child(CurrentParent).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    final String Email=dataSnapshot.getValue(Users.class).getUserEmail();
                                                                    String password=dataSnapshot.getValue(Users.class).getUserPassword();
                                                                    mAuth.signInWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                            if(task.isSuccessful()){
                                                                              // System.out.println("login with "+Email);
                                                                                Go_to_main();

                                                                            }else{
                                                                              //  System.out.println(" not login");
                                                                            }
                                                                        }
                                                                    });

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }else{
                                                            mAuth.getCurrentUser().delete();
                                                            UserRef.child(mAuth.getCurrentUser().getUid()).removeValue();
                                                            dismissProgressDialog();
                                                        }
                                                    }
                                                });

                                            } else {
                                                mAuth.getCurrentUser().delete();
                                                dismissProgressDialog();

                                                Toast.makeText(AddChildActivity.this, "Error !! " + task.getException(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                } else {
                                    dismissProgressDialog();
                                    Toast.makeText(AddChildActivity.this, "Error !! " + task.getException(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                    else{
                        Toast.makeText(AddChildActivity.this,"you should login first" ,Toast.LENGTH_LONG).show();
                    }


                }
            }
        });





    }



    private  void Go_to_main(){
        Intent signup_main=new Intent(AddChildActivity.this,MainActivity.class);
        startActivity(signup_main);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean validate(){
        boolean valid=true;
        name=signup_username.getText().toString().trim();
        Email=signup_email.getText().toString().trim();
        password=signup_password.getText().toString().trim();
        Reenterpass=signup_reEnterpassword.getText().toString().trim();
       // Addresse=signup_addresse.getText().toString().trim();
        if(name.isEmpty()){
            signup_username.setError("child name can not be empty");
            valid=false;
        }
        if(name.length()>25){
            signup_username.setError("child name can not be that long");
            valid=false;
        }
        if(Email.isEmpty()){
            signup_email.setError("child Email is mandatory, it can not be empty");
            valid=false;
        }
        if(Email.contains(" ")){
            Email = Email.replaceAll("\\s","");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            signup_email.setError("Email format not valid , please enter valid email");
            valid=false;
        }
        if(password.isEmpty()){
            signup_password.setError("Password can not be empty");
            valid=false;
        }
        if(Reenterpass.isEmpty()){
            signup_reEnterpassword.setError("ReEnter password can not be empty");
            valid=false;
        }
        if(!TextUtils.equals(password,Reenterpass)){
            signup_reEnterpassword.setError("The two password should be matched");
            valid=false;
        }
       /* if(Addresse.isEmpty()){
            signup_addresse.setError("Addresse can not be empty");
            valid=false;
        }
        if(Addresse.length()>300){
            signup_addresse.setError("Addresse can not be that long");
            valid=false;
        }
        */

        if(filldate==0){
            date.setError("Date of birth is mandatory");
            valid=false;

        }
        if(filldate==1){
            if(currentyear-useryear>20){
                date.setError("child age should not exceed 20 years");
                Toast.makeText(AddChildActivity.this,"child age should not exceed 20 years",Toast.LENGTH_LONG).show();
                valid=false;
            }
        }

        return valid;


    }






    private void showDialogue(){
        if(progressdialogue==null){
            progressdialogue=new ProgressDialog(this);
            progressdialogue.setTitle("loading...");
            progressdialogue.setMessage("please wait...");

        }
        progressdialogue.show();
    }
    private void dismissProgressDialog() {
        if (progressdialogue != null && progressdialogue.isShowing()) {
            progressdialogue.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filepath=data.getData();

            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                circleimage.setImageBitmap(bitmap);

            }
            catch (IOException e){
                e.printStackTrace();

            }
        }

    }

    private void upload_profile_pic(){

        if(filepath!=null){
            // final ProgressDialog progressdialogue=new ProgressDialog(this);
            //  progressdialogue.setTitle("loading...");
            //  progressdialogue.show();

            imagekey = UUID.randomUUID().toString();
            StorageReference ref = mStorageRef.child("UserImages/"+imagekey);
            ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        dismissProgressDialog();
                    }
                    else{
                        dismissProgressDialog();
                        Toast.makeText(AddChildActivity.this,"Profile Photo uploading error !!"+task.getException(),Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }































/*
    private  static  final  String TAG="AddChildActivity";
    private TextView date;
    private DatePickerDialog.OnDateSetListener datepicker;
    private RadioGroup radio_group_gender;
    private RadioButton choosenRadiobutton;
    private EditText username;
    private EditText addresse;
    private Button submit;
    private Integer filldate=0;
    private  String choosendate;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    private   DatabaseReference parentChildrenRef = database.getReference("ParentChildren");
    private Date newdate;
    private boolean isMale;
    private Integer currentyear;
    private Integer useryear;
    private CircleImageView circleimage;
    private Integer PICK_IMAGE_REQUEST=71;
    private Uri filepath;
    private StorageReference mStorageRef;
    private String imagekey;
    private TextView logout;
    private ProgressDialog progressdialogue;
    private EditText childemail;
    private EditText childpassword;
    private TextView finaltextindesign;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        childemail=(EditText)findViewById(R.id.childemaildittext);
        childpassword=(EditText) findViewById(R.id.childpasswodedittext);
        childemail.setVisibility(View.VISIBLE);
        childpassword.setVisibility(View.VISIBLE);
        finaltextindesign=(TextView) findViewById(R.id.finaltext);
        finaltextindesign.setVisibility(View.GONE);
        logout=(TextView) findViewById(R.id.logout_data);
        logout.setVisibility(View.GONE);

        mAuth=FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        date=(TextView) findViewById(R.id.date);
        radio_group_gender=(RadioGroup) findViewById(R.id.radipgp_gender);
        username=(EditText) findViewById(R.id.usernameedittext) ;
        addresse=(EditText) findViewById(R.id.addresse_edittext);
        circleimage=(CircleImageView) findViewById(R.id.profile_image);
        submit=(Button) findViewById(R.id.submit);






        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                currentyear=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialogue=new DatePickerDialog(AddChildActivity.this,android.R.style.Theme_DeviceDefault_Dialog,datepicker,year,month,day);
                dialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialogue.show();
            }
        });


        datepicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                choosendate=dayOfMonth +"/" + month +"/" +year;
                date.setText(choosendate);
                filldate=1;
                newdate=new Date(year,month,dayOfMonth);
                useryear=year;


            }


        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue();
                String name=username.getText().toString();
                String useraddresse=addresse.getText().toString();
                String child_mail=childemail.getText().toString();
                String child_pass=childpassword.getText().toString();
                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(useraddresse)&&filldate!=0&&!TextUtils.isEmpty(child_mail)&&!TextUtils.isEmpty(child_pass)){
                    if(currentyear-useryear<18){
                        int genderId=radio_group_gender.getCheckedRadioButtonId();
                        choosenRadiobutton= (RadioButton)findViewById(genderId);
                        if(TextUtils.equals(choosenRadiobutton.getText().toString(),"Male")){
                            isMale=true;

                        }
                        else{
                            isMale=false;

                        }


                        upload_profile_pic();
                        final String parentId=mAuth.getCurrentUser().getUid();
                       final FirebaseUser currentParent=mAuth.getCurrentUser();
                        final Users newuser=new Users();
                        newuser.setUsername(name);
                        newuser.setUserEmail(child_mail);
                        mAuth.createUserWithEmailAndPassword(child_mail,child_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    myRef.child(mAuth.getCurrentUser().getUid()).setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                ParentChildren parentchild=new ParentChildren();
                                                parentchild.setChildId(mAuth.getCurrentUser().getUid());
                                                parentchild.setParentId(parentId);
                                                parentChildrenRef.push().setValue(parentchild).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            dismissProgressDialog();
                                                            Toast.makeText(AddChildActivity.this,"Child addedd successfully",Toast.LENGTH_LONG).show();
                                              mAuth.signOut();
                                                  myRef.child(parentId).addValueEventListener(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String username =dataSnapshot.getValue(Users.class).getUsername();
                                                        System.out.print("username is"+username);
                                                          mAuth.signInWithEmailAndPassword("aya@yahoo.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<AuthResult> task) {
                                                                  if(task.isSuccessful()){
                                                                      System.out.println("login");
                                                                      Go_to_main();
                                                                  }
                                                                  else{
                                                                      System.out.println(" not login");
                                                                  }
                                                              }
                                                          });
                                                      }

                                                      @Override
                                                      public void onCancelled(@NonNull DatabaseError databaseError) {

                                                      }
                                                  });


                                                        }
                                                        else{
                                                            dismissProgressDialog();
                                                            Toast.makeText(AddChildActivity.this,"Error !!!  "+task.getException(),Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });

                                            }
                                            else{
                                                dismissProgressDialog();
                                                Toast.makeText(AddChildActivity.this,"Error !!!  "+task.getException(),Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                }
                                else{
                                    dismissProgressDialog();
                                    Toast.makeText(AddChildActivity.this,"Error !!!  "+task.getException(),Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    }
                    else{
                        dismissProgressDialog();
                        Toast.makeText(AddChildActivity.this,"Sorry,your child should not exceed 18 years!!",Toast.LENGTH_LONG).show();

                    }
                }






                else{
                    dismissProgressDialog();
                    Toast.makeText(AddChildActivity.this,"Fill all fields please ,they are all mandatory !!",Toast.LENGTH_LONG).show();

                }

                // String chosen=choosenRadiobutton.getText().toString();
                //  Toast.makeText(UserdataActivity.this,choosenRadiobutton.getText(),Toast.LENGTH_LONG).show();
            }
        });


        circleimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE_REQUEST);
            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filepath=data.getData();

            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                circleimage.setImageBitmap(bitmap);

            }
            catch (IOException e){
                e.printStackTrace();

            }
        }

    }

    private void upload_profile_pic(){
        if(filepath!=null){
            // final ProgressDialog progressdialogue=new ProgressDialog(this);
            //  progressdialogue.setTitle("loading...");
            //  progressdialogue.show();

            imagekey = UUID.randomUUID().toString();
            StorageReference ref = mStorageRef.child("UserImages/"+imagekey);
            ref.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // progressdialogue.dismiss();
                            dismissProgressDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressdialogue.dismiss();
                            dismissProgressDialog();
                            Toast.makeText(AddChildActivity.this,"Profile Photo uploading error",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    private void Go_to_main(){
        Intent goMain=new Intent(AddChildActivity.this,MainActivity.class);
        startActivity(goMain);
        finish();
    }
    private void showDialogue(){
        if(progressdialogue==null){
            progressdialogue=new ProgressDialog(this);
            progressdialogue.setTitle("loading...");
            progressdialogue.setMessage("please wait...");

        }
        progressdialogue.show();
    }
    private void dismissProgressDialog() {
        if (progressdialogue != null && progressdialogue.isShowing()) {
            progressdialogue.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    */
}
