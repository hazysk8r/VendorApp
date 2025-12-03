package com.stepbystep.bossapp.account;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.stepbystep.bossapp.R;

public class AlertReceiver extends BroadcastReceiver {
    public static final String TYPE_ALARM = "축제알람";

    public static final String EXTRA_MESSAGE = "Alarm_Message";
    public static final String EXTRA_TYPE = "Type";

    private final int ID_ALARM = 100;

    private final String DATE_FORMAT = "yyyy-MM-dd";
    private final String TIME_FORMAT = "HH:mm";

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = TYPE_ALARM;
        int notifyId = ID_ALARM;


        showToast(context, title, message);

        showAlarmNotification(context, title, message, notifyId);
    }

    private void showToast(Context context, String title, String message) {
        Toast.makeText(context, title + " : " + message, Toast.LENGTH_SHORT).show();
    }

    public void setOnTimeAlarm(Context context, String date, String time, String message) {

        if (isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);

        String DateArr[] = date.split("-");
        String TimeArr[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(DateArr[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(DateArr[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(DateArr[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(TimeArr[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(TimeArr[1]));
        calendar.set(Calendar.SECOND, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ALARM, intent, PendingIntent.FLAG_IMMUTABLE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            //RTC_WAKEUP- 알람으로 지정된 시간에 핸드폰의 절전 모드를 해제하여 대기 중인 인텐트 실행
            Toast.makeText(context, "알람 등록 성공", Toast.LENGTH_SHORT).show();
        }else {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            //RTC_WAKEUP- 알람으로 지정된 시간에 핸드폰의 절전 모드를 해제하여 대기 중인 인텐트 실행
            Toast.makeText(context, "알람 등록 성공", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat stringToDate = new SimpleDateFormat(format, Locale.getDefault());
            stringToDate.setLenient(false);
            stringToDate.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_ID";
        String CHANNEL_NAME = "Channel_Name";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notify_icon)
                .setContentTitle(title)
                .setContentText(message) //사용자가 입력한 메세지
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000})
                .setSound(alarmSound);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{1000, 1000, 1000});

        builder.setChannelId(CHANNEL_ID);

        if (notificationManagerCompat != null) {
            notificationManagerCompat.createNotificationChannel(channel);
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlertReceiver.class);
        int requestCode = ID_ALARM;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            pendingIntent.cancel();

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }

            Toast.makeText(context, "알람 취소", Toast.LENGTH_SHORT).show();

        }else {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }

            Toast.makeText(context, "알람 취소", Toast.LENGTH_SHORT).show();
        }




    }
}