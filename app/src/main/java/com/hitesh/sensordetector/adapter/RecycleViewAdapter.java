package com.hitesh.sensordetector.adapter;


import android.content.Context;
import android.hardware.Sensor;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benevolenceinc.sensordetector.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hitesh on 12/20/18.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolderClass> {
    List<Sensor> stringList = new ArrayList<>();
    Context context;

    public RecycleViewAdapter(List<Sensor> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view_item,parent,false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
            holder.tvTitle.setText(stringList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        public TextView tvTitle;
        public ViewHolderClass(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvName);
        }
    }
}
