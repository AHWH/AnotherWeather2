package com.ahwh.anotherweather2;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiho on 13/5/2016.
 */
@JsonObject
public class WeatherForecast_Model {

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
    public CurrentWeather currently;
    @JsonField
    public HourlyWeather hourly;
    @JsonField
    public DailyWeather daily;

    public void setCurrently(CurrentWeather currently) {
        this.currently = currently;
    }
    public void setHourly(HourlyWeather hourly) {
        this.hourly = hourly;
    }
    public void setDaily(DailyWeather daily) {
        this.daily = daily;
    }

    public CurrentWeather getCurrentWeather() {
        return currently;
    }
    public HourlyWeather getHourly() {
        return hourly;
    }
    public DailyWeather getDaily() {
        return daily;
    }

    @JsonObject
    public static class CurrentWeather {
        @JsonField
        private long time;
        @JsonField
        private String summary;
        @JsonField
        private String icon;
        @JsonField
        private double temperature;
        @JsonField
        private double apparentTemperature;
        @JsonField
        private double humidity;
        @JsonField
        private double windSpeed;
        @JsonField
        private double visibility;
        @JsonField
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

    @JsonObject
    public static class HourlyWeather {
        @JsonField
        List<CurrentWeather> data;

        public void setData(List<CurrentWeather> data) {
            this.data = data;
        }

        public List<CurrentWeather> getData() {
            return data;
        }
    }

    @JsonObject
    public static class DailyWeather {
        @JsonField
        List<DailyData> data;

        public void setData(List<DailyData> data) {
            this.data = data;
        }
        public List<DailyData> getData() {
            return data;
        }
    }

    @JsonObject
    public static class DailyData {
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
}
