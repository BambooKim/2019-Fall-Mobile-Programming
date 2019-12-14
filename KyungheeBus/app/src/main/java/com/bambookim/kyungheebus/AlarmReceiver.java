package com.bambookim.kyungheebus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int reqCode = intent.getIntExtra("Id", 0);

        if (TimeActivity.count == 0) {
            TimeActivity.isFirstOfToday = true;
        }

        if (TimeActivity.isFirstOfToday) {

            Log.d(TAG, "if isFirstOfToday");

            TimeActivity.isFirstOfToday = false;

            Calendar calendar = Calendar.getInstance();
            int nWeek = calendar.get(Calendar.DAY_OF_WEEK);     // SUN : 1 ~ SAT : 7

            DBHelper helper = new DBHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();

            String sql = "SELECT * FROM AlarmList";

            Cursor c = db.rawQuery(sql, null);

            while (c.moveToNext()) {
                int Id_pos = c.getColumnIndex("Id");
                int DAY_pos;
                switch (nWeek) {
                    case 1:
                        DAY_pos = c.getColumnIndex("SUN");
                        break;
                    case 2:
                        DAY_pos = c.getColumnIndex("MON");
                        break;
                    case 3:
                        DAY_pos = c.getColumnIndex("TUE");
                        break;
                    case 4:
                        DAY_pos = c.getColumnIndex("WED");
                        break;
                    case 5:
                        DAY_pos = c.getColumnIndex("THUR");
                        break;
                    case 6:
                        DAY_pos = c.getColumnIndex("FRI");
                        break;
                    case 7:
                        DAY_pos = c.getColumnIndex("SAT");
                        break;
                    default:
                        DAY_pos = 0;
                }

                int Id = c.getInt(Id_pos);
                int DAY = c.getInt(DAY_pos);

                Log.d(TAG, Integer.toString(DAY));

                if (Id == reqCode) {
                    if (DAY != 0) {
                        Log.d(TAG, TimeActivity.count + "번째 반복");
                        Toast.makeText(context, TimeActivity.count + " : Hello World", Toast.LENGTH_SHORT).show();
                        StationNotiTask task = new StationNotiTask(context);
                        task.execute();

                        TimeActivity.count++;

                        TimeActivity.repeatAlarmCallback(context, reqCode);
                    }
                    break;
                }
            }
        } else {
            Log.d(TAG, TimeActivity.count + "번째 반복");
            Toast.makeText(context, TimeActivity.count + " : Hello World", Toast.LENGTH_SHORT).show();
            StationNotiTask task = new StationNotiTask(context);
            task.execute();

            TimeActivity.repeatAlarmCallback(context, reqCode);

            TimeActivity.count++;
        }

        if (TimeActivity.count >= 30) {
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            TimeActivity.switchOffAlarm(context, reqCode);

//            Intent cancelIntent = new Intent(context, AlarmReceiver.class);
//            PendingIntent sender = PendingIntent.getBroadcast(context, reqCode, cancelIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            if (sender != null) {
//                alarmManager.cancel(sender);
//                sender.cancel();
//            }

            TimeActivity.count = 0;


            Intent tomorrowIntent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("Id", reqCode);
            PendingIntent tomorrowPending = PendingIntent.getBroadcast(context, reqCode, tomorrowIntent, 0);

            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        24 * 60 * 60 * 1000 - 15 * 60 * 1000, tomorrowPending);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.RTC_WAKEUP,
                        24 * 60 * 60 * 1000 - 15 * 60 * 1000, tomorrowPending);
            } else {
                manager.set(AlarmManager.RTC_WAKEUP,
                        24 * 60 * 60 * 1000 - 15 * 60 * 1000, tomorrowPending);
            }
        }
    }
}
