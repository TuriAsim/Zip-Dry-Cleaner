package com.xumerz.dajisoft.zipsproject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Talha-PC on 8/31/2016.
 */
public class BranchListAdapter extends ArrayAdapter<Model> {
    Context context;
    List<Model> branchList;



    public BranchListAdapter(Context context, List<Model> branchList) {
        super(context, R.layout.branchrow,branchList);
        this.context=context;
        this.branchList=branchList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null)
        {
         convertView=layoutInflater.inflate(R.layout.branchrow,null);
            TextView byId= (TextView) convertView.findViewById(R.id.branchid);
            TextView branchName= (TextView) convertView.findViewById(R.id.branchname);
            byId.setText(branchList.get(position).getBranchId());
            branchName.setText(branchList.get(position).getBranchName());


            if (position%2==0)
            {

                branchName.setTextColor(Color.BLACK);

            }
            else {

                branchName.setTextColor(Color.BLUE);
            }
        }
        return  convertView;
    }
}