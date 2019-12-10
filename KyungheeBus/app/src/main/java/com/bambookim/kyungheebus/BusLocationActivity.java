package com.bambookim.kyungheebus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class BusLocationActivity extends AppCompatActivity {

    TextView busLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_location);

        busLocation = (TextView) findViewById(R.id.busLocation);

        StationListTask task = new StationListTask(this, busLocation);
        task.execute("200000115");
    }
}
