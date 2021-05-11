package com.xumerz.dajisoft.zipsproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Select_Branches extends AppCompatActivity {

    Button next;
    Toolbar toolbar;
    Spinner spinnerId;
    static List<Model> list;
    String id, name;
    Model m;
    // ListView listView;
    GridView listView;
    public static final String BId = "bId";
    public static final String Mypref = "mypref";
    SharedPreferences sharedPref;

    private static final String URL_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__branches);

        toolbar = (Toolbar) findViewById(R.id.signup_toolbar);
        spinnerId = (Spinner) findViewById(R.id.selectid);
        listView = (GridView) findViewById(R.id.listview);
        sharedPref = getSharedPreferences(Mypref, Context.MODE_PRIVATE);
        toolbar.setTitle("SignUp Activity");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView branchid = (TextView) view.findViewById(R.id.branchid);
                String bid = branchid.getText().toString();
                Log.e("bid", bid);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(BId, bid);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));

            }
        });

        if (NetworkCheck.getInstance(this).isOnline()) {

            new BranchName().execute();
            Toast.makeText(getApplicationContext(), "You are online", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.branch_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:  //other menu items if you have any

                list.clear();
                if (NetworkCheck.getInstance(this).isOnline()) {

                    new BranchName().execute();
                    Toast.makeText(getApplicationContext(), "Network Connected", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_LONG).show();

                }
                break;
        }
return true;
    }


    ///////// Branches Class API

    public class BranchName extends AsyncTask<Void, Void, String> {
        String idResult = "";
        private ProgressDialog pD;
        String message="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pD = new ProgressDialog(Select_Branches.this);
            pD.setMessage("Please wait");
            pD.setTitle("Branches are loading...");
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
                connection.setRequestMethod("GET");
                int response = connection.getResponseCode();
                Log.e("responce",""+response);

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
                JSONArray objArray = jsonObject.getJSONArray("value");
                Log.e("objarray", objArray.toString());

                for(int i=0;i<objArray.length();i++){

                    m  = new Model();
                    JSONObject innerobj = objArray.getJSONObject(i);
                     id = innerobj.getString("BranchID");
                     name = innerobj.getString("BranchName");
                    message=id;
                    //Log.e("id",id);
                    //Log.e("name",name);
                    m.setBranchId(id);
                    m.setBranchName(name);

                    list.add(m);

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
            if(!s.equals("")) {

                BranchListAdapter branchList = new BranchListAdapter(Select_Branches.this, list);
                listView.setAdapter(branchList);
                pD.dismiss();
            }
            else {
                pD.dismiss();
                Toast.makeText(Select_Branches.this, "No Data Avialable", Toast.LENGTH_SHORT).show();
            }


        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
