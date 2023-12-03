package com.example.topets.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStringConverter {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static Date getDateFrom(String s) throws ParseException {
        return DATE_FORMAT.parse(s);
    }
    public static String getStringFrom(Date d){
        return DATE_FORMAT.format(d);
    }
}
