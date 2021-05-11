package com.xumerz.dajisoft.zipsproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class  MainActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button customer,payment,passchange,order;
    public static final String MyPref = "mypref";
    SharedPreferences sharedpref;
    SharedPreferences sharedPreferences;
    String bid,email,password;
    String token=null;
    public static final String TOKEN = "token";
    boolean first=false;
    public static Context myContext;


    private static final String URL_LOGIN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      toolbar= (Toolbar) findViewById(R.id.customer_toolbar);
        customer= (Button) findViewById(R.id.customer);
        payment= (Button) findViewById(R.id.payment);
        passchange= (Button) findViewById(R.id.chngpass);
       order= (Button) findViewById(R.id.order);
        sharedpref=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        sharedPreferences=getSharedPreferences(LogInActivity.MyPref, Context.MODE_PRIVATE);
        email=sharedPreferences.getString(LogInActivity.EMAIL, "");
        password=sharedPreferences.getString(LogInActivity.PASSWORD, "");
        sharedPreferences=getSharedPreferences(Select_Branches.Mypref, Context.MODE_PRIVATE);
        bid=sharedPreferences.getString(Select_Branches.BId, "");
        toolbar.setTitle("MainActivity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        customer.setOnClickListener(this);
        payment.setOnClickListener(this);
        passchange.setOnClickListener(this);
        order.setOnClickListener(this);
        myContext=this;

        ////// network checking

        if (NetworkCheck.getInstance(this).isOnline()) {

            Toast.makeText(getApplicationContext(),"Network Connected",Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_LONG).show();

        }


        if(email.equals("") && password.equals("")){

            email=getIntent().getExtras().getString("E");
            password=getIntent().getExtras().getString("P");

            Log.e("first vlaue",""+email);

        }

        first=getIntent().getExtras().getBoolean("FIRST");
        Log.e("first vlaue",""+first);

        if(first==true) {

            new MainScreen().execute();

        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.customer:

                startActivity(new Intent(MainActivity.this,CustomerProfileActivity.class));

                break;
            case R.id.payment:
                startActivity(new Intent(MainActivity.this,CustomerPayment.class));

                break;
            case R.id.chngpass:
                //startActivity(new Intent(MainActivity.this,ChangePassword.class));

                Intent GotoB=new Intent(MainActivity.this,ChangePassword.class);
                GotoB.putExtra("finisher", new ResultReceiver(null) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        MainActivity.this.finish();
                    }
                });
                startActivityForResult(GotoB,1);


                break;
            case R.id.order:
                startActivity(new Intent(MainActivity.this,CustomerOrder.class));


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icon_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:  //other menu items if you have any
                SharedPreferences preferences = getSharedPreferences(LogInActivity.MyPref, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences.edit();
                editor1.clear();
                editor1.commit();
                startActivity(new Intent(MainActivity.this, Select_Branches.class));
                Toast.makeText(this, "logout Successfully", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }


        return true;
    }




    ///////// LOGIN AUTHENTICATION
    public class MainScreen extends AsyncTask<Void,Void,String>
    {

        private ProgressDialog pD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pD = new ProgressDialog(MainActivity.this);
            pD.setMessage("Loading...");
            pD.setTitle("Please Wait....");
            pD.setIndeterminate(false);
            pD.setCancelable(true);
            pD.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            URL url=null;
            String result=null;
            try {

                url = new URL(URL_LOGIN);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.setRequestMethod("POST");

                JSONObject logjson=new JSONObject();

                logjson.put("BranchID",bid);
                logjson.put("Email", email);
                logjson.put("Password",password);

                Log.e("json object", logjson.toString());

                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                wr.write(logjson.toString());
             //   Log.e("value send", logjson.toString());
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


                    return  result;
                }

                else
                {

                    return result;
                }


            }

            catch (Exception e) {
                result=e.getMessage();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("token value", "" + token);

            if (token!=null)
            {
                SharedPreferences.Editor editor=sharedpref.edit();
                editor.putString(TOKEN,token);
                editor.commit();
                Toast.makeText(MainActivity.this,"MAIN ACTIVITY",Toast.LENGTH_LONG).show();
                pD.dismiss();
            }

            else {
                pD.dismiss();
                Toast.makeText(MainActivity.this,"Network Error",Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.this.finish();
    }



}
