package com.xumerz.dajisoft.zipsproject;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Talha-PC on 12/22/2016.
 */
public class CustomerAdapter extends ArrayAdapter<Model> {
    Context context;
    List<Model> costomerList;


    public CustomerAdapter(Context context, List<Model> costomerList) {
        super(context,R.layout.customer_row,costomerList);
        this.context=context;
        this.costomerList=costomerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.customer_row,null);
            TextView fname= (TextView) convertView.findViewById(R.id.firstname);
            TextView lname= (TextView) convertView.findViewById(R.id.lastname);
            TextView email= (TextView) convertView.findViewById(R.id.email);
            TextView phone= (TextView) convertView.findViewById(R.id.hphone);
            TextView address= (TextView) convertView.findViewById(R.id.address);
            TextView zips= (TextView) convertView.findViewById(R.id.zips);
            TextView prepaidbil= (TextView) convertView.findViewById(R.id.prepaidbil);
            TextView creditinfo= (TextView) convertView.findViewById(R.id.creditinfo);
            fname.setText(costomerList.get(position).getFirstName());
            lname.setText(costomerList.get(position).getLastName());
            email.setText(costomerList.get(position).getEmail());
            phone.setText(costomerList.get(position).getPhoneNumber());
            address.setText(costomerList.get(position).getAddress());
            zips.setText(costomerList.get(position).getZips());
            prepaidbil.setText(costomerList.get(position).getPrepaidBail());
            creditinfo.setText(costomerList.get(position).getCreditInfo());

        }
        return  convertView;



    }
}
