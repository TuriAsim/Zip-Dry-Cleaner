package com.xumerz.dajisoft.zipsproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha-PC on 12/27/2016.
 */
public class OrderAdapter extends ArrayAdapter<Model> {
    Context context;
    List<Model> odderList;


    public OrderAdapter(Context context, List<Model> odderList) {
        super(context,R.layout.order_row,odderList);
        this.context=context;
        this.odderList=odderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mholder;
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mholder=new ViewHolder();
        if (convertView==null)
        {

            convertView=layoutInflater.inflate(R.layout.order_row,null);
             mholder.tickt= (TextView) convertView.findViewById(R.id.ticketno);
             mholder.dpName= (TextView) convertView.findViewById(R.id.depname);
             mholder.order= (TextView) convertView.findViewById(R.id.orderstatus);
             mholder.total= (TextView) convertView.findViewById(R.id.grandtotal);
            Log.e("adapter ticket", odderList.get(position).getTicketno());

            String tik=mholder.tickt.getText().toString();
            Log.e("adapter ticket",tik);
            convertView.setTag(mholder);


        }
        else {

             mholder= (ViewHolder) convertView.getTag();


        }

        mholder.tickt.setText(odderList.get(position).getTicketno());
        mholder.dpName.setText(odderList.get(position).getDeptName());
        mholder.order.setText(odderList.get(position).getOrderStatus());
        mholder.total.setText("$"+odderList.get(position).getGrandTotal());


        return  convertView;



    }



    private class ViewHolder {
        private TextView tickt,dpName,order,total;


    }


}
