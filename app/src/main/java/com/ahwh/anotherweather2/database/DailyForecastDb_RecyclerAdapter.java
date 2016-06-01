package com.ahwh.anotherweather2.database;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahwh.anotherweather2.Helper;
import com.ahwh.anotherweather2.R;

import java.util.List;

/**
 * Created by weiho on 28/5/2016.
 * Refer to HourlyForecast's Recyclerview Adapter for the comments as the structure is very similar
 */
public class DailyForecastDb_RecyclerAdapter extends RecyclerView.Adapter<DailyForecastDb_RecyclerAdapter.ViewHolder> {
    List<DailyWeather_db> dailyForecastArray;

    public DailyForecastDb_RecyclerAdapter(List<DailyWeather_db> list) {
        dailyForecastArray = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView day;
        TextView tempMax;
        TextView tempMin;
        TextView condition;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.Date_tv);
            day = (TextView) itemView.findViewById(R.id.Day_tv);
            tempMax = (TextView) itemView.findViewById(R.id.MaxTemp_tv);
            tempMin = (TextView) itemView.findViewById(R.id.FeelTemp_tv);
            condition = (TextView) itemView.findViewById(R.id.WeatherCondition_tv);
            icon = (ImageView) itemView.findViewById(R.id.WeatherIcon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dailyforecastrv_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyWeather_db dailyData = dailyForecastArray.get(position);

        final String fullDateStr = Helper.dateConverter(dailyData.getTime());
        final String dateStr = fullDateStr.substring(0,2);
        final String dayStr = fullDateStr.substring(3,6);
        holder.date.setText(dateStr);
        holder.day.setText(dayStr);

        String tempMaxStr = Helper.temperatureConverter(dailyData.getTemperatureMax());
        holder.tempMax.setText(tempMaxStr);

        String tempMinStr = Helper.temperatureConverter(dailyData.getTemperatureMin());
        holder.tempMin.setText(tempMinStr);

        String weatherIcon = dailyData.getIcon();
        switch (weatherIcon) {
            case "clear-day": {
                holder.icon.setImageResource(R.drawable.icon_sunny);
                holder.condition.setText(R.string.weathercond_clear);
                break;
            }
            case "clear-night": {
                holder.icon.setImageResource(R.drawable.icon_night);
                holder.condition.setText(R.string.weathercond_clear);
                break;
            }
            case "rain": {
                holder.icon.setImageResource(R.drawable.icon_rain);
                holder.condition.setText(R.string.weathercond_rain);
                break;
            }
            case "snow": {
                holder.icon.setImageResource(R.drawable.icon_snow);
                holder.condition.setText(R.string.weathercond_snow);
                break;
            }
            case "sleet": {
                holder.icon.setImageResource(R.drawable.icon_sleet);
                holder.condition.setText(R.string.weathercond_sleet);
                break;
            }
            case "fog": {
                holder.icon.setImageResource(R.drawable.icon_fog);
                holder.condition.setText(R.string.weathercond_fog);
                break;
            }
            case "wind": {
                holder.icon.setImageResource(R.drawable.icon_windy);
                holder.condition.setText(R.string.weathercond_wind);
                break;
            }
            case "cloudy": {
                holder.icon.setImageResource(R.drawable.icon_cloudy_day);
                holder.condition.setText(R.string.weathercond_cloudy);
                break;
            }
            case "party-cloudy-day": {
                holder.icon.setImageResource(R.drawable.icon_partlycloudy_day);
                holder.condition.setText(R.string.weathercond_partcloud);
                break;
            }
            case "party-cloudy-night": {
                holder.icon.setImageResource(R.drawable.icon_partycloudy_night);
                holder.condition.setText(R.string.weathercond_partcloud);
                break;
            }
            default: {
                holder.icon.setImageResource(R.drawable.icon_sunny);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dailyForecastArray.size();
    }
}
