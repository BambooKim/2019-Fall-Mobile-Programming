package com.bambookim.kyungheebus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Alarm.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE AlarmList(" +
                        "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Hour INTEGER NOT NULL," +
                        "Minute INTEGER NOT NULL," +
                        "SUN INTEGER NOT NULL," +
                        "MON INTEGER NOT NULL," +
                        "TUE INTEGER NOT NULL," +
                        "WED INTEGER NOT NULL," +
                        "THUR INTEGER NOT NULL," +
                        "FRI INTEGER NOT NULL," +
                        "SAT INTEGER NOT NULL," +
                        "isON INTEGER" +
                   ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
