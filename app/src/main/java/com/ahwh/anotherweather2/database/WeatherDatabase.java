package com.ahwh.anotherweather2.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by weiho on 13/5/2016.
 */
@Database(name = WeatherDatabase.NAME, version = WeatherDatabase.VERSION)
public class WeatherDatabase {
    public static final String NAME = "WeatherDatabase";
    public static final int VERSION = 2;
}
