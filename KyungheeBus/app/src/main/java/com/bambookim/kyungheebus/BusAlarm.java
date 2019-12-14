package com.bambookim.kyungheebus;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class BusAlarm extends AppCompatActivity {
    private static final String TAG = "BusAlarm";
    ListView listView;

    ArrayList<SampleData> alarmList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.not_move_activity, R.anim.leftout_activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_alarm);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("버스 도착 알림 설정");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.set
        this.initializeAlarm();

        listView = (ListView) findViewById(R.id.listView);
        final MyAdapter myAdapter = new MyAdapter(this, alarmList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick");
                //Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_SHORT).show();

                SampleData data = myAdapter.getItem(position);

                int Id = data.getId();

                int Hour = data.getHour();
                int Minute = data.getMinute();

                boolean [] isDayOn = data.getIsDayOn();

                int SUN = isDayOn[0] ? 1 : 0;
                int MON = isDayOn[1] ? 1 : 0;
                int TUE = isDayOn[2] ? 1 : 0;
                int WED = isDayOn[3] ? 1 : 0;
                int THUR = isDayOn[4] ? 1 : 0;
                int FRI = isDayOn[5] ? 1 : 0;
                int SAT = isDayOn[6] ? 1 : 0;

                Intent intent = new Intent(BusAlarm.this, TimeActivity.class);
                intent.putExtra("Src", "modify");
                intent.putExtra("Id", Id);
                intent.putExtra("Hour", Hour);
                intent.putExtra("Minute", Minute);
                intent.putExtra("SUN", SUN);
                intent.putExtra("MON", MON);
                intent.putExtra("TUE", TUE);
                intent.putExtra("WED", WED);
                intent.putExtra("THUR", THUR);
                intent.putExtra("FRI", FRI);
                intent.putExtra("SAT", SAT);

                startActivity(intent);
                overridePendingTransition(R.anim.leftin_activity, R.anim.not_move_activity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus_alarm, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btn_addAlarm:
                Intent intent = new Intent(BusAlarm.this, TimeActivity.class);
                intent.putExtra("Src", "new");
                startActivity(intent);
                overridePendingTransition(R.anim.leftin_activity, R.anim.not_move_activity);
                break;
            case R.id.action_btn_editAlarm:
                break;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.not_move_activity, R.anim.leftout_activity);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.initializeAlarm();

        final MyAdapter myAdapter = new MyAdapter(this, alarmList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick");
                //Toast.makeText(getApplicationContext(), "onItemClick", Toast.LENGTH_SHORT).show();

                SampleData data = myAdapter.getItem(position);

                int Id = data.getId();

                int Hour = data.getHour();
                int Minute = data.getMinute();

                boolean [] isDayOn = data.getIsDayOn();

                int SUN = isDayOn[0] ? 1 : 0;
                int MON = isDayOn[1] ? 1 : 0;
                int TUE = isDayOn[2] ? 1 : 0;
                int WED = isDayOn[3] ? 1 : 0;
                int THUR = isDayOn[4] ? 1 : 0;
                int FRI = isDayOn[5] ? 1 : 0;
                int SAT = isDayOn[6] ? 1 : 0;

                Intent intent = new Intent(BusAlarm.this, TimeActivity.class);
                intent.putExtra("Src", "modify");
                intent.putExtra("Id", Id);
                intent.putExtra("Hour", Hour);
                intent.putExtra("Minute", Minute);
                intent.putExtra("SUN", SUN);
                intent.putExtra("MON", MON);
                intent.putExtra("TUE", TUE);
                intent.putExtra("WED", WED);
                intent.putExtra("THUR", THUR);
                intent.putExtra("FRI", FRI);
                intent.putExtra("SAT", SAT);

                startActivity(intent);
                overridePendingTransition(R.anim.leftin_activity, R.anim.not_move_activity);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BusAlarm.this, "asdf", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    public void initializeAlarm() {
        alarmList = new ArrayList<SampleData>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "SELECT * FROM AlarmList";

        Cursor c = db.rawQuery(sql, null);

        while (c.moveToNext()) {
            int Id_pos = c.getColumnIndex("Id");
            int Hour_pos = c.getColumnIndex("Hour");
            int Minute_pos = c.getColumnIndex("Minute");
            int SUN_pos = c.getColumnIndex("SUN");
            int MON_pos = c.getColumnIndex("MON");
            int TUE_pos = c.getColumnIndex("TUE");
            int WED_pos = c.getColumnIndex("WED");
            int THUR_pos = c.getColumnIndex("THUR");
            int FRI_pos = c.getColumnIndex("FRI");
            int SAT_pos = c.getColumnIndex("SAT");
            int isON_pos = c.getColumnIndex("isON");

            int Id = c.getInt(Id_pos);
            int Hour = c.getInt(Hour_pos);
            int Minute = c.getInt(Minute_pos);
            int SUN = c.getInt(SUN_pos);
            int MON = c.getInt(MON_pos);
            int TUE = c.getInt(TUE_pos);
            int WED = c.getInt(WED_pos);
            int THUR = c.getInt(THUR_pos);
            int FRI = c.getInt(FRI_pos);
            int SAT = c.getInt(SAT_pos);
            int isON = c.getInt(isON_pos);

            alarmList.add(new SampleData(Id, Hour, Minute, isON != 0,
                    SUN != 0, MON != 0, TUE != 0, WED != 0, THUR != 0, FRI != 0, SAT != 0));
        }
    }
}

