package com.ahwh.anotherweather2.weatherModel;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by weiho on 18/6/2016.
 */
@JsonObject
public class DailyData_model extends RealmObject {
    @PrimaryKey
    private int id;

    @JsonField
    private long time;
    @JsonField
    private String summary;
    @JsonField
    private String icon;
    @JsonField
    private double temperatureMin;
    @JsonField
    private double temperatureMax;
    @JsonField
    private double humidity;
    @JsonField
    private double windSpeed;
    @JsonField
    private double visibility;
    @JsonField
    private double pressure;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public void setSummary(String weatherCondition) {
        this.summary = weatherCondition;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public void setTemperatureMin(double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }
    public void setTemperatureMax(double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public long getTime() {
        return time;
    }
    public String getSummary() {
        return summary;
    }
    public String getIcon() {
        return icon;
    }
    public double getTemperatureMin() {
        return temperatureMin;
    }
    public double getTemperatureMax() {
        return temperatureMax;
    }
    public double getHumidity() {
        return humidity;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public double getVisibility() {
        return visibility;
    }
    public double getPressure() {
        return pressure;
    }
}
