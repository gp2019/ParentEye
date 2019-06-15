package com.example.parenteye;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class getCurrentTime {
    //the current date

    public Calendar getClendar(){
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public DateFormat getFormatTime(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return dateFormat;
    }

    public String getDateTime() {
        return getFormatTime().format( getClendar().getTime() );
    }

    public String getTimeCloseAccount(int houre) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date myDate = dateFormat.parse(getDateTime());
        Calendar cal =Calendar.getInstance();
        cal.setTime(myDate);
        cal.add(Calendar.HOUR_OF_DAY,houre);
        String time = parseTodaysDate(cal.getTime().toString());
        return time;
    }

    public static String parseTodaysDate(String time) {


        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";

        String outputPattern = "MM/dd/yyyy HH:mm";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String timeStr = null;

        try {
            date = inputFormat.parse(time);
            timeStr = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    public boolean compareTime(String curr,String ago) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date date1 = dateFormat.parse(curr);
        Date date2 = dateFormat.parse(ago);

        if (date1.compareTo(date2) > 0) {
            return true;
        }

        return false;
    }



}
