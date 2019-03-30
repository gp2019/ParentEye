package com.example.parenteye;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class Create_Comment extends Activity {
    private Uri filepath;
    ProgressBar progressBar;
    DatabaseReference dbRef;
    ArrayList<PostComments> comments_of_post=new ArrayList<>(  );
    PostComments postComments;
    private RecyclerView recyclerView;
    private ArrayAdapterForComment mAdapter;
    String postId=null;
    ImageView noInernet;
   private RelativeLayout cameraRelative,bottom_WriteComment;
   private EditText writeComment;
   private  ImageView btCamare,Image_of_gallery;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    final long ONE_MEGABYTE = 1024 * 1024;
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

        writeComment.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable){

   if (writeComment.getText().toString().trim().length()!=0){
    writeComment.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= writeComment.getRight()) {
                    String cID= dbRef.push().getKey();
                    if (writeComment.getText().toString().trim().length()!=0){
                    postComments=new PostComments(cID,writeComment.getText().toString(),"memzoiT3c2TbPsACnDMNcl7jnrs2","LaSrDr3I6cDE93j1hU1",false,false,null,new getCurrentTime().getDateTime(),0,false);
                    dbRef.child( cID ).setValue( postComments );
                    writeComment.setText( "" );
                    return true;
                    }
                }
            }
            return false;
        }
    });
}
            }
        } );

        btCamare= findViewById( R.id.btCamera );
        btCamare.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottom_WriteComment.setVisibility( View.GONE );
                cameraRelative.setVisibility( View.VISIBLE );
                galleryIntent();
            }
        } );


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
                    mAdapter = new ArrayAdapterForComment( Create_Comment.this, comments_of_post );
                    // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    //recyclerView.setLayoutManager(mLayoutManager);

                    if (comments_of_post.size()==0) {
                        progressBar.setVisibility( View.GONE );
                    }
                    else {
                        progressBar.setVisibility( View.GONE );
                        if (recyclerView.getAdapter() == null) {
                            recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext(), LinearLayoutManager.VERTICAL, false ) );
                            recyclerView.setItemAnimator( new DefaultItemAnimator() );
                            recyclerView.setAdapter( mAdapter );
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
