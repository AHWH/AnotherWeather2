package com.ahwh.anotherweather2.weatherModel;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by weiho on 17/6/2016.
 */

@JsonObject
public class HourlyWeatherWrapper_model extends RealmObject {

    @Ignore
    @JsonField
    List<HourlyData_model> data;

    public RealmList<HourlyData_model> HourlyData_RL;

    public void setHourlyData(List<HourlyData_model> hourlyData) {
        this.data = hourlyData;
    }
    public List<HourlyData_model> getHourlyData() {
        return data;
    }

    public void setHourlyData_RL(RealmList<HourlyData_model> hourlyData_RL) {
        HourlyData_RL = hourlyData_RL;
    }
    public RealmList<HourlyData_model> getHourlyData_RL() {
        return HourlyData_RL;
    }
}
