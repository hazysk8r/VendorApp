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


        loadReviews();//Rating과 Comment를 통합하여 한 번만 호출하도록

        return view;
    }

    private void loadReviews() {
        // TruckId에 해당하는 리뷰를 한 번만 요청
        review_database.orderByChild("truckId").equalTo(truckId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 1. 리스트 초기화 (중복 방지)
                ratesList.clear();
                commentList.clear();

                // 2. 평점 집계용 변수
                float sum = 0;
                int[] starCounts = {0, 0, 0, 0, 0}; // 1~5점 카운트

                // 3. 데이터 파싱
                for (DataSnapshot unit : snapshot.getChildren()) {
                    Review review = unit.getValue(Review.class);
                    if (review != null) {
                        ratesList.add(review);
                        commentList.add(review); // 

                        // 평점 계산 로직 개선 (String -> Float)
                        try {
                            float rate = Float.parseFloat(review.getRate());
                            sum += rate;

                            // 평점 분포 계산 (1점대, 2점대... 5점)
                            int starIndex = (int) Math.floor(rate) - 1; 
                            if (starIndex >= 0 && starIndex < 5) {
                                starCounts[starIndex]++;
                            } else if (starIndex == 4 || rate == 5.0) { // 5.0 처리
                                starCounts[4]++;
                            }
                        } catch (NumberFormatException e) {
                            // 평점 데이터 오류 시 처리
                        }
                    }
                }

                // 4. UI 업데이트 (평점 정보)
                updateRatingUI(ratesList.size(), sum, starCounts);

                // 5. 리사이클러뷰 갱신
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "리뷰를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRatingUI(int quantity, float sum, int[] starCounts) {
        quantityRating.setText(String.valueOf(quantity));

        if (quantity == 0) {
            ratingNum.setText("0.0");
            ratingBar.setRating(0);
            star1.setProgress(0); star2.setProgress(0); star3.setProgress(0);
            star4.setProgress(0); star5.setProgress(0);
        } else {
            float average = sum / quantity;
            // 소수점 한 자리 반올림
            average = (float) (Math.round(average * 10) / 10.0);
            
            ratingNum.setText(String.valueOf(average));
            ratingBar.setRating(average);

            // ProgressBar 비율 설정
            star1.setProgress((starCounts[0] * 100) / quantity);
            star2.setProgress((starCounts[1] * 100) / quantity);
            star3.setProgress((starCounts[2] * 100) / quantity);
            star4.setProgress((starCounts[3] * 100) / quantity);
            star5.setProgress((starCounts[4] * 100) / quantity);
        }
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
