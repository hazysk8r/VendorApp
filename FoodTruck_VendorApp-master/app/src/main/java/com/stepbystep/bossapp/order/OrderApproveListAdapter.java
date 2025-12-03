package com.stepbystep.bossapp.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stepbystep.bossapp.DO.Order;
import com.stepbystep.bossapp.DO.Order_history;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderApproveListAdapter extends RecyclerView.Adapter<OrderApproveListAdapter.ViewHolder>{

    //order_history 객체 저장 리스트
    private ArrayList<Order_history> items;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_approve_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        private TextView orderDate;
        private TextView orderList;
        private TextView totalCost;
        private Button approveButton;
        private Button disapproveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.recyclerview_approve_order_order_date);
            orderList = itemView.findViewById(R.id.recyclerview_approve_order_order_list);
            totalCost = itemView.findViewById(R.id.recyclerview_approve_order_total_cost);
            approveButton =  itemView.findViewById(R.id.recyclerview_approve_order_approve_button);
            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    DatabaseReference orderHistoryData = FirebaseDatabase.getInstance().getReference("FoodTruck").child("OrderHistory").child(items.get(position).getOrderHistoryId());
                    items.remove(position);
                    Toast.makeText(itemView.getContext().getApplicationContext(),"주문을 수락하셨습니다.", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    HashMap hashMap = new HashMap();
                    hashMap.put("orderState","접수");
                    orderHistoryData.updateChildren(hashMap);
                }
            });
            disapproveButton = itemView.findViewById(R.id.recyclerview_approve_order_disapprove_button);
            disapproveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    DatabaseReference orderHistoryData = FirebaseDatabase.getInstance().getReference("FoodTruck").child("OrderHistory").child(items.get(position).getOrderHistoryId());
                    items.remove(position);
                    Toast.makeText(itemView.getContext().getApplicationContext(),"주문을 거절하셨습니다.", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    HashMap hashMap = new HashMap();
                    hashMap.put("orderState","취소");
                    orderHistoryData.updateChildren(hashMap);
                }
            });
        }

        public void setItem(Order_history orderHistory){
            orderDate.setText(orderHistory.getDate());
            String order_list = "";
            int sum_data = 0;
            for(Order order : orderHistory.getOrders()){
                sum_data += Integer.parseInt(order.getFood_cost()) * order.getFood_number();
                order_list += order.getFood_name()+" x"+Integer.toString(order.getFood_number())+"   ";
            }
            orderList.setText(order_list);
            totalCost.setText(Integer.toString(sum_data));
        }
    }

    public Order_history getItem(int position){
        return items.get(position);
    }

    public void setItems(ArrayList<Order_history> orderHistoryList){
        this.items = orderHistoryList;
    }

}
