package com.cykj.util;

import com.cykj.bean.BusInf;
import com.cykj.bean.Departuretime;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.BusInfMapper;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//公交行驶
public class BusTravel extends Thread {

    private SimpleDateFormat simpleFormat = new SimpleDateFormat("HH:mm");
    //公交
    private BusInfMapper busInfMapper;
    //站点
    private SiteMapper siteMapper;
    //线路
    private Route route;

    //发车时刻
    private List<Long> list = new ArrayList<>();

    int interval = 900000;//15分钟

    public BusTravel(BusInfMapper busInfMapper, SiteMapper siteMapper, Route route) {
        this.busInfMapper = busInfMapper;
        this.siteMapper = siteMapper;
        this.route = route;
        start();
    }


    @Override
    public void run() {
        init();
        while (true) {
            startTravel();
        }
    }

    //初始化
    private void init() {
        list = getTimeList(false);
    }




    //获取时间集合
    public ArrayList<Long> getTimeList(boolean condition) {
        Departuretime departuretime = route.getDeparturetime();
        String startTime = departuretime.getStartTime();//首发时间
        String endTime = departuretime.getEndTime();//末班
        Integer intervalTime = departuretime.getIntervalTime();//间隔
        //间隔毫秒值
        interval = intervalTime * 60 * 1000;
        long time = getTimeLong(startTime);
        long time1 = getTimeLong(endTime);
        long newTimeLong = getNewTimeLong();
        //发车次数
        int i = (int) ((time1 - time) / interval);
        ArrayList<Long> list = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            long l = time + j * interval;
            if (l > newTimeLong || condition) {
                list.add(l);
            }
        }
        return list;
    }



    //公交开始行驶
    public void startTravel() {
        //判断当前是否小于第一个发车时间
        if (list.size() > 0 && getNewTimeLong() < list.get(0)) {
            System.out.println("公交准备出发！" + route.getRouteName()+"id:"+route.getRouteId());
            HashMap<String, Object> map = route.getRouteTypeMap();
            map.put("busState", 17);
            map.put("isDepart", 22);
            List<BusInf> busInfs = busInfMapper.selectBusByMap(map);

            List<Integer> busIds=BusTravelTime.busIds;//已发车公交
            for (int i = 0; i < busInfs.size(); i++) {
                BusInf busInf = busInfs.get(i);
                for (int j = 0; j < busIds.size(); j++) {
                    if (busInf.getBusId().equals(busIds.get(j))){
                        busInf=null;
                        break;
                    }
                }
                if (busInf!=null){
                    busInf.setRoute(route);
                    BusTravelThread thread = new BusTravelThread(busInf, busInfMapper, siteMapper);
                    thread.start();
                    BusTravelTime.busIds.add(busInf.getBusId());
                    System.out.println("开始发车的公交：" + busInf.getCarNum());
                    break;
                }else {
                    System.out.println("当前线路站无公交！");
                }
            }

            //移除这个线路
            list.remove(0);
        }
        try {
            sleep((long) interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //计算时间，返回毫秒值
    public long getTimeLong(String time) {
        long time1 = 0;
        try {
            time1 = simpleFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time1;
    }

    //获取当前时间毫秒值
    public long getNewTimeLong() {
        long time1 = 0;
        try {
            String format = simpleFormat.format(new Date());
            time1 = simpleFormat.parse(format).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time1;
    }

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }
}
