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
public class DailyWeatherWrapper_model extends RealmObject {

    @Ignore
    @JsonField
    List<DailyData_model> data;

    public RealmList<DailyData_model> DailyData_RL;

    public void setDailyData(List<DailyData_model> data) {
        this.data = data;
    }
    public List<DailyData_model> getDailyData() {
        return data;
    }

    public void setDailyData_RL(RealmList<DailyData_model> dailyData_RL) {
        this.DailyData_RL = dailyData_RL;
    }
    public RealmList<DailyData_model> getDailyData_RL() {
        return DailyData_RL;
    }
}
