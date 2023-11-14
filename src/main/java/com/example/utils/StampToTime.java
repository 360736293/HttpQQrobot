package com.example.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StampToTime {
    public static String get(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = Long.parseLong(s);
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return res;
    }
}
