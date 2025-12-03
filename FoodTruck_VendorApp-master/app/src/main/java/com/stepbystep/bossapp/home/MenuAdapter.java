package com.stepbystep.bossapp.home;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.stepbystep.bossapp.DO.AdminFood;

import com.stepbystep.bossapp.R;


import java.util.Collections;
import java.util.List;



public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<AdminFood> foods;
    private Context context;
    private onItemClickListener itemListener;
    private onLongClickListener longListener;

    public interface onItemClickListener{
        void onItemClick(int pos);
    }
    public interface onLongClickListener{
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(onItemClickListener listener)
    {
        itemListener = listener;
    }

    public void setOnLongClickListener(onLongClickListener listener)
    {
        longListener = listener;
    }

    public MenuAdapter(Context context, List<AdminFood> foods) {
        this.context = context;
        this.foods = foods;
    }

    public void addList(List<AdminFood> list) {
        foods.clear();
        Collections.copy(foods, list);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(v, itemListener, longListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.img.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        Picasso.get().load(foods.get(position).getImage()).centerCrop().fit().into(holder.img);
        holder.name.setText(foods.get(position).getName());
        holder.description.setText(foods.get(position).getContent());
        holder.cost.setText(foods.get(position).getCost()+" 원");
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name, description, cost;
        CardView cardView;
        public MenuViewHolder(@NonNull View itemView , final onItemClickListener itemlistener , final onLongClickListener longClickListener) {
            super(itemView);
            img = itemView.findViewById(R.id.adapterMenuImage);
            name = itemView.findViewById(R.id.adapterMenuName);
            description = itemView.findViewById(R.id.adapterMenuDescription);
            cost = itemView.findViewById(R.id.adapterMenuPrice);
            cardView = itemView.findViewById(R.id.MenuCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemlistener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            itemlistener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(longClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            longClickListener.onItemLongClick(position);
                    }
                    return false;
                }
            });
        }
    }

}