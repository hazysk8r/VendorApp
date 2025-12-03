package com.stepbystep.bossapp.chart;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepbystep.bossapp.DO.Order;
import com.stepbystep.bossapp.DO.Order_history;
import com.stepbystep.bossapp.DO.UserAccount;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Calculatesales {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference order_history_databaseReference;
    private DatabaseReference useraccount_databaseReference;
    private ArrayList<Order_history> order_histories;
    private ArrayList<Order_history> my_order_histories;
    private ArrayList<UserAccount> userAccounts;
    private ArrayList<LocalDateTime> dates;
    private ArrayList<Float> sales;
    private float sum;
    ArrayAdapter arrayAdapter;


    public ArrayList<Float> MonthCalculateSales(String truck_id){
        order_histories = new ArrayList<>();
        userAccounts = new ArrayList<>();
        my_order_histories = new ArrayList<>();
        dates = new ArrayList<>();
        sales = new ArrayList<>();
        sum = 0;
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        sales.add(0f);
        firebaseDatabase = FirebaseDatabase.getInstance();
        useraccount_databaseReference = firebaseDatabase.getReference("FoodTruck").child("UserAccount");
        useraccount_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAccounts.clear();
                order_histories.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserAccount userAccount = snapshot1.getValue(UserAccount.class);
                    userAccounts.add(userAccount);
                    // System.out.println(userAccounts);
                    String user_id = userAccount.getIdToken();
                    order_history_databaseReference = firebaseDatabase.getReference("FoodTruck").child("Order_history").child(user_id);
                    order_history_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {  //반복문으로 리스트를 출력함
                                Order_history order_history = snapshot1.getValue(Order_history.class); // 객체에 데이터를 담는다
                                order_histories.add(order_history);
                                //System.out.println("메롱" + order_histories);// 여기서 add 를 했음
                                System.out.println(truck_id);
                                if(order_history.getTruck_id().equals(truck_id)){
                                    my_order_histories.add(order_history);
                                    System.out.println("잘되나" +my_order_histories);
                                    LocalDateTime date = StringtoDate.changetodata(order_history.getDate());
                                    System.out.println(date.getMonthValue());

                                    switch(date.getMonthValue()){
                                        case 1: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(0,sum);
                                            } break;
                                        }
                                        case 2: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(1,sum);

                                            } break;
                                        }
                                        case 3: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(2,sum);

                                            } break;
                                        }
                                        case 4: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(3,sum);

                                            } break;
                                        }
                                        case 5: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(4,sum);

                                            }break;
                                        }
                                        case 6: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                            }
                                            sales.set(5,sum);
                                            System.out.println(sales);
                                            break;
                                        }
                                        case 7: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                            }
                                            sales.set(6,sum);
                                            break;
                                        }
                                        case 8: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                            }
                                            sales.set(7,sum);
                                            break;
                                        }
                                        case 9: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                            }
                                            sales.set(8,sum);
                                            break;
                                        }
                                        case 10: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(9,sum);

                                            }break;
                                        }
                                        case 11: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(10,sum);

                                            }break;
                                        }
                                        case 12: {
                                            ArrayList<Order> orders = order_history.getOrders();
                                            for (int i = 0; i < orders.size(); i++) {
                                                sum = sum + (Float.parseFloat(orders.get(i).getFood_cost())*orders.get(i).getFood_number());
                                                sales.set(11,sum);

                                            }  break;
                                        }
                                    }
                                }

                            }


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // error
                            Log.e("Calculatesales", String.valueOf(error.toException()));
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Calculatesales", String.valueOf(error.toException()));
            }
        });
        System.out.println("씨이빨"+userAccounts+"\n"+order_histories);


        return sales;
    }


}
