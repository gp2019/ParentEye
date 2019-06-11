package com.example.parenteye;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.O)
@RequiresApi(api = Build.VERSION_CODES.O)
public class Create_Post extends AppCompatActivity implements View.OnClickListener {


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    private Uri filepath;
    private String imagekey;
    private BottomSheetDialog bottomSheetDialog;
    private RelativeLayout bottomSheet;
    private EditText writePost;
    private ImageView arrowBack,imageViewPost,remove,profileImage;
    private TextView post,nameUser,CreatePost;
    DatabaseReference dbRef;
    private String userId,placeTypeId,placeId;
    private String postIdEdit,postcontentEdit,imageIdEdit,hasImageEdit;
    private  ProgressDialog progressdialogue;
    private String ImageId;
    private StorageReference mStorageRef,mStorageRef2;
    final long ONE_MEGABYTE = 1024 * 1024;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference galleryRef = database.getReference("Gallery");



    Users users = new Users();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        writePost = findViewById(R.id.edit_text_write_post);
        arrowBack = findViewById(R.id.arrow_back);
        post = findViewById(R.id.post);
        nameUser = findViewById(R.id.name_of_user);
        bottomSheet = findViewById(R.id.bottom_sheet);
        profileImage = findViewById(R.id.profile_image);
        CreatePost = findViewById(R.id.create_post);
        remove = findViewById(R.id.remove);
        remove.setVisibility(View.INVISIBLE);

        imageViewPost = findViewById(R.id.imageViewPost);
        imageViewPost.setVisibility(View.GONE);

        dbRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();



         userId= getIntent().getStringExtra("userId");
        placeTypeId= getIntent().getStringExtra("placeTypeId");
        placeId= getIntent().getStringExtra("placeId");
         ImageId = getIntent().getStringExtra("ImageId");


        mStorageRef2 = FirebaseStorage.getInstance().getReference("UserImages/");

