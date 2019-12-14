package com.bambookim.kyungheebus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeActivity extends AppCompatActivity {

    TimePicker timePicker;
    CheckBox sun, mon, tue, wed, thur, fri, sat;

    String src = "";

    int Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        sun = (CheckBox) findViewById(R.id.checkBox_sun);
        mon = (CheckBox) findViewById(R.id.checkBox_mon);
        tue = (CheckBox) findViewById(R.id.checkBox_tue);
        wed = (CheckBox) findViewById(R.id.checkBox_wed);
        thur = (CheckBox) findViewById(R.id.checkBox_thur);
        fri = (CheckBox) findViewById(R.id.checkBox_fri);
        sat = (CheckBox) findViewById(R.id.checkBox_sat);

        Intent fromIntent = getIntent();
        src = fromIntent.getStringExtra("Src");

        if (src.equals("modify")) {

            int Hour = fromIntent.getIntExtra("Hour", 0);
            int Minute = fromIntent.getIntExtra("Minute", 0);
            int SUN = fromIntent.getIntExtra("SUN", 0);
            int MON = fromIntent.getIntExtra("MON", 0);
            int TUE = fromIntent.getIntExtra("TUE", 0);
            int WED = fromIntent.getIntExtra("WED", 0);
            int THUR = fromIntent.getIntExtra("THUR", 0);
            int FRI = fromIntent.getIntExtra("FRI", 0);
            int SAT = fromIntent.getIntExtra("SAT", 0);
            Id = fromIntent.getIntExtra("Id", 0);

            if (Build.VERSION.SDK_INT >= 23) {
                timePicker.setHour(Hour);
                timePicker.setMinute(Minute);
            } else {
                timePicker.setCurrentHour(Hour);
                timePicker.setCurrentMinute(Minute);
            }

            sun.setChecked(SUN != 0);
            mon.setChecked(MON != 0);
            tue.setChecked(TUE != 0);
            wed.setChecked(WED != 0);
            thur.setChecked(THUR != 0);
            fri.setChecked(FRI != 0);
            sat.setChecked(SAT != 0);
        } else {
            long currentTime = System.currentTimeMillis();

            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(currentTime);

            SimpleDateFormat HourFormat = new SimpleDateFormat("hh", Locale.getDefault());
            SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());

            int pre_hour = Integer.parseInt(HourFormat.format(calendar.getTime()));
            int pre_minute = Integer.parseInt(MinuteFormat.format(calendar.getTime()));

            int isAMorPM = calendar.get(Calendar.AM_PM);
            if (isAMorPM == Calendar.PM) {
                pre_hour += 12;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                timePicker.setHour(pre_hour);
                timePicker.setMinute(pre_minute);
            } else {
                timePicker.setCurrentHour(pre_hour);
                timePicker.setCurrentMinute(pre_minute);
            }
        }
    }

    public void mOnClick(View view) {
        switch (view.getId()) {
            case R.id.alarmSave:

                int hour, minute;

                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }







                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }








                int _sun, _mon, _tue, _wed, _thur, _fri, _sat;

                _sun = sun.isChecked() ? 1 : 0;
                _mon = mon.isChecked() ? 1 : 0;
                _tue = tue.isChecked() ? 1 : 0;
                _wed = wed.isChecked() ? 1 : 0;
                _thur = thur.isChecked() ? 1 : 0;
                _fri = fri.isChecked() ? 1 : 0;
                _sat = sat.isChecked() ? 1 : 0;

                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();

                String sql = "";
                String [] arg;

                if (src.equals("modify")) {

                    sql = "UPDATE AlarmList " +
                            "SET Hour = ?," +
                            "Minute = ?," +
                            "SUN = ?," +
                            "MON = ?," +
                            "TUE = ?," +
                            "WED = ?," +
                            "THUR = ?," +
                            "FRI = ?," +
                            "SAT = ?" +
                            "WHERE Id = ?";

                    arg = new String[] {
                            Integer.toString(hour),
                            Integer.toString(minute),
                            Integer.toString(_sun),
                            Integer.toString(_mon),
                            Integer.toString(_tue),
                            Integer.toString(_wed),
                            Integer.toString(_thur),
                            Integer.toString(_fri),
                            Integer.toString(_sat),
                            Integer.toString(Id)
                    };

                    initializeAlarm(getApplicationContext(), Id, calendar);

                } else {

                    Cursor c = db.rawQuery("SELECT * FROM AlarmList", null);

                    int len = 0;
                    while (c.moveToNext()) {
                        len++;
                    }

                    Id = len + 1;

                    sql = "INSERT INTO AlarmList " +
                            "(Id, Hour, Minute, SUN, MON, TUE, WED, THUR, FRI, SAT, isON) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    arg = new String[] {
                            Integer.toString(Id),
                            Integer.toString(hour),
                            Integer.toString(minute),
                            Integer.toString(_sun),
                            Integer.toString(_mon),
                            Integer.toString(_tue),
                            Integer.toString(_wed),
                            Integer.toString(_thur),
                            Integer.toString(_fri),
                            Integer.toString(_sat),
                            "1"
                    };

                    initializeAlarm(getApplicationContext(), Id, calendar);

                }

                db.execSQL(sql, arg);

                db.close();

                finish();

                break;
            case R.id.alarmCancel:
                finish();
                break;
        }
    }

    static int count = 0;
    static boolean isFirstOfToday = false;

    public static void initializeAlarm(Context context, int reqCode, Calendar calendar) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        Intent cancelIntent = new Intent(context, AlarmReceiver.class);
//        PendingIntent cancelPending = PendingIntent.getBroadcast(context, reqCode, cancelIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("Id", reqCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        manager.cancel(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void repeatAlarmCallback(Context context, int reqCode) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int delay = 30 * 1000;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delay, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delay, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delay, pendingIntent);
        }
    }

    public static void switchOffAlarm(Context context, int reqCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent cancelIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, reqCode, cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (sender != null) {
            alarmManager.cancel(sender);
            sender.cancel();
        }

        count = 0;
    }
}