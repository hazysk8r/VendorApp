package com.stepbystep.bossapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    protected void FinishWithAnim() {
        finish();
        if (isFinishing()) {

            overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
        }
    }
    protected void BackWithAnim() {
        finish();
        if (isFinishing()) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    }

    public static void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFinishing()) {
            overridePendingTransition(R.anim.slide_none, R.anim.slide_out_right);
        }
    }


}
