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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.stepbystep.bossapp.DO.Notice;
import com.stepbystep.bossapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class NoticeActivity extends AppCompatActivity {

    private DatabaseReference databaseReference; //파이어베이스 실시간 데이터베이스
    private ArrayList<Notice> arrayList;
    int count = 0;
    NoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        //액션바 지우기
        ActionBar abar = getSupportActionBar();
        abar.hide();
        setRecycleView();
    }

    private void setRecycleView(){
        RecyclerView recyclerView = findViewById(R.id.activity_notice_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        noticeAdapter = new NoticeAdapter();
        arrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodTruck").child("notify");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //데이터 받아오기
                arrayList.clear(); // 기존 데이터가 존재하지 않게 초기화
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notice notice = dataSnapshot.getValue(Notice.class); // notice 객체에 데이터를 담는다
                    arrayList.add(notice);
                }
                noticeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("log","실패실패실패");
            }
        });
        Collections.reverse(arrayList);
        noticeAdapter.setItems(arrayList);
        recyclerView.setAdapter(noticeAdapter);
        noticeAdapter.setOnItemClickListener(new OnNoticeItemClickListener() {
            @Override
            public void onItemClick(NoticeAdapter.ViewHolder holder, View view, int position) {
                Intent intent = new Intent(view.getContext(), NoticeContentActivity.class);
                Notice notice = noticeAdapter.getItem(position);
                intent.putExtra("Notice",notice);
                startActivity(intent);
            }
        });
    }

}