package com.bambookim.kyungheebus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람 스타트
        AlarmUtils.getInstance().startThirtySecondAlram(context);

        StationNotiTask task = new StationNotiTask(context);
        task.execute();

        Log.d(getClass().getName(),"time second : " + new Date().toString());
    }
}
