package com.hitesh.sensordetector.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.benevolenceinc.sensordetector.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hitesh on 12/19/18.
 */
public class GridViewAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;

    public GridViewAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        TextView textViewTitle = convertView.findViewById(R.id.tvTitle);
        TextView textViewSubTitle = convertView.findViewById(R.id.tvSubTitle);
        textViewTitle.setText(list.get(position).charAt(0) + "");
        textViewSubTitle.setText(list.get(position));
        return convertView;
    }
}
