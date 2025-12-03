package com.stepbystep.bossapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.stepbystep.bossapp.R;

public class FindPasswordActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    static final String TAG = "Find Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //액션바 지우기
        ActionBar abar = getSupportActionBar();
        abar.hide();
        setContentView(R.layout.activity_find_password);
        button = findViewById(R.id.activity_find_password_button);
        editText = findViewById(R.id.activity_find_password_editText);
        button.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String emailAddress = editText.getText().toString();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(v.getContext(),"이메일에 메일을 보냈습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(v.getContext(),"없는 계정입니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    };
}
