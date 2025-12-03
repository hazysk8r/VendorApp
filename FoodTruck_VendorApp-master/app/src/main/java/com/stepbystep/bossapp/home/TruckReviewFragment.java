package com.stepbystep.bossapp.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stepbystep.bossapp.R;
import com.stepbystep.bossapp.DO.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TruckReviewFragment extends Fragment {

    String truckId;
    RecyclerView recyclerView;
    RateReviewAdapter reviewAdapter;
    ArrayList<Review> ratesList;
    ArrayList<Review> commentList;
    TextView ratingNum, quantityRating;
    RatingBar ratingBar;
    ProgressBar star1, star2, star3,star4, star5;

    DatabaseReference review_database;
    DatabaseReference truck_database;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public TruckReviewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_truck_review, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        review_database =  FirebaseDatabase.getInstance().getReference("FoodTruck").child("Review");
        truck_database =  FirebaseDatabase.getInstance().getReference("FoodTruck").child("Truck");
        truckId = getArguments().getString("truck");
        recyclerView = view.findViewById(R.id.rcv_comment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentList = new ArrayList<>();
        ratesList = new ArrayList<>();
        reviewAdapter = new RateReviewAdapter(commentList, getActivity());
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ratingNum =view.findViewById(R.id.ratingNum);
        ratingBar=view.findViewById(R.id.ratingBarFrag);
        quantityRating=view.findViewById(R.id.ratingQuantity);

        star1=view.findViewById(R.id.pb1);
        star2=view.findViewById(R.id.pb2);
        star3=view.findViewById(R.id.pb3);
        star4=view.findViewById(R.id.pb4);
        star5=view.findViewById(R.id.pb5);


        getRating();
        getComment();

        return view;
    }

    private void getRating() { //별점 받아와서 데이터 기반으로 progressbar와 평균 계산

        review_database.orderByChild("truckId").equalTo(truckId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sum =0;
                float[] pb = {0,0,0,0,0};
                for(DataSnapshot unit : snapshot.getChildren()){
                    ratesList.add(unit.getValue(Review.class));
                }
                int quantity = ratesList.size();
                for (Review item: ratesList) {
                    sum += Float.parseFloat(item.getRate());
                    switch (item.getRate()){
                        case "1": case "1.0": case "1.5":
                            pb[0] += 1;
                            break;
                        case "2": case "2.0": case "2.5":
                            pb[1] += 1;
                            break;
                        case "3": case "3.0": case "3.5":
                            pb[2] += 1;
                            break;
                        case "4": case "4.0": case "4.5":
                            pb[3] += 1;
                            break;
                        case "5": case "5.0":
                            pb[4] += 1;
                            break;
                    }
                }
                float ave = (float)sum/quantity;
                DecimalFormat df = new DecimalFormat("#.#");
                df.format(ave);

                quantityRating.setText(String.valueOf(quantity));
                if(quantity==0){
                    ratingNum.setText("5");
                    ratingBar.setRating(5);
                }
                else {
                    ave = (float) (Math.ceil(ave*10)/10.0);
                    ratingNum.setText(String.valueOf(ave));
                    ratingBar.setRating(ave);
                }
                for (int i = 0; i<5;i++ ){
                    pb[i] = (pb[i]/quantity)*100;
                }
                star1.setProgress((int) pb[0]);
                star2.setProgress((int) pb[1]);
                star3.setProgress((int) pb[2]);
                star4.setProgress((int) pb[3]);
                star5.setProgress((int) pb[4]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getComment() { // Comment 및 유저이름 받아오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("FoodTruck").child("Review");
        myRef.orderByChild("truckId").equalTo(truckId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot unit : snapshot.getChildren()){
                    commentList.add( unit.getValue(Review.class));
                }
                for (Review item: commentList) {
                    DatabaseReference reference = database.getReference("FoodTruck").child("Review");
                    reference.orderByChild("idToken").equalTo(item.getUser_idToken()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                item.setUserName(snapshot1.child("userName").getValue().toString());
                            }
                            reviewAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}