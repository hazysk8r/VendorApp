package com.stepbystep.bossapp.home;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ImageView;
import com.stepbystep.bossapp.R;

import com.stepbystep.bossapp.databinding.ActivityFullPicBinding;
import com.squareup.picasso.Picasso;


public class FullPicActivity extends AppCompatActivity {

    private ImageView mContentView;
    private ActivityFullPicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullPicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent get = getIntent();
        String img = get.getStringExtra("url");
        mContentView = findViewById(R.id.fullscreen_img);
        Picasso.get().load(img).into(mContentView);

        ActionBar abar = getSupportActionBar();
        abar.hide();
    }


}