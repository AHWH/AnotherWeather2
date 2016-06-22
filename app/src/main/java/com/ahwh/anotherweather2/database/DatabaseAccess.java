package com.ahwh.anotherweather2.database;

import android.content.Context;

import com.ahwh.anotherweather2.MainActivity;
import com.ahwh.anotherweather2.weatherModel.DailyData_model;
import com.ahwh.anotherweather2.weatherModel.DailyWeatherWrapper_model;
import com.ahwh.anotherweather2.weatherModel.HourlyData_model;
import com.ahwh.anotherweather2.weatherModel.HourlyWeatherWrapper_model;
import com.ahwh.anotherweather2.weatherModel.MainWeather_model;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by weiho on 18/6/2016.
 */
public class DatabaseAccess {

    private Context context;

    private MainWeather_model int$Model;

    private Realm realm;
    private RealmResults<MainWeather_model> results;

    //Retrieve context and WeatherModel object
    public DatabaseAccess(Context context, MainWeather_model model) {
        this.context = context;
        this.int$Model = model;
    }

    //Write to Realm's DB asynchronously
    public void writeToDB() {
        //Configuring Realm db
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //Assign id to replace data through Primary key in respective models to replace old data
                MainWeather_model final$Model = int$Model;
                final$Model.setId(0);
                final$Model.getCurrentWeather().setId(0);
                HourlyWeatherWrapper_model hourlyWrapper = realm.createObject(HourlyWeatherWrapper_model.class);
                for (int i = 0; i < final$Model.getHourly().getHourlyData().size(); i++) {
                    //Parsing the HourlyData object into RealmList
                    HourlyData_model data = final$Model.getHourly().getHourlyData().get(i);
                    data.setId(i);
                    hourlyWrapper.HourlyData_RL.add(data);
                }

                //The same for Daily Weather
                DailyWeatherWrapper_model dailyWrapper = realm.createObject(DailyWeatherWrapper_model.class);
                for (int i = 0; i < final$Model.getDaily().getDailyData().size(); i++) {
                    DailyData_model data = final$Model.getDaily().getDailyData().get(i);
                    data.setId(i);
                    dailyWrapper.DailyData_RL.add(data);
                }
                final$Model.setHourly(hourlyWrapper);
                final$Model.setDaily(dailyWrapper);
                realm.copyToRealmOrUpdate(final$Model);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getFromDB();
            }
        });
    }

    //Retrieve data from Realm's database asynchronously
    public void getFromDB() {
        realm = Realm.getDefaultInstance();
        if(!realm.isEmpty()) {
            results = realm.where(MainWeather_model.class).findAllAsync();
            results.addChangeListener(callBack);
        }
    }

    //Callback when the retrieval is done
    private RealmChangeListener<RealmResults<MainWeather_model>> callBack = new RealmChangeListener<RealmResults<MainWeather_model>>() {
        @Override
        public void onChange(RealmResults<MainWeather_model> element) {
            results.removeChangeListeners();
            MainWeather_model transitionModel = element.get(0);
            ((MainActivity)context).dataSplitter(transitionModel);
        }
    };
}
