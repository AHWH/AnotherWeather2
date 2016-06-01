package com.ahwh.anotherweather2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by weiho on 29/5/2016.
 *
 * How Recyclerview works:
 *
 * 1) We create the recycler layout and the specific layout file to be used within each row
 * 2) In the recycler adapter, we extendes the ViewHolder class of RecyclerView.Adapter
 * 3) We then create inner static ViewHolder class to hold all the views and through a constructor
 *    associate them with the views in the layout file through view passed by...
 * 4) onCreateViewHolder - it is the first cycle of the lifecycle of the adapter, here we set-up
 *    context and inflate the layout view. We then pass the view back to Viewholder (3)
 * 5) Next we call onBindViewHolder - to do what we need to and that's it
 * 6) Remember to call getItemCount() as well
 *
 */
public class HourlyForecast_RecyclerAdapter extends RecyclerView.Adapter<HourlyForecast_RecyclerAdapter.ViewHolder> {

    public List<WeatherForecast_Model.CurrentWeather> hourlyDataArray;

    //Constructor class to pass arraylist given by caller function to within adapter
    public HourlyForecast_RecyclerAdapter(List<WeatherForecast_Model.CurrentWeather> list) {
        hourlyDataArray = list.subList(0,25);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        ImageView icon;
        TextView temp;

        //Constructor class to pass View's object to viewholder variables from onCreateViewHolder
        public ViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.forecastHour_tv);
            icon = (ImageView) itemView.findViewById(R.id.hourlyIcon_iv);
            temp = (TextView) itemView.findViewById(R.id.hourlyTemp_tv);
        }
    }

    //Get context, inflate layout and pass the view to ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.hourlyforecastrv_row, parent, false);
        return new ViewHolder(view);
    }

    //Once ViewHolder has been setup, proceed to do what it needs to do
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherForecast_Model.CurrentWeather data = hourlyDataArray.get(position);

        //Set time
        String convertedTime = Helper.dateConverter(data.getTime());
        Log.i("", convertedTime);
        String parsedTime = convertedTime.substring(8);
        holder.time.setText(parsedTime);

        //Set Temperature
        String convertedTemp = Helper.temperatureConverter(data.getTemperature());
        holder.temp.setText(convertedTemp + "°");

        //Set icon
        String weatherIcon = data.getIcon();
        switch (weatherIcon) {
            case "clear-day": {
                holder.icon.setImageResource(R.drawable.icon_sunny);
                break;
            }
            case "clear-night": {
                holder.icon.setImageResource(R.drawable.icon_night);
                break;
            }
            case "rain": {
                holder.icon.setImageResource(R.drawable.icon_rain);
                break;
            }
            case "snow": {
                holder.icon.setImageResource(R.drawable.icon_snow);
                break;
            }
            case "sleet": {
                holder.icon.setImageResource(R.drawable.icon_sleet);
                break;
            }
            case "fog": {
                holder.icon.setImageResource(R.drawable.icon_fog);
                break;
            }
            case "wind": {
                holder.icon.setImageResource(R.drawable.icon_windy);
                break;
            }
            case "cloudy": {
                holder.icon.setImageResource(R.drawable.icon_cloudy_day);
                break;
            }
            case "partly-cloudy-day": {
                holder.icon.setImageResource(R.drawable.icon_partlycloudy_day);
                break;
            }
            case "partly-cloudy-night": {
                holder.icon.setImageResource(R.drawable.icon_partycloudy_night);
                break;
            }
            default: {
                holder.icon.setImageResource(R.drawable.icon_sunny);
            }
        }
    }

    @Override
    public int getItemCount() {
        return hourlyDataArray.size();
    }
}
