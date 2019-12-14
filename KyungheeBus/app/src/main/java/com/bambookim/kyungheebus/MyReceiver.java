package com.bambookim.kyungheebus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        /*
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 사용하고자 하는 코드
                Toast.makeText(context, "반복 알림",Toast.LENGTH_SHORT).show();
                Log.d("asdf", "반복 알림");
            }
        }, 0);

         */

        Toast.makeText(context, "반복 알림",Toast.LENGTH_SHORT).show();
        Log.d("asdf", "반복 알림");
    }
}
