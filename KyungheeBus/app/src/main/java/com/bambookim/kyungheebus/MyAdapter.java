package com.bambookim.kyungheebus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MyAdapter extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<SampleData> sample;

    public MyAdapter(Context context, ArrayList<SampleData> data) {
        this.context = context;
        sample = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public SampleData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.listview, null);

        TextView time = (TextView) view.findViewById(R.id.listview_time);

        TextView sun = (TextView) view.findViewById(R.id.listview_sun);
        TextView mon = (TextView) view.findViewById(R.id.listview_mon);
        TextView tue = (TextView) view.findViewById(R.id.listview_tue);
        TextView wed = (TextView) view.findViewById(R.id.listview_wed);
        TextView thur = (TextView) view.findViewById(R.id.listview_thur);
        TextView fri = (TextView) view.findViewById(R.id.listview_fri);
        TextView sat = (TextView) view.findViewById(R.id.listview_sat);

        Switch alarmSwitch = (Switch) view.findViewById(R.id.listview_switch);

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SampleData data = sample.get(position);
                int Id = data.getId();

                DBHelper helper = new DBHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();

                String sql = "";
                String[] arg;

                if (isChecked) {
                    sql = "UPDATE AlarmList " +
                            "SET isON = ?" +
                            "WHERE Id = ?";

                    arg = new String [] {
                            Integer.toString(isChecked ? 1 : 0),
                            Integer.toString(Id)
                    };

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, sample.get(position).getHour());
                    calendar.set(Calendar.MINUTE, sample.get(position).getMinute());
                    calendar.set(Calendar.SECOND, 0);

                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1);
                    }

                    TimeActivity.initializeAlarm(context, Id, calendar);

                } else {
                    sql = "UPDATE AlarmList " +
                            "SET isON = ?" +
                            "WHERE Id = ?";

                    arg = new String [] {
                            Integer.toString(isChecked ? 1 : 0),
                            Integer.toString(Id)
                    };

                    TimeActivity.switchOffAlarm(context, Id);
                }

                db.execSQL(sql, arg);
                db.close();
            }
        });

        int hour, minute;
        hour = sample.get(position).getHour();
        minute = sample.get(position).getMinute();

        String _hour, _minute;

        if (hour < 10) {
            _hour = "0" + hour;
        } else {
            _hour = Integer.toString(hour);
        }

        if (minute < 10) {
            _minute = "0" + minute;
        } else {
            _minute = Integer.toString(minute);
        }

        String str = _hour + ":" + _minute;

        time.setText(str);

        boolean[] isDayOn = sample.get(position).getIsDayOn();
        for (int i = 0; i < isDayOn.length; i++) {
            if (isDayOn[i]) {
                switch (i) {
                    case 0:
                        sun.setTextColor(Color.RED);
                        break;
                    case 1:
                        mon.setTextColor(Color.RED);
                        break;
                    case 2:
                        tue.setTextColor(Color.RED);
                        break;
                    case 3:
                        wed.setTextColor(Color.RED);
                        break;
                    case 4:
                        thur.setTextColor(Color.RED);
                        break;
                    case 5:
                        fri.setTextColor(Color.RED);
                        break;
                    case 6:
                        sat.setTextColor(Color.RED);
                        break;
                }
            }
        }

        boolean isSwitchOn = sample.get(position).getIsSwitchOn();
        if (isSwitchOn) {
            alarmSwitch.setChecked(true);
        } else {
            alarmSwitch.setChecked(false);
        }

        return view;
    }
}

class SampleData {

    private int Id;

    private int hour;
    private int minute;

    private boolean[] isDayOn;
    private boolean isSwitchOn;

    public SampleData(int Id, int hour, int minute, boolean isSwitchOn, boolean... isDayOn) {
        this.Id = Id;
        this.hour = hour;
        this.minute = minute;
        this.isSwitchOn = isSwitchOn;

        this.isDayOn = new boolean[7];
        this.isDayOn[0] = isDayOn[0];
        this.isDayOn[1] = isDayOn[1];
        this.isDayOn[2] = isDayOn[2];
        this.isDayOn[3] = isDayOn[3];
        this.isDayOn[4] = isDayOn[4];
        this.isDayOn[5] = isDayOn[5];
        this.isDayOn[6] = isDayOn[6];
    }

    public int getId() {
        return this.Id;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public boolean[] getIsDayOn() {
        return this.isDayOn;
    }

    public boolean getIsSwitchOn() {
        return this.isSwitchOn;
    }
}
