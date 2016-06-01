package com.ahwh.anotherweather2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahwh.anotherweather2.database.ForecastMain_db;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

/**
 * Created by weiho on 31/5/2016.
 */
public class WeatherInfo_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get data from SQLdatabase
        FlowManager.init(new FlowConfig.Builder(getContext()).build());
        return inflater.inflate(R.layout.moreinfo_overlay, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Not going to live-stream data in because fragment won't be in memory
        //Fragment will instead receive values from SQLdatabase directly --> operation is quick enough to look instant
        SQLite.select().from(ForecastMain_db.class).async().queryResultCallback(new QueryTransaction.QueryResultCallback<ForecastMain_db>() {
            @Override
            public void onQueryResult(QueryTransaction transaction, @NonNull CursorResult<ForecastMain_db> tResult) {
                if (tResult.count() > 0) {
                    ForecastMain_db forecastMain_db = tResult.toListClose().get(0);

                    TextView humidity = (TextView) view.findViewById(R.id.humidityValue_tv);
                    TextView visiblity = (TextView) view.findViewById(R.id.visibilityValue_tv);
                    TextView pressure = (TextView) view.findViewById(R.id.pressureValue_tv);
                    TextView windSpeed = (TextView) view.findViewById(R.id.windSpeedValue_tv);

                    String humidityStr = Double.toString(forecastMain_db.getCurrently().getHumidity());
                    humidity.setText(humidityStr);
                    String visibilityStr = Double.toString(forecastMain_db.getCurrently().getVisibility());
                    visiblity.setText(visibilityStr);
                    String pressureStr = Double.toString(forecastMain_db.getCurrently().getPressure());
                    pressure.setText(pressureStr);
                    String windSpeedStr = Double.toString(forecastMain_db.getCurrently().getWindSpeed());
                    windSpeed.setText(windSpeedStr);
                }
            }
        }).execute();
    }
}
