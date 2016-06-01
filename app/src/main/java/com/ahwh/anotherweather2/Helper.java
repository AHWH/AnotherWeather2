package com.ahwh.anotherweather2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ahwh.anotherweather2.database.CurrentWeather_db;
import com.ahwh.anotherweather2.database.DailyForecastDb_RecyclerAdapter;
import com.ahwh.anotherweather2.database.DailyWeather_db;
import com.ahwh.anotherweather2.database.ForecastMain_db;
import com.ahwh.anotherweather2.database.HourlyForecastDb_RecyclerAdapter;
import com.ahwh.anotherweather2.database.HourlyWeather_db;
import com.ahwh.anotherweather2.database.WeatherDatabase;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by weiho on 13/5/2016.
 */
public class Helper {

    //Constructor class to pass context to function in helper
    Context context;
    public Helper(Context context) {
        this.context = context;
    }

    public static String dateConverter(long time) {
        GregorianCalendar calendar = new GregorianCalendar();
        TimeZone timezone = calendar.getTimeZone();
        long offset = timezone.getOffset(time);
        long localisedTime = time;
        Date dateObj = new Date(localisedTime*1000);
        SimpleDateFormat desireFormat = new SimpleDateFormat("dd E, ha");
        return desireFormat.format(dateObj);
    }

    public static String temperatureConverter(double temp) {
        long roundedTemp = Math.round(temp);
        return Long.toString(roundedTemp);
    }

    public void updateView(WeatherForecast_Model forecastModelObj) {
        //Creating WeatherForecast object from response pass by Retrofit in MainActivity and seperating out DailyForecast's array
        List<WeatherForecast_Model.DailyData> dailyForecastArray = forecastModelObj.getDaily().getData();
        List<WeatherForecast_Model.CurrentWeather> hourlyForecastArray = forecastModelObj.getHourly().getData();

        //Listing relevant views and preparing ArrayAdapter
        TextView condition_main = (TextView) ((Activity)context).findViewById(R.id.Condition_tv);
        TextView temp_main = (TextView) ((Activity)context).findViewById(R.id.Temp_tv);
        TextView temp_unit = (TextView) ((Activity)context).findViewById(R.id.TempTv_companion2) ;
        TextView tempFeel_main = (TextView) ((Activity)context).findViewById(R.id.FeelTemp_tv);
        RecyclerView hourlyForecast_rv = (RecyclerView) ((Activity)context).findViewById(R.id.HourlyForecast_rv);
        HourlyForecast_RecyclerAdapter adapter = new HourlyForecast_RecyclerAdapter(hourlyForecastArray);
        RecyclerView dailyForecast_rv = (RecyclerView) ((Activity)context).findViewById(R.id.DailyForecast_rv);
        DailyForecast_RecyclerAdapter arrayAdapter = new DailyForecast_RecyclerAdapter(dailyForecastArray);

        //Setting top fragment's weather condition
        condition_main.setText(forecastModelObj.getCurrentWeather().getSummary());

        //Setting main Temperature by going through a converter to round and convert from Double to String
        String mainTemp = Helper.temperatureConverter(forecastModelObj.getCurrentWeather().getTemperature());
        temp_main.setText(mainTemp);
        SharedPreferences preferencesFile = PreferenceManager.getDefaultSharedPreferences(context);
        String unitPref = preferencesFile.getString("temp_unit", "");
        if (unitPref.equals("Celsius")) {
            temp_unit.setText("C");
        } else {
            temp_unit.setText("F");
        }

        //Setting feel Temperature by going through a converter to round and convert from Double to String
        String feelTemp = Helper.temperatureConverter(forecastModelObj.getCurrentWeather().getApparentTemperature());
        String feelTempFull = "Feels like: " + feelTemp + "°";
        tempFeel_main.setText(feelTempFull);

        //Populating hourly's recyclerview
        hourlyForecast_rv.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyForecast_rv.setLayoutManager(linearLayoutManager);

        //Populating daily's recyclerview
        dailyForecast_rv.setAdapter(arrayAdapter);
        dailyForecast_rv.setLayoutManager(new LinearLayoutManager(context));
        dailyForecast_rv.setHasFixedSize(true);

    }

