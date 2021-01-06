package com.cykj.util;

import com.cykj.bean.*;

import java.util.ArrayList;
import java.util.List;

public class Station {


    public static List getStation(List<Site> sites , List<Route> routes, List<City> cities){
        List busLines = new ArrayList();
        List stationInfos = new ArrayList();

        for (Route route:routes) {
            String[] ids=route.getRouteinf().split("#");
            for (Site site:sites) {
                if (site.getIsDot()==0){
                    for (int i=0;i<=ids.length;i++){
                        if (site.getSiteId().toString().equals(ids[i])){
                            //创建线路
                            Lines lines = new Lines();
                            lines.setId(route.getRouteid() + "");
                            lines.setName(route.getRoutename());
                            Double[]bms= TransMap.convertG2BMC(Double.valueOf(site.getSiteX()),Double.valueOf(site.getSiteY()));
                            lines.setLocation(new AMap(Double.valueOf(site.getSiteX()),Double.valueOf(site.getSiteY()),bms));
                            lines.setStart_stop(site.getSiteName());
                            lines.setEnd_stop(sites.get(sites.size()-1).getSiteName());

                            busLines.add(lines);

                        }else {
                            break;
                        }
                    }
                }else {
                    break;
                }
            }
        }
        for (Route route:routes) {
            String[] ids=route.getRouteinf().split("#");
            for (Site site:sites) {
                if (site.getIsDot()==0){
                    for (int i=0;i<=ids.length;i++){
                        if (site.getSiteId().toString().equals(ids[i])){
                            //创建站点
                            StationInfo stationInfo =new StationInfo();
                            stationInfo.setId(site.getSiteId()+"");
                            stationInfo.setName(site.getSiteName());
                            Double[]bms= TransMap.convertG2BMC(Double.valueOf(site.getSiteX()),Double.valueOf(site.getSiteY()));
                            stationInfo.setLocation(new AMap(Double.valueOf(site.getSiteX()),Double.valueOf(site.getSiteY()),bms));
                            stationInfo.setAdcode("350200");
                            stationInfo.setCitycode("0592");
                            stationInfo.setBuslines(busLines);
                            stationInfos.add(stationInfo);
                        }else {
                            break;
                        }
                    }
                }else {
                    break;
                }
            }
        }

        return stationInfos;
    }

}
