package com.bambookim.kyungheebus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class StationArrivalActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private StationArrivalFragment fragment;
    private FragmentTransaction transaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_arrival);

        fragmentManager = getSupportFragmentManager();

        fragment = new StationArrivalFragment(StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr]);

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.arrival_framelayout, fragment).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stationarrival, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionBtnAlarm:
                Intent intent = new Intent(getApplicationContext(), BusAlarm.class);
                startActivity(intent);
                overridePendingTransition(R.anim.leftin_activity, R.anim.not_move_activity);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
