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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CustomerPayment extends AppCompatActivity {


    Toolbar toolbar;
    SharedPreferences sharedPref;
    String token=null;
    ListView paymentlistview;
    static List<Model> list;
    Model m;

    private static final String URL_LOGIN = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        toolbar= (Toolbar) findViewById(R.id.customer_toolbar);
        paymentlistview= (ListView) findViewById(R.id.paymentlist);
        sharedPref=getSharedPreferences(MainActivity.MyPref, Context.MODE_PRIVATE);
        token=sharedPref.getString(MainActivity.TOKEN, "");
        list=new ArrayList<>();
        toolbar.setTitle("Payment Activity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        if (NetworkCheck.getInstance(this).isOnline()) {

            new CustomerPay().execute();

        } else {

            Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_LONG).show();

        }




    }

    ///////// payment Class

    public class CustomerPay extends AsyncTask<Void, Void, String> {
        String idResult = "";
        private ProgressDialog pD;
        String message="";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(CustomerPayment.this);
            pD.setMessage("Please wait");
            pD.setTitle("Payment Details loading...");
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
                JSONArray jsonArray=jsonObject.getJSONArray("value");
                for (int i=0;i<jsonArray.length();i++) {

                    m=new Model();
                    JSONObject innerobj=jsonArray.getJSONObject(i);
                    String date = innerobj.getString("PaymentDate");
                    String amount = innerobj.getString("PaymentAmount");
                    String description = innerobj.getString("PaymentDescription");
                    String parts = date.replaceAll("T", " ");
                    String part1 = parts.substring(0, 19);

                    Log.e("dateis",part1);
                    Log.e("amount", amount);
                    Log.e("description",description);


                    m.setDate(part1);
                    m.setAmount(amount);
                    m.setType(description);



                    list.add(m);
                    message = "1";

                }
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
                Collections.sort(list, new ModelComprator());
               PaymentAdapter paymentAdapter = new PaymentAdapter(CustomerPayment.this, list);
                paymentlistview.setAdapter(paymentAdapter);
                pD.dismiss();
            }
            else {
                pD.dismiss();
                Toast.makeText(CustomerPayment.this, "No Data Avialable", Toast.LENGTH_SHORT).show();
            }


        }

    }


    public class ModelComprator implements Comparator<Model> {

        Date d1,d2;


        @Override
        public int compare(Model lhs, Model rhs) {



            String s1 = lhs.getDate();
            String s2 = rhs.getDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                d1 = simpleDateFormat.parse(s1);
                d2=simpleDateFormat.parse(s2);

            }
            catch (ParseException ex)
            {
                System.out.println("Exception "+ex);
            }




            if (d2.compareTo(d1) < 0) {

                return -1;

            } else if (d2.compareTo(d1) > 0) {

                return 1;

            } else {

                return 0;
            }
        }

    }



}
