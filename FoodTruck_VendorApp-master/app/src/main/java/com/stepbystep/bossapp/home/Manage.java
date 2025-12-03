package com.stepbystep.bossapp.home;

import static androidx.fragment.app.FragmentManager.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.stepbystep.bossapp.DO.Truck;
import com.stepbystep.bossapp.DO.UserAccount;
import com.stepbystep.bossapp.MainActivity;
import com.stepbystep.bossapp.R;
import com.stepbystep.bossapp.chart.MyMarkerView;
import com.stepbystep.bossapp.chart.StringtoDate;
import com.stepbystep.bossapp.databinding.FragmentManageBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Manage extends Fragment {
    private DatabaseReference databaseReference;
    private DatabaseReference locationRef;
    private FirebaseUser user;
    FirebaseAuth mAuth;

    private LocationListener mLocListener = null;
    FusedLocationProviderClient client;
    private Location currentLocation;
    private String lat;
    private String lon;
    int PERMISSION_ID = 100;

    String truckId;
    String vendor_status;
    StoreAccount storeAccount;

    FragmentManageBinding binding;
    ArrayList<Truck> items = new ArrayList<>();

    LocalDate datenow = LocalDate.now();
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference storeAccount_databaseReference;

    ArrayList<Float> month_sales  ;
    ArrayList<StoreAccount>  storeAccounts;
    private DatabaseReference order_history_databaseReference;
    private DatabaseReference useraccount_databaseReference;
    private ArrayList<Order_history> order_histories;
    private ArrayList<Order_history> my_order_histories;
    private ArrayList<UserAccount> userAccounts;
    private ArrayList<String> dates;
    private ArrayList<Float> sales;
    private ArrayList<BarEntry> values;
    private float[] sum;
    private int weeksales;

    public Manage() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initButtonClickListener();

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("BossApp").child("StoreAccount").child(user.getUid());
        locationRef = FirebaseDatabase.getInstance().getReference("FoodTruck").child("Truck");

        if(user != null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        storeAccount = snapshot.getValue(StoreAccount.class);
                        truckId = storeAccount.getTruckId();
                        vendor_status = storeAccount.getVendor_status();
                        if(vendor_status.equals("0")){
                            binding.toggleButton.setChecked(true); //토글버튼 활성화 시 위치 갱신 및 영업시작
                            binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    databaseReference.child("vendor_status").setValue("1");
                                    locationRef.child(truckId).child("vendor_status").setValue("1");
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},100);
                                    } else {
                                        getLastLocation();
                                        com.stepbystep.bossapp.DO.Location location = new com.stepbystep.bossapp.DO.Location(lat, lon);
                                        locationRef.child(truckId).child("location").setValue(location);
                                        Log.d("CheckCurrentLocation", "현재 위치 값: "+lat+","+ lon);
                                    }
                                }
                            });
                        }
                        if(vendor_status.equals("1"))
                        {
                            binding.toggleButton.setChecked(false); //토글버튼 비활성화 시 영업중지
                            binding.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    databaseReference.child("vendor_status").setValue("0");
                                    locationRef.child(truckId).child("vendor_status").setValue("0");
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},100);
                                    } else {
                                        getLastLocation();
                                        com.stepbystep.bossapp.DO.Location location = new com.stepbystep.bossapp.DO.Location(lat, lon);
                                        locationRef.child(truckId).child("location").setValue(location);
                                        Log.d("CheckCurrentLocation", "현재 위치 값: " +lat+","+ lon);
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return view;
    }

    private void initButtonClickListener() {
        binding.btnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TruckNoticeActivity.class));
                //getActivity().finish();
            }
        });


        binding.btnMenuManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuManageActivity.class);
                startActivity(intent);
            }
        });//

        binding.btnReviewManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewManageActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        if (checkPermissions()) {


            if (isLocationEnabled()) {

                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        currentLocation = location;
                        if (location == null) {
                            requestNewLocationData();
                        }
                        else
                        {
                            lat = Double.toString(location.getLatitude());
                            lon = Double.toString(location.getLongitude());
                        }
                    }
                });
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        client = LocationServices.getFusedLocationProviderClient(this.getActivity());
        client.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @SuppressLint("RestrictedApi")
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            Location mLastLocation = locationResult.getLastLocation();
            Log.d(TAG, "Location: " + mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void showchart(ArrayList<BarEntry> data, ArrayList<String> date){

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
        barChart.setTouchEnabled(true); // 터치는 가능하게 함
        barChart.setPinchZoom(false);  // 줌 도 못하게 고정
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false); // 더블 탭 확대 못하게 고정
        setMaker(barChart);

    }


    public void datainChart (LocalDate date) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storeAccounts = new ArrayList<>();
        order_histories = new ArrayList<>();
        userAccounts = new ArrayList<>();
        my_order_histories = new ArrayList<>();
        values = new ArrayList<>();
        dates = new ArrayList<String>();
        //datenow = LocalDate.of(2022,6,16);
        sales = new ArrayList<>();
        sum = new float[7];
        for (int i = 0; i < 7; i++) {
            sales.add(0f);
            sum[i] = 0;
        }
        for (int i = 0; i < 9; i++) {
            dates.add("");
            dates.set(i, String.valueOf(datenow.getMonthValue()) + "/" + String.valueOf(datenow.getDayOfMonth() - (8 - i)));
        }
        dates.set(0, "");
        dates.set(1, "");
        firebaseDatabase = FirebaseDatabase.getInstance();
        storeAccount_databaseReference = firebaseDatabase.getReference("BossApp").child("StoreAccount");
        storeAccount_databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeAccounts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StoreAccount storeAccount = dataSnapshot.getValue(StoreAccount.class);
                    // System.out.println(storeAccount);
                    if (storeAccount.getIdToken() != null) {
                        if (storeAccount.getIdToken().equals(user.getUid())) {
                            //storeAccounts.add(storeAccount);
                            truckId = storeAccount.getTruckId();
                            order_histories.clear();
                            order_history_databaseReference = firebaseDatabase.getReference("FoodTruck").child("OrderHistory");
                            Query query = order_history_databaseReference.orderByChild("truckId").equalTo(truckId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {  //반복문으로 리스트를 출력함
                                        Order_history order_history = snapshot1.getValue(Order_history.class); // 객체에 데이터를 담는다
                                        order_histories.add(order_history);
                                        my_order_histories.add(order_history);
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
                                                    }
                                                    break;

                                                }
                                                case 1: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[5] = sum[5] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(5, sum[5]);
                                                    }
                                                    break;
                                                }
                                                case 2: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[4] = sum[4] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(4, sum[4]);
                                                    }
                                                    break;
                                                }
                                                case 3: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[3] = sum[3] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(3, sum[3]);
                                                    }
                                                    break;
                                                }
                                                case 4: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[2] = sum[2] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(2, sum[2]);
                                                    }
                                                    break;
                                                }
                                                case 5: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[1] = sum[1] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(1, sum[1]);
                                                    }
                                                    break;
                                                }
                                                case 6: {
                                                    ArrayList<Order> orders = order_history.getOrders();
                                                    for (int i = 0; i < orders.size(); i++) {
                                                        sum[0] = sum[0] + (Float.parseFloat(orders.get(i).getFood_cost()) * orders.get(i).getFood_number());
                                                        sales.set(0, sum[0]);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        weeksales = 0;
                                        for (int i = 0; i < sales.size(); i++) {
                                            weeksales += sales.get(i);
                                            values.add(new BarEntry(i + 2, sales.get(i).floatValue())); // +2는 앞에 빈 값들임
                                        }
                                    }
                                    TextView textView = view.findViewById(R.id.textView1);
                                    textView.setText(Utils.formatNumber(weeksales, 0, true) + "원");
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
    private void setMaker(BarChart barChart){
        MyMarkerView mv1 = new MyMarkerView(this.getContext(),R.layout.custom_marker_view); // 마커뷰
        mv1.setChartView(barChart);
        barChart.setMarker(mv1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}