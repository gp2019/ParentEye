package com.example.parenteye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewImage extends AppCompatActivity {

    private ImageView VImage;
    private TextView userName,Time;
    private StorageReference mStorageRef;
    final long ONE_MEGABYTE = 1024 * 1024;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        VImage=findViewById(R.id.VImage);
        userName=findViewById(R.id.userName);
        Time=findViewById(R.id.time);
        mStorageRef = FirebaseStorage.getInstance().getReference("CommentImages/");

        progressBar = findViewById( R.id.progressBar );
        progressBar.setVisibility( View.GONE );

        new getImage().execute();


    }


    class getImage extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {


            final Intent in = getIntent();
            mStorageRef.child(in.getStringExtra("image")).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    final Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    progressBar.setVisibility(View.GONE);
                    VImage.setImageBitmap(bm);
                    userName.setText(in.getStringExtra("userName"));
                    Time.setText("ago "+in.getStringExtra("time"));
                }
            });
            return null;
        }

    }
}