       if (users.getProfile_pic_id()!=null){
        mStorageRef2.child(users.getProfile_pic_id()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                final Bitmap bm = BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
                profileImage.setImageBitmap(bm);
            }
        });
        }

         dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users = dataSnapshot.getValue(Users.class);
                nameUser.setText(users.getUsername());
                //Toast.makeText(Create_Post.this, "the User ID "+users.getUsername()+"the type post is "+typePost,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Create_Post.this, "type error "+databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        //bottom sheet

        bottomSheetDialog = new BottomSheetDialog(Create_Post.this);
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.activity_bottom__sheet, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);

        View photo_gallery = bottomSheetDialogView.findViewById(R.id.gallery);
        View camera = bottomSheetDialogView.findViewById(R.id.camera);

        //click listener function

        photo_gallery.setOnClickListener(this);
        camera.setOnClickListener(this);
        bottomSheet.setOnClickListener(this);
        remove.setOnClickListener(this);
        post.setOnClickListener(this);
        arrowBack.setOnClickListener(this);
    }

   //listener function

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {

                case R.id.bottom_sheet:
                bottomSheetDialog.show();
                break;

                case R.id.gallery:
                galleryIntent();
                bottomSheetDialog.dismiss();
                break;


                case R.id.camera:
                cameraIntent();
                bottomSheetDialog.dismiss();
                break;

                case R.id.remove:
                imageViewPost.setImageDrawable(null);
                remove.setVisibility(view.GONE);
                if (imageViewPost.getDrawable()==null)
                {
                    writePost.setHint("what's on your mind ?");
                }
                break;

                case R.id.post:
                publishPost();
                break;

                case R.id.arrow_back:
                    arrowBack();
                    break;
        }

    }

    // function publishPost
    private void  publishPost (){

        if (writePost.getText().toString().equals("")&&imageViewPost.getDrawable()==null)
        {
            //here will put intent from ""home page"" and exchange by ""Toast""
            Toast.makeText(Create_Post.this, "No data to publish",Toast.LENGTH_LONG).show();
        }
        else {
            showDialogue();
            boolean hasImage=false;
            if (imageViewPost.getDrawable()!=null)
            {
                upload_post_pic();
                hasImage= true;
            }

            dbRef= FirebaseDatabase.getInstance().getReference().child("Posts");

            if (postIdEdit!=null){
                dbRef.child(postIdEdit).child("postcontent").setValue(writePost.getText().toString());
                if (hasImage==true){
                    dbRef.child(postIdEdit).child("imageId").setValue(imageIdEdit);
                    dbRef.child(postIdEdit).child("hasimage").setValue(true);
                }
                dismissProgressDialog();
                GoAcountActive();
            }
            else {

            String Post_ID= dbRef.push().getKey();

            Posts posts = new Posts(Post_ID,userId,new getCurrentTime().getDateTime(),placeTypeId,placeId,writePost.getText().toString(),hasImage,imagekey,0,0);

            dbRef.child(Post_ID).setValue(posts).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        dismissProgressDialog();
                        Toast.makeText(Create_Post.this,"completed Successfully",Toast.LENGTH_LONG).show();
                        GoHome();
                    }
                    else{
                        dismissProgressDialog();
                        Toast.makeText(Create_Post.this,"Error !!!  "+task.getException(),Toast.LENGTH_LONG).show();

                    }
                }
            });
          }

        }

    }

    // Gallery Intent
    private void galleryIntent()
    {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
        */
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,SELECT_FILE);
    }
    // Camera Intent
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        filepath = data.getData();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (writePost.getText().toString().equals(""))
        {
            writePost.setHint("say something about this photo...");
        }

        imageViewPost.setVisibility(View.VISIBLE);
        imageViewPost.setImageBitmap(thumbnail);
        remove.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        filepath = data.getData();
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (writePost.getText().toString().equals("")&&imageViewPost.getDrawable()==null)
        {
            writePost.setHint("say something about this photo...");
        }

        imageViewPost.setVisibility(View.VISIBLE);
        imageViewPost.setImageBitmap(bm);
        remove.setVisibility(View.VISIBLE);
    }

    //arrow back
    private void arrowBack(){
        if (postIdEdit!=null){
            GoAcountActive();
        }else{
        if (writePost.getText().toString().equals("")&&imageViewPost.getDrawable()==null)
    {
        //here will put intent from ""home page"" and exchange by ""Toast""

        GoHome();
    }
                else {
        showAlertDialog();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (postIdEdit!=null){
            GoAcountActive();
        }
        else {
            if (writePost.getText().toString().equals("")&&imageViewPost.getDrawable()==null)
            {
                GoHome();
            }
            else {
                showAlertDialog();
            }
        }
    }

    //function of Alert Dialog
    private void showAlertDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //here will put intent from ""home page"" and exchange by ""finish"();
                GoHome();
            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    // intent for go the home main
    private void GoHome(){
        Intent home_main=new Intent(Create_Post.this,MainActivity.class);
        startActivity(home_main);
        finish();
    }
    // intent for go the home main
    private void GoAcountActive(){
        Intent home_main=new Intent(Create_Post.this,AccountActivity.class);
        startActivity(home_main);
        finish();
    }


    private void upload_post_pic(){
        if(filepath!=null){

            imageIdEdit=imagekey = UUID.randomUUID().toString();

            StorageReference ref = mStorageRef.child("PostImages/"+imagekey);
            ref.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dismissProgressDialog();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dismissProgressDialog();
                            Toast.makeText(Create_Post.this,"Post Photo uploading error",Toast.LENGTH_LONG).show();
                        }
                    });
        }
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
    protected void onStart() {
        super.onStart();
        postIdEdit=getIntent().getStringExtra("postId");
        postcontentEdit=getIntent().getStringExtra("postcontent");
        imageIdEdit=getIntent().getStringExtra("imageId");
        userId=getIntent().getStringExtra("userId");
        hasImageEdit=getIntent().getStringExtra("hasImage");

        System.out.println("-----------------------------------");
        System.out.println("postId "+imageIdEdit);
        System.out.println("-----------------------------------");
        if (postIdEdit!=null){
            CreatePost.setText("Edit");
            post.setText("Save");
            writePost.setText(postcontentEdit);
            if (imageIdEdit!=null){
                imageViewPost.setVisibility(View.VISIBLE);
                mStorageRef2 = FirebaseStorage.getInstance().getReference("PostImages/");
                mStorageRef2.child(imageIdEdit).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageViewPost.setImageBitmap(bm);
                        remove.setVisibility(View.VISIBLE);
                        if (postcontentEdit==null){
                            writePost.setHint("say something about this photo...");
                        }
                    }
                });
            }
        }
    }
}
