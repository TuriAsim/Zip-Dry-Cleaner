package com.xumerz.dajisoft.zipsproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {



    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer=new Thread() {

            @Override
            public void run() {
                try{

                    sleep(3000);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

                }
                finally {

                    SharedPreferences app_preferences =getSharedPreferences(LogInActivity.MyPref, Context.MODE_PRIVATE);

                    Log.e("appprefp", "" + app_preferences);
                   email=app_preferences.getString(LogInActivity.EMAIL,"");
                    password=app_preferences.getString(LogInActivity.PASSWORD,"");

                    Log.e("isfirsttimetrue", "" +email);

                    if (!email.equals("") && !password.equals("")) {

                        Intent i=new Intent(SplashActivity.this,MainActivity.class);
                        i.putExtra("FIRST",true);
                        startActivity(i);

                        Log.e("isfirsttimetrue", "" + email);


                    }
                    else{


                        startActivity(new Intent(SplashActivity.this, Select_Branches.class));
                        Log.e("isfirsttimefalse", "" + email);
                    }



                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
