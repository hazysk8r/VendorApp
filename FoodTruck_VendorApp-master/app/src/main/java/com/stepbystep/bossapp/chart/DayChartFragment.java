package com.stepbystep.bossapp.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import com.google.firebase.storage.FirebaseStorage;
import com.stepbystep.bossapp.DO.Order;
import com.stepbystep.bossapp.DO.Order_history;
import com.stepbystep.bossapp.DO.StoreAccount;
import com.stepbystep.bossapp.DO.UserAccount;
import com.stepbystep.bossapp.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class DayChartFragment extends Fragment {

    public static DayChartFragment newInstance() {
        DayChartFragment daychart = new DayChartFragment();
        return daychart;
    }

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference storeAccount_databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String truck_id;
    private ArrayList<StoreAccount>  storeAccounts;
    private DatabaseReference order_history_databaseReference;
    private ArrayList<Order_history> order_histories;
    private ArrayList<Order_history> my_order_histories;
    private ArrayList<String> dates;
    private LocalDate datenow= LocalDate.now();
    private ArrayList<Float> sales;
    private ArrayList<BarEntry> values;
    private int weeksales;
    private float[] sum;
    private DatePicker datePicker;




    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daychart, container, false);
        datePicker = view.findViewById(R.id.datePicker);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthofYear, int dayofMonth) {

                datenow = LocalDate.of(year,monthofYear+1,dayofMonth);
                // System.out.println("입력받은 날짜 "+datenow);
                datainChart(datenow);
            }
        });

        datainChart(datenow);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    public void datainChart (LocalDate date){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storeAccounts = new ArrayList<>();
        order_histories = new ArrayList<>();
        my_order_histories = new ArrayList<>();
        values = new ArrayList<>();
        dates = new ArrayList<>();


        //datenow = LocalDate.of(2022,6,16);
        sales = new ArrayList<>();
        sum = new float[7];


        for (int i = 0; i <7 ;i++){
            sales.add(0f);
            sum[i]= 0;
        }

        for (int i = 0; i <9 ;i++){
            dates.add("");
            dates.set(i, String.valueOf(datenow.getMonthValue()) + "/" + String.valueOf(datenow.getDayOfMonth()-(8-i)));
        }
        dates.set(0,"");
        dates.set(1,"");

        firebaseDatabase = FirebaseDatabase.getInstance();
        storeAccount_databaseReference =  firebaseDatabase.getReference("BossApp").child("StoreAccount");
        storeAccount_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeAccounts.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StoreAccount storeAccount = dataSnapshot.getValue(StoreAccount.class);
                    // System.out.println(storeAccount);
                    if (storeAccount.getIdToken() != null) {
                        if (storeAccount.getIdToken().equals(user.getUid())) {
                            //storeAccounts.add(storeAccount);
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
                                        LocalDate order_date = date.toLocalDate();
                                        // 오늘 날짜로 부터 7일 전까지 보여줌
                                        int period = (int) ChronoUnit.DAYS.between(order_date, datenow);
                                        if (period < 7) {
                                            switch (period) {
                                                case 0: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[6] = sum[6] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(6, sum[6]);
                                                        Log.d("6",""+sum[6]);
                                                    }
                                                    break;

                                                }
                                                case 1: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[5] = sum[5] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(5, sum[5]);
                                                        Log.d("5",""+sum[6]);
                                                    }
                                                    break;
                                                }
                                                case 2: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[4] = sum[4] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(4, sum[4]);
                                                        Log.d("4",""+sum[6]);
                                                    }
                                                    break;
                                                }
                                                case 3: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[3] = sum[3] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(3, sum[3]);
                                                        Log.d("3",""+sum[6]);
                                                    }
                                                    break;
                                                }
                                                case 4: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[2] = sum[2] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(2, sum[2]);
                                                        Log.d("2",""+sum[6]);
                                                    }
                                                    break;
                                                }
                                                case 5: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[1] = sum[1] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(1, sum[1]);
                                                        Log.d("1",""+sum[6]);
                                                    }
                                                    break;
                                                }
                                                case 6: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[0] = sum[0] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(0, sum[0]);
                                                        Log.d("0",""+sum[6]);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    weeksales = 0;
                                    for (int i = 0; i < sales.size(); i++) {
                                        //System.out.println(sales.get(i));
                                        weeksales += sales.get(i);
                                        values.add(new BarEntry(i + 2, sales.get(i).floatValue())); // +2는 앞에 빈 값들임
                                    }
                                    //System.out.println(weeksales);
                                    TextView textView1 = view.findViewById(R.id.textView1);
                                    textView1.setText("총 " + Utils.formatNumber(weeksales, 0, true) + "원");
//                              System.out.println("확인용"+sales +"\n" +dates);
                                    showchart(values, dates);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // error
                                    Log.e("Calculatesales", String.valueOf(error.toException()));
                                }
                            });


                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Calculatesales", String.valueOf(error.toException()));
            }
        });




    }


    private void showchart(ArrayList<BarEntry> data,ArrayList<String> date ){

        BarChart barChart = (BarChart) view.findViewById(R.id.day_chart);

        String[] weeks =date.toArray(new String[date.size()]); // 왜인지 모르겠는데 이렇게 해야 맞음


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
        xAxis.setDrawAxisLine(false); //
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setAxisMaximum(9);
        xAxis.setAxisMinimum(1);
        xAxis.setLabelCount(7);

        xAxis.setGranularity(-1f); // x 축 벨류 간 간격

        xAxis.setValueFormatter(new IndexAxisValueFormatter(weeks));

        xAxis.setTextSize(8f);
        xAxis.setGranularityEnabled(false);

        yAxis = barChart.getAxisLeft();
        barChart.getAxisRight().setEnabled(false);

        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(false);
//        yAxis.setAxisMaximum(1f);
        yAxis.setAxisMinimum(0f);
        yAxis.setSpaceMax(0.2f);
        yAxis.setSpaceMin(0.2f);



        BarDataSet barDataSet = new BarDataSet(data,"매출액");
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
        barChart.setTouchEnabled(true); // 터치는 가능하게 함\
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);  // 줌 도 못하게 고정
        barChart.setDoubleTapToZoomEnabled(false); // 더블 탭 확대 못하게 고정

        MyMarkerView mv1 = new MyMarkerView(getContext(),R.layout.custom_marker_view); // 마커뷰
        mv1.setChartView(barChart);
        barChart.setMarker(mv1);
    }

    public void onCreateView() {
    }
}
