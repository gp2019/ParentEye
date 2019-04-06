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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

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
    DatabaseReference galleryRef = database.getReference("Gallery");
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
        signup_email=(EditText) findViewById(R.id.signup_email);
        signup_password=(EditText) findViewById(R.id.signup_password);
        signup_reEnterpassword=(EditText) findViewById(R.id.signup_reenterpassword);
        signup_login=(TextView) findViewById(R.id.signup_login);
       // signup_addresse=(EditText)findViewById(R.id.addresse_edittext);
        date=(TextView) findViewById(R.id.date);
        radio_group_gender=(RadioGroup) findViewById(R.id.radipgp_gender);
        circleimage=(CircleImageView) findViewById(R.id.profile_image);
        signup=(Button) findViewById(R.id.signup_btn);
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
                DatePickerDialog dialogue=new DatePickerDialog(SignupActivity.this,android.R.style.Theme_DeviceDefault_Dialog,datepicker,year,month,day);
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
                if(validate()){
                    showDialogue();
                    int genderId=radio_group_gender.getCheckedRadioButtonId();
                    choosenRadiobutton= (RadioButton)findViewById(genderId);
                    if(TextUtils.equals(choosenRadiobutton.getText().toString(),"Male")){
                        isMale=true;

                    }
                    else{
                        isMale=false;

                    }

                    upload_profile_pic();

                    final Users newuser=new Users();
                    newuser.setUsername(name);
                    newuser.setUserEmail(Email);
                    newuser.setUserPassword(password);
                    newuser.setLocation(Addresse);
                    newuser.setDateofbirth(newdate);
                    newuser.setGender(isMale);
                    newuser.setRoleId("1");
                    newuser.setActiveStatus(true);
                    newuser.setProfile_pic_id(imagekey);
                   // newuser.setCover_pic_id("");

                    mAuth.createUserWithEmailAndPassword(Email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                  UserRef.child(mAuth.getCurrentUser().getUid()).setValue(newuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              if(imagekey!=null){
                                  AddtoGallery(mAuth.getCurrentUser().getUid(),imagekey,"1");
                              }

                              dismissProgressDialog();
              Toast.makeText(SignupActivity.this,"Congratulation, Your Account Created Successfully",Toast.LENGTH_LONG).show();
                              Go_to_main();
                          }
                          else{
                              mAuth.getCurrentUser().delete();
                              dismissProgressDialog();

       Toast.makeText(SignupActivity.this,"Error !! "+task.getException(),Toast.LENGTH_LONG).show();

                          }
                      }
                  });

                            }else{
                                dismissProgressDialog();
              Toast.makeText(SignupActivity.this,"Error !! "+task.getException(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });



                }



            }
        });


        signup_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Go_to_login();
            }
        });



    }



    private  void Go_to_main(){
        Intent signup_main=new Intent(SignupActivity.this,MainActivity.class);
        startActivity(signup_main);
        finish();
    }

    @Override
    protected void onStart() {
        if(mAuth.getCurrentUser()!=null){
            Go_to_main();
        }
        super.onStart();
    }

    private void Go_to_login(){
        Intent signuplogin=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(signuplogin);
        finish();
    }
    private boolean validate(){
        boolean valid=true;
       name=signup_username.getText().toString().trim();
       Email=signup_email.getText().toString().trim();
       password=signup_password.getText().toString().trim();
       Reenterpass=signup_reEnterpassword.getText().toString().trim();
      // Addresse=signup_addresse.getText().toString().trim();
       if(name.isEmpty()){
           signup_username.setError("Username can not be empty");
           valid=false;
       }
       if(name.length()>25){
           signup_username.setError("Username can not be that long");
           valid=false;
       }
      if(Email.isEmpty()){
          signup_email.setError("Email is mandatory, it can not be empty");
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
           if(currentyear-useryear<20){
               date.setError("your age should not be less than 20 years");
               Toast.makeText(SignupActivity.this,"your age should not be less than 20 years",Toast.LENGTH_LONG).show();
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
  Toast.makeText(SignupActivity.this,"Profile Photo uploading error !!"+task.getException(),Toast.LENGTH_LONG).show();

                    }
                }
            });

                   /* .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                            Toast.makeText(SignupActivity.this,"Profile Photo uploading error",Toast.LENGTH_LONG).show();
                        }
                    });
                    */
        }
    }
  private void AddtoGallery(String ownerId,String photoId,String typeid){
        // 1=user,2=post,3=comment
        Users user=new Users();
        user.setProfile_pic_id(photoId);
        user.setRoleId(typeid);
        galleryRef.child(ownerId).push().setValue(user);

    }

}

