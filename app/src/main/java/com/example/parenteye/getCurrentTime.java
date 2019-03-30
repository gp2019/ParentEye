package com.example.parenteye;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class getCurrentTime {
    //the current date

    public String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return dateFormat.format( calendar.getTime() );
    }
}
