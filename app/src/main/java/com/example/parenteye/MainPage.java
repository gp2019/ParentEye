
package com.example.parenteye;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;

public class MainPage extends AppCompatActivity {
    private Button main_signup;
    private Button main_login;

    ConstraintLayout constraintLayout;
    TextView tvTimeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        constraintLayout=findViewById(R.id.container);
        tvTimeMsg=findViewById(R.id.tv_time_msg);
        Calendar c = Calendar.getInstance();
        int timeOfDay= c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay>=0 && timeOfDay<12){
            //morning
            constraintLayout.setBackground(getDrawable(R.drawable.good_morning_img));
            tvTimeMsg.setText("Good Morning");
        }
        else if(timeOfDay>=12 && timeOfDay<16){
            //afternoon

            constraintLayout.setBackground(getDrawable(R.drawable.good_morning_img));
            tvTimeMsg.setText("Good Afternoon");


        }
        else if(timeOfDay>=16 && timeOfDay<121){
            //evening
           constraintLayout.setBackground(getDrawable(R.drawable.good_night_img));
            tvTimeMsg.setText("Good Evening");
        }
        else if(timeOfDay>=21 && timeOfDay<24){
            //night
            constraintLayout.setBackground(getDrawable(R.drawable.good_night_img));
            tvTimeMsg.setText("Good Night");
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        main_signup = (Button) findViewById(R.id.main_signup);

        main_login = (Button) findViewById(R.id.main_login);


        main_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainSignupIntent=new Intent(MainPage.this,SignupActivity.class);
                startActivity(mainSignupIntent);
                finish();
            }
        });
        main_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainLoginIntent=new Intent(MainPage.this,LoginActivity.class);
                startActivity(mainLoginIntent);
                finish();
            }
        });


    }

}
