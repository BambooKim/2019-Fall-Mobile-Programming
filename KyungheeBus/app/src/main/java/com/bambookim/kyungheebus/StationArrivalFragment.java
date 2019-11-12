package com.bambookim.kyungheebus;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class StationArrivalFragment extends Fragment {

    private static final String TAG = "StationArrivalFragment";

    private TextView stationName;
    private TextView arrival_M5107;
    private TextView arrival_1112;
    private TextView arrival_5100;
    private TextView arrival_7000;
    private TextView arrival_9;
    private TextView direction;

    private String stationId;

    private Button before, next;

    LinkedHashMap<String, String> stationMap;

    public StationArrivalFragment(String stationId) {
        this.stationId = stationId;

        stationMap = new LinkedHashMap<>();
        stationMap.put("228000723", "경희대정문");
        stationMap.put("228000710", "외국어대학");
        stationMap.put("228000709", "생명과학대");
        stationMap.put("228000708", "사색의광장");
        stationMap.put("228001174", "사색의광장");
        stationMap.put("228000704", "생명과학대.산업대학");
        stationMap.put("228000703", "경희대체육대학.외대");
        stationMap.put("203000125", "경희대학교");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //StationArrivalTask task = new StationArrivalTask(StationArrivalActivity.class, );

        View view = inflater.inflate(R.layout.fragment_station_arrival, container, false);

        stationName = (TextView) view.findViewById(R.id.stationName);
        arrival_M5107 = (TextView) view.findViewById(R.id.listArrival_M5107);
        arrival_1112 = (TextView) view.findViewById(R.id.listArrival_1112);
        arrival_5100 = (TextView) view.findViewById(R.id.listArrival_5100);
        arrival_7000 = (TextView) view.findViewById(R.id.listArrival_7000);
        arrival_9 = (TextView) view.findViewById(R.id.listArrival_9);
        direction = (TextView) view.findViewById(R.id.direction);

        before = (Button) view.findViewById(R.id.before);
        next = (Button) view.findViewById(R.id.next);

        stationName.setText(stationMap.get(stationId));

        if (StationArrivalTask2.arrayPtr != 3 && StationArrivalTask2.arrayPtr != 7) {
            direction.setText(stationMap.get(StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr + 1]) + " 방면");
        }

        startTask(stationId);

        Button button = view.findViewById(R.id.arrival_refresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTask(stationId);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StationArrivalTask2.arrayPtr < 7) {
                    StationArrivalTask2.arrayPtr++;

                    stationId = StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr];
                    stationName.setText(stationMap.get(stationId));

                    if (StationArrivalTask2.arrayPtr != 3 && StationArrivalTask2.arrayPtr != 7) {
                        direction.setText(stationMap.get(StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr + 1]) + " 방면");
                    } else {
                        direction.setText("");
                    }

                    startTask(stationId);
                } else {

                }
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StationArrivalTask2.arrayPtr > 0) {
                    StationArrivalTask2.arrayPtr--;

                    stationId = StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr];
                    stationName.setText(stationMap.get(stationId));

                    if (StationArrivalTask2.arrayPtr != 3 && StationArrivalTask2.arrayPtr != 7) {
                        direction.setText(stationMap.get(StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr + 1]) + " 방면");
                    } else {
                        direction.setText("");
                    }

                    startTask(stationId);
                } else {

                }
            }
        });

        stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();

                inflater.inflate(R.menu.menu_station, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(listener);

                popupMenu.show();
            }

            PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.stn_in_entrance:
                            StationArrivalTask2.arrayPtr = 0;
                            break;
                        case R.id.stn_in_foreign:
                            StationArrivalTask2.arrayPtr = 1;
                            break;
                        case R.id.stn_in_life:
                            StationArrivalTask2.arrayPtr = 2;
                            break;
                        case R.id.stn_in_plaza:
                            StationArrivalTask2.arrayPtr = 3;
                            break;
                        case R.id.stn_out_plaza:
                            StationArrivalTask2.arrayPtr = 4;
                            break;
                        case R.id.stn_out_life:
                            StationArrivalTask2.arrayPtr = 5;
                            break;
                        case R.id.stn_out_phy:
                            StationArrivalTask2.arrayPtr = 6;
                            break;
                        case R.id.stn_out_khu:
                            StationArrivalTask2.arrayPtr = 7;
                            break;
                    }
                    stationId = StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr];
                    stationName.setText(stationMap.get(stationId));

                    if (StationArrivalTask2.arrayPtr != 3 && StationArrivalTask2.arrayPtr != 7) {
                        direction.setText(stationMap.get(StationArrivalTask2.stationList[StationArrivalTask2.arrayPtr + 1]) + " 방면");
                    } else {
                        direction.setText("");
                    }

                    startTask(stationId);

                    return false;
                }
            };
        });

        return view;
    }

    public void startTask(String stnId) {
        new StationArrivalTask2(getActivity(), arrival_M5107).execute(stnId, "234001243");
        new StationArrivalTask2(getActivity(), arrival_1112).execute(stnId, "234000016");
        new StationArrivalTask2(getActivity(), arrival_5100).execute(stnId, "200000115");
        new StationArrivalTask2(getActivity(), arrival_7000).execute(stnId, "200000112");
        new StationArrivalTask2(getActivity(), arrival_9).execute(stnId, "200000103");
    }
}
