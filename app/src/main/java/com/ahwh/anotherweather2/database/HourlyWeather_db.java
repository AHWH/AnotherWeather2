package com.ahwh.anotherweather2.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by weiho on 21/5/2016.
 */
@Table(database = WeatherDatabase.class)
public class HourlyWeather_db extends BaseModel{
    @PrimaryKey
    public int id;
    @Column
    private long time;
    @Column
    private String summary;
    @Column
    private String icon;
    @Column
    private double temperature;
    @Column
    private double apparentTemperature;
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
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public void setApparentTemperature(double apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
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
    public double getTemperature() {
        return temperature;
    }
    public double getApparentTemperature() {
        return apparentTemperature;
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
