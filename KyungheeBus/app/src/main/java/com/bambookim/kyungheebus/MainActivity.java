package com.bambookim.kyungheebus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView text1;
    Button goToStationArrival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.textView);
        goToStationArrival = (Button) findViewById(R.id.goToStationArrival);

        goToStationArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StationArrivalActivity.class);
                startActivity(intent);
            }
        });
    }

     public void btnMethod(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = getMenuInflater();

        switch (view.getId()) {
            case R.id.btn_stationList:
                inflater.inflate(R.menu.menu_bus, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(listener1);
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
}