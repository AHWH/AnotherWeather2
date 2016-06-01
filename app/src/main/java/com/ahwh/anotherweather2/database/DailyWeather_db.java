package com.ahwh.anotherweather2.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by weiho on 21/5/2016.
 */
@Table(database = WeatherDatabase.class)
public class DailyWeather_db extends BaseModel{
    @PrimaryKey
    public int id;
    @Column
    private long time;
    @Column
    private String summary;
    @Column
    private String icon;
    @Column
    private double temperatureMin;
    @Column
    private double temperatureMax;
    @Column
    private double humidity;
    @Column
    private double windSpeed;
    @Column
    private double visibility;
    @Column
    private double pressure;

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
