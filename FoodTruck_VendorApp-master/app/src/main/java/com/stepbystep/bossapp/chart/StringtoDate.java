package com.stepbystep.bossapp.chart;

import com.stepbystep.bossapp.DO.Order_history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StringtoDate {

    private static LocalDateTime parse_date;

    public static LocalDateTime changetodata(String stringdate){

        parse_date = LocalDateTime.parse(stringdate,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return  parse_date;
    }


}
