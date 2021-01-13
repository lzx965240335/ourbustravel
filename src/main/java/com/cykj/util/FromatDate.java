package com.cykj.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FromatDate {
    public static String DateFormat(String form , Date date){
        SimpleDateFormat sd = new SimpleDateFormat(form);
        return sd.format(date);
    }
}
