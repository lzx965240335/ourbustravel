package com.cykj.util;

import com.cykj.bean.BusInf;
import com.cykj.bean.Route;
import com.cykj.mapper.BusInfMapper;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BusTravelTime {
    public static List<Integer> busIds = new ArrayList<>();//已发车公交

    //公交
    @Autowired
    private BusInfMapper busInfMapper;

    //线路
    @Autowired
    private RouteMapper routeMapper;

    //站点
    @Autowired
    private SiteMapper siteMapper;

    //所有公交站点集合
    public static Map<String, Object> map = new HashMap<>();


    public BusTravelTime() {

    }

    //公交开始行驶

    public void startTravel() {
        List<Route> routes = routeMapper.selectRouteAll();
        for (int i = 0; i < routes.size(); i++) {
            System.out.println("线路：" + routes.get(i).getRouteId());
            BusTravel busTravel = new BusTravel(busInfMapper, siteMapper, routes.get(i));
            map.put(routes.get(i).getRouteId() + "", busTravel);
        }
    }

    //初始化公交状态（公交全部回归到初始状态）
    public void initBus() {
        List<Route> routes = routeMapper.selectRouteAll();
        for (int j = 0; j < routes.size(); j++) {
            Route route = routes.get(j);
            HashMap<String, Object> map = route.getRouteTypeMap();
            String split = route.getPosition().split("#")[0];
            String stop = route.getRouteInf().split("#")[0];
            Integer rightOrLeft = route.getRightOrLeft();
            System.out.println("修改！");
            System.out.println(route);
            List<BusInf> busInfs = busInfMapper.selectBusByMap(map);

            for (int i = 0; i < busInfs.size(); i++) {
                BusInf busInf = busInfs.get(i);
                if (busInf.getRightOrLeft().equals(rightOrLeft)) {
                    busInf.setBusState(17);
                    busInf.setOnloadState(13);
                    busInf.setIsDepart(22);
                    busInf.setSiteId(Integer.parseInt(split));
                    busInf.setStopSiteId(Integer.parseInt(stop));
                    System.out.println("==============");
                    System.out.println(busInf);
                    busInfMapper.updateBus(busInf);
                }
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }

    }
}
