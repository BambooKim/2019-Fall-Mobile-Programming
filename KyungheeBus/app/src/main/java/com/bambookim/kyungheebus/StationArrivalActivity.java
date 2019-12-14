package com.bambookim.kyungheebus;

import android.os.Bundle;

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
}
