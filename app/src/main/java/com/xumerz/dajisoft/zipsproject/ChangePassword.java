package com.xumerz.dajisoft.zipsproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChangePassword extends AppCompatActivity {

    Toolbar toolbar;
    Button savepass;
    EditText oldpass,newpass,confirmpass;
    String pass,cpass,oldPass;
    String result = null;
    SharedPreferences sharedpref;
    String tokenId;
    String getoldPass;


    //private static final String URL_PASS= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        savepass= (Button) findViewById(R.id.savepass);
        toolbar= (Toolbar) findViewById(R.id.login_toolbar);
        newpass= (EditText) findViewById(R.id.newpass);
        oldpass= (EditText) findViewById(R.id.oldpass);
        confirmpass= (EditText) findViewById(R.id.confirmpass);
        sharedpref=getSharedPreferences(LogInActivity.MyPref, Context.MODE_PRIVATE);
        tokenId=sharedpref.getString(MainActivity.TOKEN, "");
        //getoldPass=sharedpref.getString(LogInActivity.PASSWORD,"");



        getText();

        toolbar.setTitle("Change Password");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        savepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getText();
               /* if(!oldPass.equals(getoldPass))
                {

                    Toast.makeText(ChangePassword.this,"OldPassword is Not Correct",Toast.LENGTH_LONG).show();

                }*/
                if(!pass.equals(cpass))
                {

                    Toast.makeText(ChangePassword.this,"Password Not Matching",Toast.LENGTH_LONG).show();
                }
                else if(pass.equals("") && cpass.equals(""))
                {
                    Toast.makeText(ChangePassword.this,"Please Enter New and Confirm Password",Toast.LENGTH_LONG).show();
                }
                else {

                    new ChangedPassword().execute();

                }
            }
        });

    }

    public void getText()
    {
        oldPass=oldpass.getText().toString();
        pass=newpass.getText().toString();
        cpass=confirmpass.getText().toString();



    }


    ///////// Password Changed class
    public class ChangedPassword extends AsyncTask<Void,Void,String>
    {

        String idResult="";
        private ProgressDialog pD;
        String data=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pD = new ProgressDialog(ChangePassword.this);
            pD.setMessage("Loading...");
            pD.setTitle("Checking For Change Password");
            pD.setIndeterminate(false);
            pD.setCancelable(true);
            pD.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            URL url=null;
            HttpURLConnection connection=null;

            try {
                url = new URL("");
                connection = (HttpURLConnection) url.openConnection();
                Log.e("URL",url.toString());
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setRequestProperty("token", tokenId);
                Log.e("token",tokenId);
                connection.setRequestMethod("GET");
                int HttpResult = 0;

                HttpResult = connection.getResponseCode();

                Log.e("httpresult before", "" + HttpResult);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";

                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                    // Log.e("select branch",line);
                }

                idResult = sb.toString().replaceAll(" ", " ");
                idResult = sb.toString();
                Log.e("device id result", idResult);

                JSONObject jsonObject=new JSONObject(idResult);

                 data = jsonObject.getString("value");
                Log.e("datqa is",data);





            }
            catch (MalformedURLException e)
            {
                Log.e("exception",e.getMessage());
            }
            catch (ProtocolException ex)
            {
                Log.e("protocol except",ex.getMessage());

            }
             catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Log.e("token value", "" + token);

            if (data.equals("Password changed successfully"))
            {

               SharedPreferences preferences = getSharedPreferences(LogInActivity.MyPref, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences.edit();
                    editor1.clear();
                    editor1.commit();
                    startActivity(new Intent(ChangePassword.this, Select_Branches.class));
                    Toast.makeText(ChangePassword.this," Password Change Successfully",Toast.LENGTH_LONG).show();
            }

            else {
                pD.dismiss();
                Toast.makeText(ChangePassword.this,"Please Enter Correct Old Password",Toast.LENGTH_SHORT).show();

            }
            pD.dismiss();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        ((MainActivity)MainActivity.myContext).finish();
        //((ResultReceiver)getIntent().getExtra("finisher")).send(1, new Bundle());

    }
}
