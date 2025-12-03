package com.stepbystep.bossapp.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.stepbystep.bossapp.DO.Festival;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class FestivalActivity extends AppCompatActivity {

    private DatabaseReference databaseReference; //파이어베이스 실시간 데이터베이스
    private ArrayList<Festival> arrayList;
    int count = 0;
    FestivalAdapter festivalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        //액션바 지우기
        ActionBar abar = getSupportActionBar();
        abar.hide();
        setRecycleView();
    }

    private void setRecycleView(){
        RecyclerView recyclerView = findViewById(R.id.activity_festival_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        festivalAdapter = new FestivalAdapter();
        arrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("BossApp").child("Festival");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //데이터 받아오기
                arrayList.clear(); // 기존 데이터가 존재하지 않게 초기화
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Festival festival = dataSnapshot.getValue(Festival.class); //
                    arrayList.add(festival);
                }
                festivalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("log","실패실패실패");
            }
        });
        Collections.reverse(arrayList);
        festivalAdapter.setItems(arrayList);
        recyclerView.setAdapter(festivalAdapter);
        festivalAdapter.setOnItemClickListener(new OnFestivalItemClickListener() {
            @Override
            public void onItemClick(FestivalAdapter.ViewHolder holder, View view, int position) {
                Intent intent = new Intent(view.getContext(), FestivalContentActivity.class);
                Festival festival = festivalAdapter.getItem(position);
                intent.putExtra("Festival",festival);
                startActivity(intent);
            }
        });
    }

}
