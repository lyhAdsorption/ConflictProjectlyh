package com.fmt.conflictproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmt.conflictproject.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder> {

    List<String> mData;
    LayoutInflater mLayoutInflater;

    public RecyclerAdapter(Context context,List<String> data) {
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerHolder(mLayoutInflater.inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

class RecyclerHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public RecyclerHolder(@NonNull View itemView) {
        super(itemView);
        textView  = itemView.findViewById(R.id.textview);
    }

    public void bind(String title) {
        textView.setText(title);
    }
}

