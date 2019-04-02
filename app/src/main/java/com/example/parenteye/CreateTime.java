package com.example.parenteye;

import android.provider.ContactsContract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTime {

    String date1 ;

    Date dateObj1,dateObj2;
    getCurrentTime curr= new getCurrentTime();

    String date2 =curr.getDateTime();

    String format = "MM/dd/yyyy HH:mm";


    public CreateTime(String ago)  {
        this.date1=ago;
    }


    public void sdf() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

         dateObj1= sdf.parse(date1);
         dateObj2= sdf.parse(date2);


    }



    // calculate time
    public String calculateTime(){

        DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

        // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
        long diff = dateObj2.getTime() - dateObj1.getTime();


        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        //System.out.println("difference between days: " + diffDays);

        int diffhours = (int) (diff / (60 * 60 * 1000));
        //System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

        int diffmin = (int) (diff / (60 * 1000));
        //System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

        int diffsec = (int) (diff / (1000));

        String sec=crunchifyFormatter.format(diffsec);
        String min=crunchifyFormatter.format(diffmin);
        String hr=crunchifyFormatter.format(diffhours);
        String day=crunchifyFormatter.format(diffDays);

        NumberFormat format=NumberFormat.getInstance( Locale.UK);
        try {
            Number number_sec = format.parse( sec );
            Number number_min = format.parse( min );
            Number number_hr = format.parse( hr);

           // System.out.println("sec "+number_sec.intValue()+" min "+number_min.intValue()+" hr "+number_hr.intValue());

           if (number_sec.intValue()<60){
                return number_sec.intValue()+" s";
            }
            else if (number_min.intValue()<60){
                return number_min.intValue()+" min";
            }
            else if (number_hr.intValue()<24){
                return number_hr.intValue() +" hr";
            }
            else if (Integer.parseInt( day )<7) {
                return day+" day";
            }
            else if (Integer.parseInt(day)%7==0){
                return Integer.parseInt(day)/7+" w";
            }

            else if (Integer.parseInt(day)%30==0){
                return Integer.parseInt(day)/30+" m";
            }
            else if (Integer.valueOf( day )%365==0){
                return String.valueOf( (Integer.valueOf( day )/365) )+" y" ;}
            else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObj2);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            return month+" / "+year;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

            //System.out.println("difference between milliseconds: " + crunchifyFormatter.format(diff));






}
