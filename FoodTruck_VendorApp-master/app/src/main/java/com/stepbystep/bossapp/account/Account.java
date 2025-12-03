package com.stepbystep.bossapp.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.stepbystep.bossapp.DO.Image;
import com.stepbystep.bossapp.MainActivity;
import com.stepbystep.bossapp.R;
import com.stepbystep.bossapp.DO.StoreAccount;

public class Account extends Fragment {

    private FirebaseUser mAuth; //파이어페이스 유저
    private DatabaseReference databaseReference; //파이어베이스 실시간 데이터베이스
    TextView textView_name;
    TextView textView_email;
    StoreAccount account;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);
        //리싸이클뷰 설정
        setRecycleView(rootView, container);
        //데이터베이스, 유저 받아오기
        textView_name = rootView.findViewById(R.id.fragment_account_textView_name);
        textView_email = rootView.findViewById(R.id.fragment_account_textView_email);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("BossApp").child("StoreAccount").child(mAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    account = (StoreAccount)snapshot.getValue(StoreAccount.class);
                    String name = account.getName();
                    String email = account.getEmail();
                    textView_name.setText(name);
                    textView_email.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;
    }

    private void  setRecycleView(ViewGroup rootView, ViewGroup container){
        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_account_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        AccountAdapter accountAdapter = new AccountAdapter();
        Image image1= new Image(container.getResources().getDrawable(R.drawable.profile_icon), "내 계정");
        Image image2= new Image(container.getResources().getDrawable(R.drawable.logout_icon), "로그아웃");
        Image image3= new Image(container.getResources().getDrawable(R.drawable.festival_icon), "축제 알림설정");
        Image image4= new Image(container.getResources().getDrawable(R.drawable.notify_icon), "공지사항");
        Image image5= new Image(container.getResources().getDrawable(R.drawable.call_icon), "고객 지원");
        accountAdapter.addItem(image1);
        accountAdapter.addItem(image2);
        accountAdapter.addItem(image3);
        accountAdapter.addItem(image4);
        accountAdapter.addItem(image5);
        recyclerView.setAdapter(accountAdapter);

        //리스너 설정
        accountAdapter.setOnItemClickListener(new OnAccountItemClickListener() {
            @Override
            public void onItemClick(AccountAdapter.ViewHolder holder, View view, int position) {
                //내 계정
                if(position == 0){
                    Intent intent = new Intent(rootView.getContext(), AccountManagementActivity.class);
                    intent.putExtra("StoreAccount",account);
                    startActivity(intent);
                }
                //로그아웃
                else if(position == 1){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(rootView.getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(rootView.getContext(), "로그아웃하셨습니다.", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("order",getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(intent);
                }
                //축제 알림설정
                else if(position == 2){
                    Intent intent = new Intent(rootView.getContext(), FestivalActivity.class);
                    startActivity(intent);
                }
                //공지사항
                else if(position ==3){
                    Intent intent = new Intent(rootView.getContext(), NoticeActivity.class);
                    startActivity(intent);
                }
                //고객지원
                else if(position == 4) {

                }
            }
        });
    }


}