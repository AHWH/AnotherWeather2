package com.ahwh.anotherweather2;

import android.content.Context;
import android.util.Log;

import com.ahwh.anotherweather2.weatherModel.MainWeather_model;
import com.ahwh.anotherweather2.database.DatabaseAccess;
import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by weiho on 17/6/2016.
 */
public class ForecastService_Retrofit {

    private Context context;

    private String baseURL;
    private String apiKey;
    private String coordinates;
    private String units;
    private String excludedData;

    public ForecastService_Retrofit(Context context, String base, String key, String coords, String unit, String exclusion) {
        this.context = context;

        this.baseURL = base;
        this.apiKey = key;
        this.coordinates = coords;
        this.units = unit;
        this.excludedData = exclusion;
    }

    public void connectToAPI() {
        //Building retrofit instance
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .build();

        //Intercepting said created retrofit's instance
        WeatherForecast_connector connector = retrofit.create(WeatherForecast_connector.class);

        //Registering call with created instance
        Call<MainWeather_model> call = connector.getForecastIOWeather(apiKey, coordinates, units, excludedData);

        //Make call to Forecast Service's API asynchronously to get JSON data
        //Automatically parsed by attached LoganSquare's parser through MainWeather_model
        call.enqueue(new Callback<MainWeather_model>() {
            @Override
            public void onResponse(Call<MainWeather_model> call, Response<MainWeather_model> response) {
                if(response.isSuccessful()) {
                    MainWeather_model intModel = response.body();
                    DatabaseAccess databaseAccess = new DatabaseAccess(context, intModel);
                    databaseAccess.writeToDB();
                }
            }

            @Override
            public void onFailure(Call<MainWeather_model> call, Throwable t) {
                Log.e("AnotherWeather2", "Retrofit failed to connect to API. " + t);
            }
        });

    }
}