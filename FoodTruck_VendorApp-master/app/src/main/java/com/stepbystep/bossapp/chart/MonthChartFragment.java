package com.stepbystep.bossapp.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepbystep.bossapp.DO.Order;
import com.stepbystep.bossapp.DO.Order_history;
import com.stepbystep.bossapp.DO.StoreAccount;
import com.stepbystep.bossapp.DO.UserAccount;
import com.stepbystep.bossapp.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MonthChartFragment extends Fragment {

    public static MonthChartFragment newInstance() {
        MonthChartFragment monthchart = new MonthChartFragment();
        return monthchart;
    }

    private View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference storeAccount_databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String truck_id;
    ArrayList<StoreAccount> storeAccounts;
    ArrayList<Float> month_sales;
    private DatabaseReference order_history_databaseReference;
    private ArrayList<Order_history> order_histories;
    private ArrayList<Float> sales;
    private ArrayList<BarEntry> values;
    private float[] sum;
    private int yearsales;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_monthchart, container, false);
        TextView textView1 = view.findViewById(R.id.textView1);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storeAccounts = new ArrayList<>();
        month_sales = new ArrayList<>();
        order_histories = new ArrayList<>();
        values = new ArrayList<>();
        sales = new ArrayList<>();
        sum = new float[13];

        for (int i = 0; i < 13; i++) {
            sales.add(0f);
        }
        for (int i = 0; i < 12; i++) {
            sum[i] = 0;
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        if (user != null) {
            FirebaseDatabase.getInstance().getReference("BossApp").child("StoreAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StoreAccount storeAccount = snapshot.getValue(StoreAccount.class);
                    truck_id = storeAccount.getTruckId();
                    order_histories.clear();
                    order_history_databaseReference = firebaseDatabase.getReference("FoodTruck").child("OrderHistory");
                    Query query = order_history_databaseReference.orderByChild("truckId").equalTo(truck_id);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {  //반복문으로 리스트를 출력함
                                Order_history order_history = snapshot1.getValue(Order_history.class); // 객체에 데이터를 담는다
                                order_histories.add(order_history);
                                LocalDateTime date = StringtoDate.changetodata(order_history.getDate());
                                switch (date.getMonthValue()) {
                                    case 1: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[0] = sum[0] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(0, sum[0]);
                                        }
                                        break;
                                    }
                                    case 2: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[1] = sum[1] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(1, sum[1]);

                                        }
                                        break;
                                    }
                                    case 3: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[2] = sum[2] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(2, sum[2]);

                                        }
                                        break;
                                    }
                                    case 4: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[3] = sum[3] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(3, sum[3]);

                                        }
                                        break;
                                    }
                                    case 5: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[4] = sum[4] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(4, sum[4]);

                                        }
                                        break;
                                    }
                                    case 6: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[5] = sum[5] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                        }
                                        sales.set(5, sum[5]);
                                        break;
                                    }
                                    case 7: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[6] = sum[6] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                        }
                                        sales.set(6, sum[6]);
                                        break;
                                    }
                                    case 8: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[7] = sum[7] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                        }
                                        sales.set(7, sum[7]);
                                        break;
                                    }
                                    case 9: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[8] = sum[8] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                        }
                                        sales.set(8, sum[8]);
                                        break;
                                    }
                                    case 10: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[9] = sum[9] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(9, sum[9]);

                                        }
                                        break;
                                    }
                                    case 11: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[10] = sum[10] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(10, sum[10]);

                                        }
                                        break;
                                    }
                                    case 12: {
                                        ArrayList<Order> orders = order_history.getOrders();
                                        for (int i = 0; i < orders.size(); i++) {
                                            sum[11] = sum[11] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                            sales.set(11, sum[11]);

                                        }
                                        break;
                                    }
                                }


                            }
                            yearsales = 0;
                            for (int i = 0; i < sales.size(); i++) {
                                //System.out.println(yearsales);
                                yearsales += sales.get(i);
                                values.add(new BarEntry(i + 2, sales.get(i).floatValue()));
                            }
                            showChart(values);
                            textView1.setText("총 " + Utils.formatNumber(yearsales, 0, true) + "원");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // error
                            Log.e("Calculatesales", String.valueOf(error.toException()));
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return view;
    }

    private void showChart(ArrayList<BarEntry> data) {

        /*    막대그래프   */
        BarChart barChart = (BarChart) view.findViewById(R.id.month_chart);
        String[] months = {"", "", "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"}; //앞에 두개 공백채워줘야함

        XAxis xAxis;
        YAxis yAxis;


        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10);
        // 레이블 텍스트 색
        xAxis.setTextColor(Color.BLACK);
        // 축 색
        xAxis.setAxisLineColor(Color.BLACK);
        // 그래프 뒷 배경의 그리드 표시하지 않기
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setAxisMaximum(14);
        xAxis.setAxisMinimum(1);
        xAxis.setLabelCount(12);

        xAxis.setGranularity(-1f); // x 축 벨류 간 간격

        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));

        xAxis.setTextSize(8f);
        xAxis.setGranularityEnabled(false);

        yAxis = barChart.getAxisLeft();
        barChart.getAxisRight().setEnabled(false);

        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
        //yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(0f);
        yAxis.setSpaceMax(0.2f);
        yAxis.setSpaceMin(0.2f);


        BarDataSet barDataSet = new BarDataSet(data, "매출액");
        barDataSet.setFormSize(5f);
        barDataSet.setColors(Color.parseColor("#00b2ce"));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(0.2f);
        barDataSet.setDrawValues(true);
        // barDataSet.setFormLineWidth(10f);

        BarData bardata = new BarData(barDataSet);
        bardata.setBarWidth(0.3f); // 바의 두께

        barChart.setFitBars(true);
        barChart.setData(bardata);
        barChart.getDescription().setText("단위 : 원");
        barChart.animateY(2000);
        barChart.setTouchEnabled(true); // 터치는 가능하게 함
        barChart.setPinchZoom(false);  // 줌 도 못하게 고정
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false); // 더블 탭 확대 못하게 고정
        MyMarkerView mv1 = new MyMarkerView(getContext(), R.layout.custom_marker_view); // 마커뷰
        mv1.setChartView(barChart);
        barChart.setMarker(mv1);


    }


}



