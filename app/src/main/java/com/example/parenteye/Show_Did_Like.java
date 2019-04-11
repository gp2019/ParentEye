package com.example.parenteye;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Show_Did_Like extends AppCompatActivity implements View.OnClickListener {

    private  String PostId;
    private ProgressBar progressBar;
    ArrayList<ReactionPosts> reaction;
    private DatabaseReference dbRef;
    private ImageView arrow_back;
    private RecyclerView recyclerView;
    private ArrayAdapterForShowDidLike mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_did_like);
        progressBar = findViewById( R.id.progressBar );
        progressBar.setVisibility( View.GONE );
        arrow_back=findViewById(R.id.arrow_back);
        recyclerView=findViewById( R.id.User_recyclelistview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Show_Did_Like.this));
        recyclerView.setHasFixedSize(true);
        PostId = getIntent().getStringExtra("PostId");

        arrow_back.setOnClickListener(this);
        new getReaction().execute(  );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Go_Back_Create_Comment();
    }

    @Override
    public void onClick(View view) {
       int id=view.getId();
       switch (id){
           case R.id.arrow_back:
               Go_Back_Create_Comment();
           break;
       }
    }

    private void Go_Back_Create_Comment() {
        Intent goMain = new Intent(this,Create_Comment.class);
        startActivity(goMain);
        finish();
    }

    class getReaction extends AsyncTask<Void, Void, ArrayList<ReactionPosts>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility( View.VISIBLE );

        }

        @Override
        protected ArrayList<ReactionPosts> doInBackground(Void... voids) {
            reaction = new ArrayList<>();

            dbRef = FirebaseDatabase.getInstance().getReference().child( "ReactionComment_Post" );
            //  Query query=dbRef.orderByChild("PostId").equalTo( "LaSrDr3I6cDE93j1hU1" );
            dbRef.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reaction.clear();
                    for (DataSnapshot RectionSnapshot : dataSnapshot.getChildren()) {
                        ReactionPosts reactionPosts = RectionSnapshot.getValue( ReactionPosts.class );

                        if (reactionPosts.getReactionId()!=null){
                            if(reactionPosts.getPostORCommentId().equals(PostId)){
                                reaction.add( reactionPosts );
                            }
                        }
                    }

                    if (reaction.size()==0) {
                        progressBar.setVisibility( View.GONE );
                    }
                    else {
                        progressBar.setVisibility( View.GONE );
                        if (recyclerView.getAdapter() == null) {
                            mAdapter = new ArrayAdapterForShowDidLike( Show_Did_Like.this, reaction );
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

            return reaction;
        }

    }

}
