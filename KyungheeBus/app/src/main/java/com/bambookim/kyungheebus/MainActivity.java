package com.bambookim.kyungheebus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
        String routeId = "";

        switch (view.getId()) {
            case R.id.btn_M5107:
                routeId = "234001243";
                break;
            case R.id.btn_1112:
                routeId = "234000016";
                break;
            case R.id.btn_5100:
                routeId = "200000115";
                break;
            case R.id.btn_7000:
                routeId = "200000112";
                break;
            case R.id.btn_9:
                routeId = "200000103";
                break;
        }

        StationTask task = new StationTask(MainActivity.this, text1);
        task.execute(routeId);
    }
}
