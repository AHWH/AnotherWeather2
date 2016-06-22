package com.ahwh.anotherweather2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahwh.anotherweather2.weatherModel.MainWeather_model;

/**
 * Created by weiho on 31/5/2016.
 */
public class AdditionalWeatherInfo_Fragment extends Fragment {

    TextView humidity;
    TextView visibility;
    TextView pressure;
    TextView windSpeed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.moreinfo_overlay, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        humidity = (TextView) view.findViewById(R.id.humidityValue_tv);
        visibility = (TextView) view.findViewById(R.id.visibilityValue_tv);
        pressure = (TextView) view.findViewById(R.id.pressureValue_tv);
        windSpeed = (TextView) view.findViewById(R.id.windSpeedValue_tv);
    }

    public void updateUI(MainWeather_model forecastData) {
        Log.i("Test", "Here?");
        String humidityStr = Double.toString(forecastData.getCurrentWeather().getHumidity());
        humidity.setText(humidityStr);
        String visibilityStr = Double.toString(forecastData.getCurrentWeather().getVisibility());
        visibility.setText(visibilityStr);
        String pressureStr = Double.toString(forecastData.getCurrentWeather().getPressure());
        pressure.setText(pressureStr);
        String windSpeedStr = Double.toString(forecastData.getCurrentWeather().getWindSpeed());
        windSpeed.setText(windSpeedStr);
    }
}
