package com.stepbystep.bossapp.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stepbystep.bossapp.DO.Notice;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    ArrayList<Notice> items = new ArrayList<Notice>();
    OnNoticeItemClickListener listener;

    @NonNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.notice_recycleview_layout,parent,false);
        return new NoticeAdapter.ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.ViewHolder holder, int position) {
        //set item
        Notice notice = items.get(position);
        holder.setItem(notice);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView notice_recycleView_layout_date;
        TextView notice_recycleView_layout_title;

        public ViewHolder(@NonNull View itemView, final OnNoticeItemClickListener listener) {
            super(itemView);
            notice_recycleView_layout_date= itemView.findViewById(R.id.notice_recycleView_layout_date);
            notice_recycleView_layout_title = itemView.findViewById(R.id.notice_recycleView_layout_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(NoticeAdapter.ViewHolder.this,v,position);
                    }
                }
            });
        }

        public void setItem(Notice item){
            notice_recycleView_layout_date.setText(item.getDate());
            notice_recycleView_layout_title.setText(item.getTitle());
        }
    }

    public void addItem(Notice item){
        items.add(item);
    }

    public void setItems(ArrayList<Notice> items ){
        this.items = items;
    }

    public Notice getItem(int position){
        return items.get(position);
    }

    //item click 리스너 로직
    public void setOnItemClickListener(OnNoticeItemClickListener listener){
        this.listener = listener;
    }
}