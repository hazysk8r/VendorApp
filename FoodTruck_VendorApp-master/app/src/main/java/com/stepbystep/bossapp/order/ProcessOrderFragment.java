package com.stepbystep.bossapp.order;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stepbystep.bossapp.DO.Order_history;
import com.stepbystep.bossapp.DO.StoreAccount;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;

public class ProcessOrderFragment extends Fragment {

    //데이터베이스 참조 변수
    private DatabaseReference orderHistoryDatabase;
    //파이어베이스 인증
    private FirebaseUser user;
    //StoreAccount
    private StoreAccount myAccount;
    ArrayList<Order_history> orderHistoryList;
    //진행중인 주문 횟수 텍스트뷰
    TextView orderCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_process_order,container,false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences storeAccountData = getActivity().getSharedPreferences(user.getUid(),getActivity().MODE_PRIVATE);
        Gson gson = new GsonBuilder().create();
        String data = storeAccountData.getString("StoreAccount","");
        if(!(data.equals("") || data == null)){
            myAccount = gson.fromJson(data,StoreAccount.class);
            orderHistoryDatabase =  FirebaseDatabase.getInstance().getReference("FoodTruck").child("OrderHistory");
            orderHistoryList = new ArrayList<Order_history>();
            orderCount = rootView.findViewById(R.id.fragment_process_order_order_count);
            setRecycleView(rootView);
        }
        return rootView;
    }


    //리싸이클러뷰 설정
    private void setRecycleView(ViewGroup rootView){
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_process_order_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        Query query = orderHistoryDatabase.orderByChild("truckId").equalTo(myAccount.getTruckId());
        recyclerView.setLayoutManager(layoutManager);
        OrderProcessListAdapter orderProcessListAdapter = new OrderProcessListAdapter();
        orderProcessListAdapter.setItems(orderHistoryList);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order_history orderHistory = dataSnapshot.getValue(Order_history.class);
                    if(orderHistory.getOrderState().equals("접수")){
                        orderHistoryList.add(orderHistory);
                    }
                }
                orderCount.setText("진행 중인 주문 ("+orderHistoryList.size()+")");
                orderProcessListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(orderProcessListAdapter);
    }

}
