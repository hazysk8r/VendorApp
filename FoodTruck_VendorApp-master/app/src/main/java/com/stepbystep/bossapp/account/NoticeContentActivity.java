package com.stepbystep.bossapp.account;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.stepbystep.bossapp.DO.Notice;
import com.stepbystep.bossapp.R;

public class NoticeContentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_content);
        //액션바 지우기
        ActionBar abar = getSupportActionBar();
        abar.hide();
        Intent intent = getIntent();
        Notice notice = (Notice) intent.getSerializableExtra("Notice");
        ((TextView) findViewById(R.id.activity_notice_content_title2)).setText(notice.getTitle().replace("\\n", System.getProperty("line.separator")));
        ((TextView) findViewById(R.id.activity_notice_content_content)).setText(notice.getContent().replace("\\n", System.getProperty("line.separator")));
    }
}