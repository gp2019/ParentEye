package com.example.parenteye;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateTime {

    int s;
    String date1 ;

    Date dateObj1,dateObj2;
    getCurrentTime curr= new getCurrentTime();

    String date2 =curr.getDateTime();

    String format = "MM/dd/yyyy HH:mm";


    public CreateTime(String ago) throws ParseException {
        this.date1=ago;
    }


    public void sdf() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(format);

         dateObj1= sdf.parse(date1);
         dateObj2= sdf.parse(date2);

        System.out.println("_______________________________");
        System.out.println( dateObj1+"____________"+dateObj2 );
        System.out.println("_______________________________");


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
            Number number = format.parse( sec );
          s=number.intValue();
            System.out.println("_______________________________");
            System.out.println("the sec is "+number.intValue() );
            System.out.println("_______________________________");
        } catch (ParseException e) {
            e.printStackTrace();
        }





        System.out.println("_______________________________");
        System.out.println("the sec is "+s/60);
        System.out.println("_______________________________");

        if (Integer.parseInt( day )==1){return day;}
        else if (Integer.valueOf( day )%365==0){
            return String.valueOf( (Integer.valueOf( day )/365) ) ;}
        else{return date2;}
    }


    //System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

            //System.out.println("difference between milliseconds: " + crunchifyFormatter.format(diff));






}
