package com.xumerz.dajisoft.zipsproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    EditText name,pass;
    CheckBox chbox;
    Button login;
    SharedPreferences sharedPref;
    String bId;
    String uname,upass;
    String result = null;
    String token=null;
    public static final String TOKEN="token";
    public static final String MyPref = "mypref";
    SharedPreferences sharedpref;
    public static final String ISFIRSTTIME = "isFirstTime";
    public static final String EMAIL = "emailKey";
    public static final String PASSWORD = "passKey";
    public static final String CBOX = "chekKey";



    private static final String URL_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        toolbar= (Toolbar) findViewById(R.id.login_toolbar);
        name= (EditText) findViewById(R.id.username);
        pass= (EditText) findViewById(R.id.password);
        chbox= (CheckBox) findViewById(R.id.savebox);
        login= (Button) findViewById(R.id.login);
        sharedpref=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        sharedPref=getSharedPreferences(Select_Branches.Mypref, Context.MODE_PRIVATE);
        bId=sharedPref.getString(Select_Branches.BId, "");
        getContain();
        toolbar.setTitle("LogIn Acticity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        login.setOnClickListener(this);

        chbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                /*if (isChecked == true) {

                    getText();
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString(EMAIL, uname);
                    editor.putString(PASSWORD, upass);
                    editor.putBoolean(CBOX, isChecked);
                    editor.commit();
                    Toast.makeText(LogInActivity.this, "save password", Toast.LENGTH_SHORT).show();

                }
                else if (isChecked == false) {

                    SharedPreferences.Editor editor=sharedpref.edit();
                    //eCellno.setText("");
                    pass.setText("");
                    editor.clear();
                    editor.commit();

                }*/


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login:
                 getText();
                if(!uname.equals("")&& !upass.equals("")) {

                    if (NetworkCheck.getInstance(this).isOnline()) {

                        new LogIn().execute();


                    } else {

                        Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_LONG).show();

                    }

                }
                else {

                   Toast.makeText(LogInActivity.this,"Please Enter Email and Password",Toast.LENGTH_LONG).show();

                }
                break;




        }
    }



    public void getText(){

        uname=name.getText().toString();
        upass=pass.getText().toString();

    }



    //// if sharedPrefrence contain data
    public void getContain() {

        if (sharedpref.contains(EMAIL)) {
            name.setText(sharedpref.getString(EMAIL, ""));

        }
        if (sharedpref.contains(PASSWORD)) {
            pass.setText(sharedpref.getString(PASSWORD, ""));

        }
        if (sharedpref.contains(CBOX)) {

            boolean checkBoxValue = sharedpref.getBoolean(CBOX, false);
            if (checkBoxValue) {
                chbox.setChecked(true);

            } else {

                chbox.setChecked(false);

            }

        }
    }


    ///////// LOGIN AUTHENTICATION
    public class LogIn extends AsyncTask<Void,Void,String>
    {

        private ProgressDialog pD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pD = new ProgressDialog(LogInActivity.this);
            pD.setMessage("Loading...");
            pD.setTitle("Checking User Authentication");
            pD.setIndeterminate(false);
            pD.setCancelable(true);
            pD.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            URL url=null;
            try {

                url = new URL(URL_LOGIN);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setRequestMethod("POST");

                JSONObject logjson=new JSONObject();

                logjson.put("BranchID",bId);
                logjson.put("Email", uname);
                logjson.put("Password", upass);

                Log.e("json object", logjson.toString());

                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                wr.write(logjson.toString());
                Log.e("value send", logjson.toString());
                wr.flush();
                wr.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));

                String line =" ";
                //display what returns the POST request
                StringBuilder sb = new StringBuilder();
                int HttpResult = connection.getResponseCode();

                if (HttpResult == HttpURLConnection.HTTP_OK) {

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    // bufferedReader.close();
                    result = sb.toString().replaceAll(" ", " ");

                    JSONObject object=new JSONObject(result);
                    token=object.getString("token");

                    Log.e("boolean value",""+token);

                    Log.e("result value",result);
                    Log.e("Http result", String.valueOf(HttpResult));
                    Log.e("Http response message",connection.getResponseMessage());
                    Log.e("Http response content",""+connection.getContent());

                    return  result;
                }

                else
                {
                    Log.e("result value",result);
                    Log.e("Http result", String.valueOf(HttpResult));
                    Log.e("Http response message",connection.getResponseMessage());
                    Log.e("Http response content",""+connection.getContent());
                    return result;
                }


            }

            catch (MalformedURLException e) {
                result=e.getMessage();
            } catch (UnsupportedEncodingException e) {
                result=e.getMessage();
            } catch (IOException e) {
                result=e.getMessage();
            } catch (JSONException e) {
                result=e.getMessage();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("token value", "" + token);
             getText();
            if (token!=null)
            {

                if (chbox.isChecked()) {

                    getText();
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString(EMAIL, uname);
                    editor.putString(PASSWORD, upass);
                    editor.putBoolean(CBOX, chbox.isChecked());
                    editor.commit();
                    Toast.makeText(LogInActivity.this, "save password", Toast.LENGTH_SHORT).show();

                }

                Intent i=new Intent(LogInActivity.this,MainActivity.class);
                i.putExtra("FIRST",true);
                i.putExtra("E",uname);
                i.putExtra("P",upass);
                startActivity(i);
                Toast.makeText(LogInActivity.this,"Login successfull",Toast.LENGTH_LONG).show();
                pD.dismiss();
            }

            else {
                pD.dismiss();
                Toast.makeText(LogInActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Select_Branches.class));
    }
}
