package com.xumerz.dajisoft.zipsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Talha-PC on 12/23/2016.
 */
public class PaymentAdapter extends ArrayAdapter<Model> {
    Context context;
    List<Model> paymentList;


    public PaymentAdapter(Context context, List<Model> paymentList) {
        super(context,R.layout.customer_row,paymentList);
        this.context=context;
        this.paymentList=paymentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      holder=new ViewHolder();

        if (convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.payment_row,null);
             holder.date= (TextView) convertView.findViewById(R.id.date);
             holder.amount= (TextView) convertView.findViewById(R.id.amount);
             holder.type= (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);

        }
        else {

            holder= (ViewHolder) convertView.getTag();

        }

        holder.date.setText(paymentList.get(position).getDate());
        holder.amount.setText("$"+paymentList.get(position).getAmount());
        holder.type.setText(paymentList.get(position).getType());
        return  convertView;



    }


    private class ViewHolder {
        private TextView date,amount,type;


    }
}
