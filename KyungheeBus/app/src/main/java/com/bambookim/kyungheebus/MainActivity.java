package com.bambookim.kyungheebus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.textView);
    }

     public void btnMethod(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = getMenuInflater();

        switch (view.getId()) {
            case R.id.btn_stationList:
                inflater.inflate(R.menu.menu_bus, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(listener1);
                break;
            case R.id.btn_toIn:
                inflater.inflate(R.menu.menu_station_to_in, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(listener2);
                break;
            case R.id.btn_toOut:
                inflater.inflate(R.menu.menu_station_to_out, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(listener2);
                break;
        }

        popupMenu.show();
    }

    PopupMenu.OnMenuItemClickListener listener1 = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String routeId = "";

            switch (item.getItemId()) {
                case R.id.item_M5107:
                    routeId = "234001243";
                    break;
                case R.id.item_1112:
                    routeId = "234000016";
                    break;
                case R.id.item_5100:
                    routeId = "200000115";
                    break;
                case R.id.item_7000:
                    routeId = "200000112";
                    break;
                case R.id.item_9:
                    routeId = "200000103";
                    break;
            }

            StationListTask task = new StationListTask(MainActivity.this, text1);
            task.execute(routeId);

            return false;
        }
    };

    PopupMenu.OnMenuItemClickListener listener2 = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String stationId = "";

            switch (item.getItemId()) {
                case R.id.stn_in_entrance:

                    break;
                case R.id.stn_in_foreign:
                    stationId = "228000710";
                    break;
                case R.id.stn_in_life:

                    break;
                case R.id.stn_in_plaza:

                    break;
                case R.id.stn_out_life:

                    break;
                case R.id.stn_out_phy:

                    break;
                case R.id.stn_out_khu:

                    break;
            }

            StationArrivalTask arrivalTask = new StationArrivalTask(MainActivity.this, text1);
            arrivalTask.execute(stationId);

            return false;
        }
    };
}