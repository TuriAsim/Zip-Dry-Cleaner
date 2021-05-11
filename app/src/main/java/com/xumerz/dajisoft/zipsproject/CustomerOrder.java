package com.xumerz.dajisoft.zipsproject;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CustomerOrder extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    EditText edfdate, edtodate;
    Button search;
    private int mYear, mMonth, mDay;
    String tokenId = "";
    SharedPreferences sharedPref;
    Date myDate;
    Calendar calendar;
    String dateAsString;
    ListView ordertlistview;
    static List<Model> list;
    Model m;
    OrderAdapter orderAdapter;


    private static final String URL_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        ordertlistview = (ListView) findViewById(R.id.orderlist);
        list = new ArrayList<>();
        sharedPref = getSharedPreferences(MainActivity.MyPref, Context.MODE_PRIVATE);
        tokenId = sharedPref.getString(MainActivity.TOKEN, "");
        toolbar.setTitle("Order Activity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        myDate = new Date();
        setValue();
        setTimeDate();

    }


    public void setValue() {

        edfdate = (EditText) findViewById(R.id.fromdate);
        edtodate = (EditText) findViewById(R.id.todate);
        search = (Button) findViewById(R.id.search);
        search = (Button) findViewById(R.id.search);
        edfdate.setOnClickListener(this);
        edtodate.setOnClickListener(this);
        search.setOnClickListener(this);


    }

    //// default date and time set to edittext

    public void setTimeDate() {
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(myDate);
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyy-MM-dd");
        dateAsString = outputFmt.format(time);
        edfdate.setText(dateAsString);
        edtodate.setText(dateAsString);
        Log.e("current date", dateAsString);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fromdate:

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                String m,y,d;

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if(month < 10){

                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }


                                    edfdate.setText(year + "-" +formattedMonth + "-" + formattedDayOfMonth);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                break;

            case R.id.todate:


                final Calendar to = Calendar.getInstance();
                mYear = to.get(Calendar.YEAR);
                mMonth = to.get(Calendar.MONTH);
                mDay = to.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog toPickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if(month < 10){

                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }




                                edtodate.setText(year + "-" +formattedMonth+ "-" + formattedDayOfMonth);


                            }
                        }, mYear, mMonth, mDay);
                toPickerDialog.show();


                break;


            case R.id.search:

                String CurrentDate = edfdate.getText().toString();
                String FinalDate = edtodate.getText().toString();
                 list.clear();

                if (NetworkCheck.getInstance(this).isOnline()) {

                    new OrderHistory().execute();

                } else {

                    Toast.makeText(getApplicationContext(),"Network Connection Error",Toast.LENGTH_LONG).show();

                }
                break;


        }
    }


    ////////////// Location history by selecting time and date
    public class OrderHistory extends AsyncTask<Void, Void, String> {
        String data = "";
        ProgressDialog pd;
        String message = "";
        String from = edfdate.getText().toString();
        String to = edtodate.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CustomerOrder.this);
            pd.setMessage("Loading....");
            pd.setCancelable(true);
            pd.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                URL url = new URL("");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestProperty("token", tokenId);
                urlConnection.setRequestMethod("GET");

                BufferedReader rdr = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = rdr.readLine()) != null) {
                    builder.append(line);
                }
                data = builder.toString().replaceAll(" ", "");
                Log.e("data order", data);

                JSONObject object = new JSONObject(data);
                JSONArray jsonArray = object.getJSONArray("value");
                Log.e("json array", "" + jsonArray);
                for (int i = 0; i < jsonArray.length(); i++) {
                    m = new Model();
                    JSONObject innerob = jsonArray.getJSONObject(i);
                    String ticket = innerob.getString("Ticket");
                    String depname = innerob.getString("DeptName");
                    String Orderstatus = innerob.getString("OrderStatus");
                    String total = innerob.getString("TotalDue");


                    m.setTicketno(ticket);
                    m.setDeptName(depname);
                    m.setOrderStatus(Orderstatus);
                    m.setGrandTotal(total);

                    String vlaue = m.getTicketno();

                    Log.e("tecket", ticket);
                    Log.e("depname", depname);
                    Log.e("total", total);
                    Log.e("order", Orderstatus);

                    list.add(m);
                    message = "1";
                }


            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                message = e.getMessage();
            }


            return message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("s", s);
            if (s.equals("1")) {

               Collections.sort(list,new ModelComprator());
                 orderAdapter = new OrderAdapter(CustomerOrder.this, list);
                ordertlistview.setAdapter(orderAdapter);
                pd.dismiss();

            } else {
                Toast.makeText(CustomerOrder.this, "No Data Available OR Network Error", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }


        }

    }



    ///// inner class

    public class ModelComprator implements Comparator<Model> {


        @Override
        public int compare(Model lhs, Model rhs) {

            Double ticket1 = Double.valueOf(lhs.getTicketno());
            Double ticket2 = Double.valueOf(rhs.getTicketno());

            if (ticket2.compareTo(ticket1) < 0) {

                return -1;

            } else if (ticket2.compareTo(ticket1) > 0) {

                return 1;

            } else {

                return 0;
            }
        }

    }
}