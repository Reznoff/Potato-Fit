package com.unpas.potatosoft.potatofit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unpas.potatosoft.potatofit.activities.MainActivity;
import com.unpas.potatosoft.potatofit.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> historyList;
    private MainActivity activity;
    public String title, content;

    public RecyclerAdapter(ArrayList<HashMap<String, String>> list, MainActivity mainActivity) {
        this.historyList = list;
        this.activity = mainActivity;
    }

    public RecyclerAdapter() {
        //Empty Constructor maybe needed. (just in case lmao)
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_content, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HashMap<String, String> list = historyList.get(position);
        holder.txtTitle.setText(list.get("id"));
        holder.txtDistance.setText(list.get("jarak_tempuh"));
        holder.txtDuration.setText(list.get("durasi"));
        holder.txtBurnedCal.setText(list.get("kalori_terbuang"));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDuration, txtDistance, txtBurnedCal;


        public ViewHolder(View rootView) {
            super(rootView);
            txtTitle = rootView.findViewById(R.id.txtTitle);
            txtDuration = rootView.findViewById(R.id.txtDuration);
            txtDistance = rootView.findViewById(R.id.txtDistance);
            txtBurnedCal = rootView.findViewById(R.id.txtCal);
        }
    }

}
