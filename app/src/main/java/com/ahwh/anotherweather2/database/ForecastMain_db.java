package com.ahwh.anotherweather2.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by weiho on 21/5/2016.
 */

@Table(database = WeatherDatabase.class)
public class ForecastMain_db extends BaseModel {

    @PrimaryKey
    public int id;

    @Column
    private String latitude;
    @Column
    private String longitude;

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }

    @ForeignKey
    public CurrentWeather_db currently;
    @ForeignKey
    public HourlyWeather_db hourlyobj;
    @ForeignKey
    public DailyWeather_db dailyobj;

    public void setCurrently(CurrentWeather_db currently) {
        this.currently = currently;
    }
    public void setHourlyobj(HourlyWeather_db hourlyobj) {
        this.hourlyobj = hourlyobj;
    }
    public void setDailyobj(DailyWeather_db dailyobj) {
        this.dailyobj = dailyobj;
    }

    List<HourlyWeather_db> hourly;
    List<DailyWeather_db> daily;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "hourly")
    public List<HourlyWeather_db> getHourly() {
        if(hourly == null || hourly.isEmpty()) {
            hourly = SQLite.select().from(HourlyWeather_db.class).queryList();
        }
        return hourly;
    }
    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "daily")
    public List<DailyWeather_db> getDaily() {
        if(daily == null || daily.isEmpty()) {
            daily = SQLite.select().from(DailyWeather_db.class).queryList();
        }
        return daily;
    }
    public CurrentWeather_db getCurrently() {
        return currently;
    }
}
