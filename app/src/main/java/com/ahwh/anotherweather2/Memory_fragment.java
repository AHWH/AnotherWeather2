package com.ahwh.anotherweather2;

import android.app.Fragment;
import android.os.Bundle;

import com.ahwh.anotherweather2.weatherModel.MainWeather_model;

/**
 * Created by weiho on 20/6/2016.
 */

//A worker fragment to store data across configuration change
public class Memory_fragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private MainWeather_model mainWeather_model;

    public void setMainWeather_model(MainWeather_model mainWeather_model) {
        this.mainWeather_model = mainWeather_model;
    }

    public MainWeather_model getMainWeather_model() {
        return mainWeather_model;
    }
}
