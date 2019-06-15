package com.example.parenteye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class side_menu_bar extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button ic_makePage;
    private Button ic_makeGroup;
    private Button ic_myChildren;
    private Button ic_activity_log;
    private Button ic_logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu_bar);


        mAuth = FirebaseAuth.getInstance();

        ic_makePage =(Button)findViewById(R.id.ic_makePage) ;
        ic_makeGroup =(Button)findViewById(R.id.ic_makeGroup) ;
        ic_myChildren =(Button)findViewById(R.id.ic_myChildren) ;
        ic_activity_log =(Button)findViewById(R.id.ic_activity_log) ;
        ic_logOut =(Button)findViewById(R.id.ic_logOut) ;

        ic_makePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPageIntent=new Intent(side_menu_bar.this,AddPageActivity.class);
                startActivity(addPageIntent);
                //finish();
            }
        });

        ic_makeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MakeGroupIntent=new Intent(side_menu_bar.this,MakeGroupActivity.class);
                startActivity(MakeGroupIntent);
                //finish();
            }
        });

        ic_myChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MyChildrenIntent=new Intent(side_menu_bar.this,MyChildrenActivity.class);
                startActivity(MyChildrenIntent);
                //finish();
            }
        });

        ic_activity_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_logIntent=new Intent(side_menu_bar.this,FragmentPreview.class);
                startActivity(activity_logIntent);
                //finish();
            }
        });

        ic_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    main_login();

                }
            }
        });


    }
    private void main_login() {
        Intent main_login = new Intent(side_menu_bar.this, LoginActivity.class);
        startActivity(main_login);
        finish();

    }
}
