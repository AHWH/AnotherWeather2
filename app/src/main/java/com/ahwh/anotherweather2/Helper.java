package com.ahwh.anotherweather2;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
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
        final long roundedTemp = Math.round(temp);
        return Long.toString(roundedTemp);
    }

}
