package com.ahwh.anotherweather2;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahwh.anotherweather2.weatherModel.MainWeather_model;

/**
 * Created by weiho on 12/5/2016.
 */
public class WeatherFragment extends Fragment {

    TextView condition_main;
    TextView temp_main;
    TextView temp_unit;
    TextView tempFeel_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Views set over here to ensure that views are always associated as early as possible
        //Prevent crashes due to runtime changes
        condition_main = (TextView) view.findViewById(R.id.Condition_tv);
        temp_main = (TextView) view.findViewById(R.id.Temp_tv);
        temp_unit = (TextView) view.findViewById(R.id.TempTv_companion2);
        tempFeel_main = (TextView) view.findViewById(R.id.FeelTemp_tv);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void updateUI(MainWeather_model forecastData) {
        condition_main.setText(forecastData.getCurrentWeather().getSummary());

        String mainTemp = Helper.temperatureConverter(forecastData.getCurrentWeather().getTemperature());
        temp_main.setText(mainTemp);
        SharedPreferences preferencesFile = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitPref = preferencesFile.getString("temp_unit", "");
        if (unitPref.equals("Celsius")) {
            temp_unit.setText("C");
        } else {
            temp_unit.setText("F");
        }

        String feelTemp = Helper.temperatureConverter(forecastData.getCurrentWeather().getApparentTemperature());
        String feelTempFull = "Feels like: " + feelTemp + "°";
        tempFeel_main.setText(feelTempFull);

    }
}
