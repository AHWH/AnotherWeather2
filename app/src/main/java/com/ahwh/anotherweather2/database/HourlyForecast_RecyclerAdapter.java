package com.ahwh.anotherweather2.database;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahwh.anotherweather2.Helper;
import com.ahwh.anotherweather2.MainActivity;
import com.ahwh.anotherweather2.R;
import com.ahwh.anotherweather2.weatherModel.HourlyData_model;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by weiho on 29/5/2016.
 *
 * How Recyclerview works:
 *
 * 1) We create the Realmrecycler layout and the specific layout file to be used within each row
 * 2) In this recycler adapter, we extends the ViewHolder class of RealmRecyclerViewAdapter
 * 3) We then create inner static ViewHolder class to hold all the views and through a constructor
 *    associate them with the views in the layout file through view passed by...
 * 4) onCreateViewHolder - it is the first cycle of the lifecycle of the adapter, here we set-up
 *    context and inflate the layout view. We then pass the view back to Viewholder (3)
 * 5) Next we call onBindViewHolder - to do what we need to and that's it
 *
 *
 */
public class HourlyForecast_RecyclerAdapter extends RealmRecyclerViewAdapter<HourlyData_model, HourlyForecast_RecyclerAdapter.ViewHolder> {

    public MainActivity mainActivity;

    //Constructor class to pass arraylist given by caller function to within adapter
    public HourlyForecast_RecyclerAdapter(MainActivity mainActivity, OrderedRealmCollection<HourlyData_model> data) {
        super(mainActivity, data, true);
        this.mainActivity = mainActivity;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        ImageView icon;
        TextView temp;
        HourlyData_model data;

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
        View view = inflater.inflate(R.layout.hourlyforecastrv_row, parent, false);
        return new ViewHolder(view);
    }

    //Once ViewHolder has been setup, proceed to do what it needs to do
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyData_model data = getData().get(position);

        //Set time
        String convertedTime = Helper.dateConverter(data.getTime());
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
}
