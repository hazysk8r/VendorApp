package com.stepbystep.bossapp.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.stepbystep.bossapp.R;
import com.stepbystep.bossapp.DO.Review;

import java.util.List;

public class RateReviewAdapter extends RecyclerView.Adapter<RateReviewAdapter.CommentViewHolder> {

    private List<Review> ratesList;
    private Context context;

    public RateReviewAdapter(List<Review> ratesList, Context context) {
        this.ratesList = ratesList;
        this.context = context;
    }

    public RateReviewAdapter() {
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_and_review,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {
        Review rates = ratesList.get(position);
        if (rates==null){return; }
        viewHolder.tv_name.setText(rates.getUserName());
        viewHolder.tv_date.setText(rates.getDate());
        viewHolder.tv_content.setText(rates.getComment());
        viewHolder.ratingBar.setRating(Float.parseFloat(rates.getRate()));
        if(rates.getImage() != null){
            if(rates.getImage().length() > 0){
                Glide.with(context).load(rates.getImage()).into(viewHolder.img_cmt);
                viewHolder.img_cmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, FullPicActivity.class);
                        intent.putExtra("url",ratesList.get(position).getImage());
                        context.startActivity(intent);
                    }
                });
            }
            else{
                viewHolder.img_cmt.getLayoutParams().width = 0;
                viewHolder.img_cmt.getLayoutParams().height = 0;
            }
        }
        else{
            viewHolder.img_cmt.getLayoutParams().width = 0;
            viewHolder.img_cmt.getLayoutParams().height = 0;
        }
    }

    @Override
    public int getItemCount() {
        if (ratesList!=null) {
            return ratesList.size() ;
        }
        return 0;
    }

    public  class CommentViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name, tv_date, tv_content;
        private RatingBar ratingBar;
        private ImageView img_cmt;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.name_user_cmnt);
            tv_date = itemView.findViewById(R.id.date_user_cmnt);
            tv_content = itemView.findViewById(R.id.user_cont_cmnt);
            ratingBar = itemView.findViewById(R.id.ratingBar_user_cmnt);
            img_cmt = itemView.findViewById(R.id.img_comnt);
        }
    }
}
