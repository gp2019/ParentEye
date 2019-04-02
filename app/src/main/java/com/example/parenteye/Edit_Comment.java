package com.example.parenteye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

public class Edit_Comment extends AppCompatActivity implements View.OnClickListener {


    private int length;
    private EditText EditComment;
    private Button cancel,update;
 //   private ImageView profile_User;
 //   private DatabaseReference dbRef,dbRef2;
    private StorageReference msReference;
    private String CommentId,ContentComment;//,UserId;
    private ImageView arrow_back;
    final long ONE_MEGABYTE = 1024 * 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__comment);
        EditComment = findViewById(R.id.EditwriteComment);
        cancel = findViewById(R.id.cancelEdit);
        update = findViewById(R.id.UpdateEdit);
        arrow_back = findViewById(R.id.arrow_back);
      //  profile_User= findViewById(R.id.imageUser);

        CommentId= getIntent().getStringExtra("CommentId");
        ContentComment = getIntent().getStringExtra("CommentContent");
       // UserId = getIntent().getStringExtra("CommentUserId");
        EditComment.setText(ContentComment);

        length = EditComment.getText().toString().trim().length();

        //     dbRef2= FirebaseDatabase.getInstance().getReferenceFromUrl("https://parenteye-1b448.firebaseio.com/").child("CommentsPost");


        EditComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (EditComment.getText().toString().trim().length()!=0){
                    update.setText("Update");
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UpdateComment();
                        }
                    });
                    length = EditComment.getText().toString().trim().length();
                }
                else {
                    update.setHint("Update");
                }
            }
        });


        cancel.setOnClickListener(this);
        arrow_back.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        int id=view.getId();
        switch (id){
            case R.id.cancelEdit:
                GoComment();
                break;
            case R.id.arrow_back:
                GoComment();
                break;
        }
    }

    private void UpdateComment(){
        if (EditComment.getText().toString().trim().length()!=0) {
            Intent intent = new Intent( Edit_Comment.this, Create_Comment.class );
            intent.putExtra( "CommentId",CommentId );
            intent.putExtra( "CommentContent",EditComment.getText().toString() );
            startActivity( intent );
            finish();
        }

    }

    private void GoComment()
    {
        Intent intent = new Intent( Edit_Comment.this, Create_Comment.class );
        startActivity( intent );
        finish();
    }
}
