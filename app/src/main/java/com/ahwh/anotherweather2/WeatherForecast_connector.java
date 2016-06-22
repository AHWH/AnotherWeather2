package com.ahwh.anotherweather2;

import com.ahwh.anotherweather2.weatherModel.MainWeather_model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by weiho on 16/5/2016.
 */
public interface WeatherForecast_connector {
    @GET("{apikey}/{coordinates}")
    Call<MainWeather_model> getForecastIOWeather(@Path("apikey") String apikey, @Path("coordinates") String coordinates, @Query("units") String unit, @Query("exclude") String exclusion);
}
