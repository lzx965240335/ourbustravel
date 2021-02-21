package com.cykj.util;

import com.cykj.bean.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyRoot {

    /**
     * 计算两个点坐标的距离
     * @return km
     */
    public static double  getDistance(String slat1,String slng1,String slat2,String slng2){
        Double lat1 = Double.valueOf(slat1);
        Double lng1 = Double.valueOf(slng1);
        Double lat2 = Double.valueOf(slat2);
        Double lng2 = Double.valueOf(slng2);
        double lon1 = (Math.PI / 180) * lat1;
        double lon2 = (Math.PI / 180) * lat2;
        lat1 = (Math.PI / 180) * lng1;
        lat2 = (Math.PI / 180) * lng2;
        double R = 6371;//地球半径
        double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return distance;
    }


}
