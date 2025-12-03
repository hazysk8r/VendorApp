package com.stepbystep.bossapp.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stepbystep.bossapp.DO.Festival;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.ViewHolder> {

    ArrayList<Festival> items = new ArrayList<Festival>();
    OnFestivalItemClickListener listener;

    @NonNull
    @Override
    public FestivalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.festival_recycleview_layout,parent,false);
        return new FestivalAdapter.ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FestivalAdapter.ViewHolder holder, int position) {
        //set item
        Festival festival = items.get(position);
        holder.setItem(festival);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView festival_recycleView_layout_date;
        TextView festival_recycleView_layout_title;

        public ViewHolder(@NonNull View itemView, final OnFestivalItemClickListener listener) {
            super(itemView);
            festival_recycleView_layout_date= itemView.findViewById(R.id.festival_recycleView_layout_date);
            festival_recycleView_layout_title = itemView.findViewById(R.id.festival_recycleView_layout_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(FestivalAdapter.ViewHolder.this,v,position);
                    }
                }
            });
        }

        public void setItem(Festival item){
            festival_recycleView_layout_date.setText(item.getDate());
            festival_recycleView_layout_title.setText(item.getTitle());
        }
    }

    public void addItem(Festival item){
        items.add(item);
    }

    public void setItems(ArrayList<Festival> items ){
        this.items = items;
    }

    public Festival getItem(int position){
        return items.get(position);
    }

    //item click 리스너 로직
    public void setOnItemClickListener(OnFestivalItemClickListener listener){
        this.listener = listener;
    }
}