    public void saveData(final WeatherForecast_Model forecastModelObj) {
        DatabaseDefinition database = FlowManager.getDatabase(WeatherDatabase.class);
        Transaction transaction = database.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ForecastMain_db forecastMain_db = new ForecastMain_db();
                forecastMain_db.id = 1;
                forecastMain_db.setLatitude(forecastModelObj.getLatitude());
                forecastMain_db.setLongitude(forecastModelObj.getLongitude());

                CurrentWeather_db currentWeather_db = new CurrentWeather_db();
                currentWeather_db.id = 1;
                currentWeather_db.setTime(forecastModelObj.getCurrentWeather().getTime());
                currentWeather_db.setSummary(forecastModelObj.getCurrentWeather().getSummary());
                currentWeather_db.setIcon(forecastModelObj.getCurrentWeather().getIcon());
                currentWeather_db.setTemperature(forecastModelObj.getCurrentWeather().getTemperature());
                currentWeather_db.setApparentTemperature(forecastModelObj.getCurrentWeather().getApparentTemperature());
                currentWeather_db.setHumidity(forecastModelObj.getCurrentWeather().getHumidity());
                currentWeather_db.setWindSpeed(forecastModelObj.getCurrentWeather().getWindSpeed());
                currentWeather_db.setVisibility(forecastModelObj.getCurrentWeather().getVisibility());
                currentWeather_db.setPressure(forecastModelObj.getCurrentWeather().getPressure());
                currentWeather_db.save();
                forecastMain_db.setCurrently(currentWeather_db);

                HourlyWeather_db hourlyWeather_db = null;
                List<WeatherForecast_Model.CurrentWeather> hourlyForecastArray = forecastModelObj.getHourly().getData();
                int i = 0;
                for(WeatherForecast_Model.CurrentWeather hourlyData : hourlyForecastArray) {
                    hourlyWeather_db = new HourlyWeather_db();
                    hourlyWeather_db.id = i++;
                    hourlyWeather_db.setTime(hourlyData.getTime());
                    hourlyWeather_db.setSummary(hourlyData.getSummary());
                    hourlyWeather_db.setIcon(hourlyData.getIcon());
                    hourlyWeather_db.setTemperature(hourlyData.getTemperature());
                    hourlyWeather_db.setApparentTemperature(hourlyData.getApparentTemperature());
                    hourlyWeather_db.setHumidity(hourlyData.getHumidity());
                    hourlyWeather_db.setWindSpeed(hourlyData.getWindSpeed());
                    hourlyWeather_db.setVisibility(hourlyData.getVisibility());
                    hourlyWeather_db.setPressure(hourlyData.getPressure());
                    hourlyWeather_db.save();
                }
                forecastMain_db.setHourlyobj(hourlyWeather_db);

                DailyWeather_db dailyWeather_db = null;
                List<WeatherForecast_Model.DailyData> dailyForecastArray = forecastModelObj.getDaily().getData();
                int j = 0;
                for(WeatherForecast_Model.DailyData dailyData : dailyForecastArray) {
                    dailyWeather_db = new DailyWeather_db();
                    dailyWeather_db.id = j++;
                    dailyWeather_db.setTime(dailyData.getTime());
                    dailyWeather_db.setSummary(dailyData.getSummary());
                    dailyWeather_db.setIcon(dailyData.getIcon());
                    dailyWeather_db.setTemperatureMax(dailyData.getTemperatureMax());
                    dailyWeather_db.setTemperatureMin(dailyData.getTemperatureMin());
                    dailyWeather_db.setHumidity(dailyData.getHumidity());
                    dailyWeather_db.setWindSpeed(dailyData.getWindSpeed());
                    dailyWeather_db.setVisibility(dailyData.getVisibility());
                    dailyWeather_db.setPressure(dailyData.getPressure());
                    dailyWeather_db.save();
                }
                forecastMain_db.setDailyobj(dailyWeather_db);

                forecastMain_db.save();
            }
        }).build();
        transaction.execute();
        Toast.makeText(context, "Data saved!", Toast.LENGTH_SHORT).show();
    }

    public void retrieveData() {
        SQLite.select().from(ForecastMain_db.class).async().queryResultCallback(new QueryTransaction.QueryResultCallback<ForecastMain_db>() {
            @Override
            public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<ForecastMain_db> tResult) {
                if (tResult.toList().size() != 0) {
                    ForecastMain_db forecastData = tResult.toListClose().get(0);
                    List<HourlyWeather_db> hourlyForecastArray = forecastData.getHourly();
                    List<DailyWeather_db> dailyForecastArray = forecastData.getDaily();

                    TextView condition_main = (TextView) ((Activity)context).findViewById(R.id.Condition_tv);
                    TextView temp_main = (TextView) ((Activity)context).findViewById(R.id.Temp_tv);
                    TextView temp_unit = (TextView) ((Activity)context).findViewById(R.id.TempTv_companion2);
                    TextView tempFeel_main = (TextView) ((Activity)context).findViewById(R.id.FeelTemp_tv);

                    RecyclerView hourlyForecast_rv = (RecyclerView) ((Activity)context).findViewById(R.id.HourlyForecast_rv);
                    HourlyForecastDb_RecyclerAdapter hourlyAdapter = new HourlyForecastDb_RecyclerAdapter(hourlyForecastArray);
                    RecyclerView dailyForecast_rv = (RecyclerView) ((Activity)context).findViewById(R.id.DailyForecast_rv);
                    DailyForecastDb_RecyclerAdapter arrayAdapter = new DailyForecastDb_RecyclerAdapter(dailyForecastArray);

                    condition_main.setText(forecastData.getCurrently().getSummary());

                    String mainTemp = Helper.temperatureConverter(forecastData.getCurrently().getTemperature());
                    temp_main.setText(mainTemp);
                    SharedPreferences preferencesFile = PreferenceManager.getDefaultSharedPreferences(context);
                    String unitPref = preferencesFile.getString("temp_unit", "");
                    Log.i("", unitPref);
                    Toast.makeText(context, unitPref, Toast.LENGTH_SHORT).show();
                    if (unitPref.equals("Celsius")) {
                        temp_unit.setText("C");
                    } else {
                        temp_unit.setText("F");
                    }

                    String feelTemp = Helper.temperatureConverter(forecastData.getCurrently().getApparentTemperature());
                    String feelTempFull = "Feels like: " + feelTemp + "°";
                    tempFeel_main.setText(feelTempFull);

                    hourlyForecast_rv.setAdapter(hourlyAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    hourlyForecast_rv.setLayoutManager(linearLayoutManager);

                    dailyForecast_rv.setAdapter(arrayAdapter);
                    dailyForecast_rv.setLayoutManager(new LinearLayoutManager(context));
                    dailyForecast_rv.setHasFixedSize(true);
                }
            }
        }).error(new Transaction.Error() {
            @Override
            public void onError(Transaction transaction, Throwable error) {
                Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }

}
