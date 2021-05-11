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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomerProfileActivity extends AppCompatActivity {


    Toolbar toolbar;
    SharedPreferences sharedPref;
    String token=null;
    Model m;
    TextView lname,fname,email,cphone,caddress,czips,cprepaidbil,ccreditinfo;

    private static final String URL_LOGIN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        toolbar= (Toolbar) findViewById(R.id.login_toolbar);
        fname= (TextView) findViewById(R.id.firstname);
        lname= (TextView) findViewById(R.id.lastname);
        email= (TextView) findViewById(R.id.email);
        cphone= (TextView) findViewById(R.id.hphone);
        caddress= (TextView)findViewById(R.id.address);
        czips= (TextView) findViewById(R.id.zips);
        cprepaidbil= (TextView) findViewById(R.id.prepaidbil);
        ccreditinfo= (TextView) findViewById(R.id.creditinfo);
        sharedPref=getSharedPreferences(LogInActivity.MyPref, Context.MODE_PRIVATE);
        token=sharedPref.getString(LogInActivity.TOKEN, "");
        toolbar.setTitle("Customer Activity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        if (NetworkCheck.getInstance(this).isOnline()) {

            new Customer().execute();

        } else {

            Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_LONG).show();

        }


    }

    ///////// Customer Class

    public class Customer extends AsyncTask<Void, Void, String> {
        String idResult = "";
        private ProgressDialog pD;
        String message="";
        String fName,lName,cEmail,phone,address,zips,prepaidbail,creditinfo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(CustomerProfileActivity.this);
            pD.setMessage("Please wait");
            pD.setTitle("Customer Details loading...");
            pD.setIndeterminate(false);
            pD.setCancelable(false);
            pD.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            URL url = null;
            try {
                url = new URL(URL_LOGIN);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setRequestProperty("token",token);
                connection.setRequestMethod("GET");
                int response = connection.getResponseCode();
                Log.e("responce", "" + response);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));


                String line = "";

                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    // Log.e("select branch",line);
                }

                idResult = sb.toString().replaceAll(" ", " ");
                idResult = sb.toString();
                Log.e("device id result", idResult);


                JSONObject jsonObject=new JSONObject(idResult);
                 fName=jsonObject.getString("FirstName");
                 lName=jsonObject.getString("LastName");
                 cEmail=jsonObject.getString("Email");
                 phone=jsonObject.getString("HomePhone");
                 address=jsonObject.getString("Address");
                 zips=jsonObject.getString("Zip");
                 prepaidbail=jsonObject.getString("PrepaidBal");



                message="1";


            }
            catch (Exception e) {
                Log.e("exception", e.getMessage());
                message=e.getMessage();
            }
            return message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pD.dismiss();
            if(s.equals("1")) {
                fname.setText(fName);
                lname.setText(lName);
                email.setText(cEmail);
                int count=phone.length();
                String f=phone.substring(0,3);
                String se=phone.substring(3,6);
                String th=phone.substring(6,count);
                cphone.setText("("+f+")"+se+"-"+th);
                caddress.setText(address);
                czips.setText(zips);
                cprepaidbil.setText("$"+prepaidbail);


                pD.dismiss();
                Toast.makeText(CustomerProfileActivity.this, "Customer Profile", Toast.LENGTH_SHORT).show();
            }
            else {
                pD.dismiss();
                Toast.makeText(CustomerProfileActivity.this, "No Data Avialable", Toast.LENGTH_SHORT).show();
            }


        }

        }


/*
    @Override
    protected void onPause() {
        super.onPause();
        CustomerProfileActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
       i.putExtra("FIRST",false);
        startActivity(i);

    }*/
}
