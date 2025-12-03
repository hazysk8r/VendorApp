package com.stepbystep.bossapp.login;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.stepbystep.bossapp.R;


public class SignupActivity extends AppCompatActivity {

    private Button downloadButton;
    private TextView emailCopyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //액션바 지우기
        ActionBar abar = getSupportActionBar();
        abar.hide();
        //FirebaseAuth 인스턴스를 초기화
        downloadButton = findViewById(R.id.activity_signup_download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/foodtruck-309c3.app" +
                        "spot.com/o/%ED%91%B8%EB%93%9C%ED%8A%B8%EB%9F%AD%20%EB%93%B1%EB%A1%9D%20%EC%" +
                        "96%91%EC%8B%9D.doc?alt=media&token=cb672272-9cee-4596-adb6-ec117b83bae9");

                DownloadManager.Request request = new DownloadManager.Request(uri);
                String title = URLUtil.guessFileName("https://firebasestorage.googleapis.com/v0/" +
                        "b/foodtruck-309c3.appspot.com/o/%ED%91%B8%EB%93%9C%ED%8A%B8%EB%9F%AD%20%EB%" +
                        "93%B1%EB%A1%9D%20%EC%96%91%EC%8B%9D.doc?alt=media&token=cb672272-9cee-4596-" +
                        "adb6-ec117b83bae9","푸드트럭 등록 양식",null);

                request.setTitle(title);
                request.setDescription("푸드트럭 등록 양식을 다운로드 중입니다.");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title);
                manager.enqueue(request);
            }
        });
        emailCopyButton = findViewById(R.id.activity_email_copy_button);
        emailCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "FoodTrucker@naver.com");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(),"이메일 주소 복사됨",Toast.LENGTH_SHORT).show();
            }
        });
    }



}