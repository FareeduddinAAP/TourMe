package com.project.tourme.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.tourme.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerviewAdaper extends RecyclerView.Adapter<MyViewHolder> {

    List<mLocation>list;
    Context context;

    public RecyclerviewAdaper(List<mLocation> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_recyclerview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.markerName.setText(list.get(position).getMarkerName());
        Picasso.get().load(list.get(position).getImageUrl()).into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
