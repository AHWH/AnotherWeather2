package com.ahwh.anotherweather2.weatherModel;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by weiho on 17/6/2016.
 */

@JsonObject
public class MainWeather_model extends RealmObject {

    @PrimaryKey
    private int id;

    @JsonField
    private String latitude;
    @JsonField
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

    @JsonField
    public CurrentWeather_model currently;
    @JsonField
    public HourlyWeatherWrapper_model hourly;
    @JsonField
    public DailyWeatherWrapper_model daily;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setCurrently(CurrentWeather_model currently) {
        this.currently = currently;
    }
    public void setHourly(HourlyWeatherWrapper_model hourly) {
        this.hourly = hourly;
    }
    public void setDaily(DailyWeatherWrapper_model daily) {
        this.daily = daily;
    }

    public CurrentWeather_model getCurrentWeather() {
        return currently;
    }
    public HourlyWeatherWrapper_model getHourly() {
        return hourly;
    }
    public DailyWeatherWrapper_model getDaily() {
        return daily;
    }
}
