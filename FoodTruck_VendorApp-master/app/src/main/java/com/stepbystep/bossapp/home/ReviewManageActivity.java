package com.stepbystep.bossapp.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepbystep.bossapp.DO.StoreAccount;
import com.stepbystep.bossapp.R;

public class ReviewManageActivity extends AppCompatActivity {
    TruckReviewFragment truckReviewFragment;
    Bundle bundle;

    //파이어베이스 인증
    FirebaseAuth mAuth;
    FirebaseUser user;
    //데이터베이스 참조 변수
    DatabaseReference databaseReference;
    //파이어베이스 저장소
    String truckId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_manage);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        truckReviewFragment = new TruckReviewFragment();

        bundle = new Bundle();

        databaseReference = FirebaseDatabase.getInstance().getReference("BossApp");

        if(user != null){
            //유저 데이터 받아오기
            databaseReference.child("StoreAccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        StoreAccount storeAccount = snapshot.getValue(StoreAccount.class);
                        truckId = storeAccount.getTruckId();
                        bundle.putString("truck" ,truckId);
                        TruckReviewFragment truckReviewFragment = new TruckReviewFragment();
                        truckReviewFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.reviewContainer, truckReviewFragment).commitAllowingStateLoss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        ActionBar ac = getSupportActionBar();
        ac.hide();
    }
}
