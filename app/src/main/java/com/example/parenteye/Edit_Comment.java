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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Edit_Comment extends AppCompatActivity implements View.OnClickListener {


    private  int length;
    private EditText EditComment;
    private Button cancel,update;
    DatabaseReference dbRef2;
    private StorageReference msReference;
    private String CommentId,ContentComment;
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

        dbRef2= FirebaseDatabase.getInstance().getReference("CommentsPost");
        CommentId= getIntent().getStringExtra("CommentId");
        ContentComment = getIntent().getStringExtra("CommentContent");
        EditComment.setText(ContentComment);

        length = EditComment.getText().toString().trim().length();



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
            if (!(CommentId==null&&ContentComment==null)) {
                dbRef2.child(CommentId).child("commentcontent").setValue(EditComment.getText().toString());
             GoComment();
            }

        }

    }

    private void GoComment()
    {
        //Intent intent = new Intent( Edit_Comment.this, Create_Comment.class );
        //startActivity( intent );
        finish();
    }
}
