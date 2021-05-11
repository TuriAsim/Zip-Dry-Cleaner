package com.xumerz.dajisoft.zipsproject;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    Spinner spinnerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar= (Toolbar) findViewById(R.id.signup_toolbar);
        spinnerId = (Spinner) findViewById(R.id.selectid);
        String[] years = {"1996","1997","1998","1998"};
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years);
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinnerId.setAdapter(langAdapter);
        toolbar.setTitle("SignUp Activity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

    }
}
