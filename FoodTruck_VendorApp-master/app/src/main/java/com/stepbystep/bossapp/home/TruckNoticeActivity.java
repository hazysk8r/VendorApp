package com.stepbystep.bossapp.home;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepbystep.bossapp.AnimActivity;
import com.stepbystep.bossapp.DO.StoreAccount;
import com.stepbystep.bossapp.R;
import com.stepbystep.bossapp.databinding.ActivityTruckNoticeBinding;


public class TruckNoticeActivity extends AnimActivity {
    private ActivityTruckNoticeBinding binding;
    DatabaseReference databaseReference;
    FirebaseUser user;
    StoreAccount storeAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("BossApp").child("StoreAccount").child(user.getUid());

        binding = ActivityTruckNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar abar = getSupportActionBar();
        abar.hide();

        initButtonListener();
        initAddTextChangeListener();
    }
    private void initAddTextChangeListener(){
        binding.etNotice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 255){
                    Toast.makeText(TruckNoticeActivity.this,"255자 까지만 입력할 수 있습니다.",Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                binding.tvInputCounter.setText(String.format("(%d/255자)",s.length()));   //글자수 TextView에 보여주기.
                if(s.length() >= 255){
                    binding.tvInputCounter.setTextColor(getColor(R.color.error));
                }else{
                    binding.tvInputCounter.setTextColor(getColor(R.color.grey));
                }
            }
        });
    }

    private void initButtonListener(){
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FinishWithAnim();
            }
        });

        binding.btnSaveNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotice();
            }
        });
    }

    private void saveNotice(){
        try {
            binding.progressBarNotice.setVisibility(View.VISIBLE);
            String NoticeTexts = binding.etNotice.getText().toString();
            if(user != null){
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            storeAccount = snapshot.getValue(StoreAccount.class);
                            databaseReference.child("vendor_notice").push().setValue(NoticeTexts);
                            binding.progressBarNotice.setVisibility(View.GONE);
                            Toast.makeText(TruckNoticeActivity.this, "공지사항을 저장했습니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TruckNoticeActivity.this, "공지사항을 저장하지 못했습니다.", Toast.LENGTH_LONG).show();

                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(TruckNoticeActivity.this, "공지사항을 저장하지 못했습니다.", Toast.LENGTH_LONG).show();
            Log.e("e = ", e.getMessage());
            binding.progressBarNotice.setVisibility(View.GONE);
        }
    }


}
