package com.stepbystep.bossapp.account;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.stepbystep.bossapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener{
    private Calendar calendar;
    public AlertReceiver alertReceiver;

    EditText edtMessage;
    TextView tvDate;
    TextView tvTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        ActionBar abar = getSupportActionBar();
        abar.hide();

        Intent intent = getIntent();

        this.calendar = Calendar.getInstance();
        displayDate();

        edtMessage = findViewById(R.id.edt_message);
        tvDate = findViewById(R.id.txtDate);
        tvTime = findViewById(R.id.txtTime);

        findViewById(R.id.btnCalendar).setOnClickListener(this);
        findViewById(R.id.btnTime).setOnClickListener(this);
        findViewById(R.id.btnAlarm).setOnClickListener(this);
        findViewById(R.id.btnAlarmCancel).setOnClickListener(this);

        alertReceiver = new AlertReceiver();
    }

    private void displayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ((TextView) findViewById(R.id.txtDate)).setText(format.format(this.calendar.getTime()));
    }

    private void displayTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        ((TextView) findViewById(R.id.txtTime)).setText(format.format(this.calendar.getTime()));
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, dayOfMonth);

                displayDate();
            }
        }, this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                displayTime();
            }
        }, this.calendar.get(Calendar.HOUR_OF_DAY), this.calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalendar:
                showDatePicker();
                break;

            case R.id.btnTime:
                showTimePicker();
                break;

            case R.id.btnAlarm:
                String onceDate = tvDate.getText().toString();
                String onceTime = tvTime.getText().toString();
                String onceMessage = edtMessage.getText().toString();

                alertReceiver.setOnTimeAlarm(this, onceDate, onceTime, onceMessage);
                break;

            case R.id.btnAlarmCancel:
                alertReceiver.cancelAlarm(this);
                break;
        }
    }
}
