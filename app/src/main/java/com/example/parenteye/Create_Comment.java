package com.example.parenteye;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.support.constraint.Constraints.TAG;

public class Create_Comment extends Activity {
    private Uri filepath;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference dbRef,dbRef2,dbRef3;
    ArrayList<PostComments> comments_of_post=new ArrayList<>(  );
    PostComments postComments;
    private RecyclerView recyclerView;
    private ArrayAdapterForComment mAdapter;
    String postId=null;
    ImageView noInernet;
    private String imagekey;
    private RelativeLayout cameraRelative,bottom_WriteComment;
    private EditText writeComment;
    private  ImageView btCamare,Image_of_gallery,cancelImage;
    private int SELECT_FILE = 1;
    private int CHECK_IMAGE=0;
    private StorageReference mStorageRef;
    private ProgressDialog progressdialogue;
    private int position;
    final long ONE_MEGABYTE = 1024 * 1024;
    private String CommentId,ContentComment;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_comment );
        recyclerView=findViewById( R.id.Post_recyclelistview);
        Image_of_gallery=findViewById( R.id.image_of_gallery );
        cameraRelative = findViewById( R.id.bottom_sheet_for_camera );
        bottom_WriteComment=findViewById( R.id.bottom_WriteComment );
        cameraRelative.setVisibility( View.GONE );
        progressBar = findViewById( R.id.progressBar );
        progressBar.setVisibility( View.GONE );
        writeComment=findViewById( R.id.writeComment );
        mStorageRef = FirebaseStorage.getInstance().getReference();
        registerForContextMenu(recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Create_Comment.this));
        dbRef2= FirebaseDatabase.getInstance().getReference().child("CommentsPost");
        dbRef3= FirebaseDatabase.getInstance().getReference().child("ReactionComment");

        final FirebaseUser currentUser = mAuth.getCurrentUser();

        postId=getIntent().getStringExtra("postId");

        writeComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= writeComment.getRight()) {
                        String cID= dbRef.push().getKey();
                        if (!(writeComment.getText().toString().isEmpty())||CHECK_IMAGE==1){
                            boolean hasImage=false;
                            if (CHECK_IMAGE==1)
                            {
                                cameraRelative.setVisibility(View.GONE);
                                upload_post_pic();
                                hasImage= true;
                                CHECK_IMAGE=0;
                            }
                            postComments=new PostComments(cID,writeComment.getText().toString(),currentUser.getUid(),postId,false,hasImage,imagekey,new getCurrentTime().getDateTime(),0,false);
                            dbRef.child( cID ).setValue( postComments );
                            writeComment.setText( "" );
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        btCamare= findViewById( R.id.btCamera );
        btCamare.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                galleryIntent();
            }
        } );

        cancelImage = findViewById(R.id.cancelImage);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraRelative.setVisibility(View.GONE);
                CHECK_IMAGE=0;
            }
        });

//        noInernet = findViewById( R.id.noInternet );
//        noInernet.setVisibility( View.GONE);


        postComments = new PostComments(  );
        dbRef= FirebaseDatabase.getInstance().getReference().child("CommentsPost");


      //  if (new InternetConnection().checkConnection( this )) {
            new getComment().execute(  );
        //} else {
          //  noInernet.setVisibility( View.VISIBLE );
        //}
/*


*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        CommentId = getIntent().getStringExtra("CommentId");
        ContentComment = getIntent().getStringExtra("CommentContent");
        if (!(CommentId==null&&ContentComment==null)) {
            dbRef2.child(CommentId).child("commentcontent").setValue(ContentComment);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
      //  int position = -1;
        try {
            position = ((ArrayAdapterForComment)recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        {
        switch (item.getItemId()) {
            case R.id.Delete:
                showAlertDialog();


                break;
            case R.id.Edit:
                UpdateComment();
                break;
        }
        }
        return super.onContextItemSelected(item);
    }

    private  void DeletComment() {
        dbRef2= FirebaseDatabase.getInstance().getReference().child("CommentsPost");
        dbRef3= FirebaseDatabase.getInstance().getReference().child("ReactionComment");
        String keyCoPost= comments_of_post.get(position).getCommentID();
        String keyRecId= position +comments_of_post.get(position ).getPostId()+comments_of_post.get( position ).getUserId();



        if (comments_of_post.get( position ).isDidLike()==true){
            dbRef3.child( keyRecId ).removeValue();
            dbRef2.child( keyCoPost ).removeValue();
        }
        else {
            dbRef2.child( keyCoPost ).removeValue();
        }

        comments_of_post.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position,comments_of_post.size());
    }

    private  void UpdateComment() {

        String keyCoPost= comments_of_post.get(position).getCommentID();

        Intent intent = new Intent( Create_Comment.this, Edit_Comment.class );
        intent.putExtra( "CommentId",keyCoPost );
        intent.putExtra( "CommentContent",comments_of_post.get(position).getCommentcontent() );
        intent.putExtra( "CommentUserId",comments_of_post.get(position).getUserId() );
        startActivity( intent );
        finish();
    }
    private void upload_post_pic(){
        if(filepath!=null){

            imagekey = UUID.randomUUID().toString();
            StorageReference ref = mStorageRef.child("CommentImages/"+imagekey);
            ref.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Create_Comment.this,"Post Photo uploading error",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    //function of Alert Dialog
    private void showAlertDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Delete Comment");
        builder.setMessage("Are you sure you want to Delete comment ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DeletComment();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                CHECK_IMAGE=requestCode;
            cameraRelative.setVisibility( View.VISIBLE );
            onSelectFromGalleryResult(data);
        }
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

        Image_of_gallery.setImageBitmap( bm );

    }


    class getComment extends AsyncTask<Void, Void, ArrayList<PostComments>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility( View.VISIBLE );

        }

        @Override
        protected ArrayList<PostComments> doInBackground(Void... voids) {


            dbRef = FirebaseDatabase.getInstance().getReference().child( "CommentsPost" );
            comments_of_post = new ArrayList<>();
            //  Query query=dbRef.orderByChild("PostId").equalTo( "LaSrDr3I6cDE93j1hU1" );
            dbRef.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments_of_post.clear();
                    for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                        PostComments postComments = commentSnapshot.getValue( PostComments.class );
                        comments_of_post.add( postComments );
                    }

                    if (comments_of_post.size()==0) {
                        progressBar.setVisibility( View.GONE );
                    }
                    else {
                        progressBar.setVisibility( View.GONE );
                        if (recyclerView.getAdapter() == null) {
                            mAdapter = new ArrayAdapterForComment( Create_Comment.this, comments_of_post );
                            recyclerView.setAdapter(mAdapter);


                        } else {
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

            return comments_of_post;
        }

         @Override
         protected void onPostExecute(ArrayList<PostComments> comments) {
             super.onPostExecute( comments );



         }
     }




 /*   public class InternetConnection {

        // CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
        public  boolean checkConnection(Context context) {
            final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    return true;
                }
            }
            return false;
        }
    }*/
}
