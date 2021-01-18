package com.cykj.service.impl;

import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//线的Service
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private SiteMapper siteMapper;

    //条件查询线路
    public LayuiJson selectRoutes(Map map) {
        List<Route> routes = routeMapper.selectRoutes(map);
        int count = routeMapper.routeCount(map);
        return new LayuiJson(count, routes);
    }


    @Override
    public boolean addRoute(Route route) {
        int maxId = routeMapper.getMaxId("t_site");

        StringBuilder position = new StringBuilder(maxId + "#");
        for (int i = 1; i < route.getSites().size(); i++) {
            position.append(maxId + i);
            if (i < route.getSites().size() - 1) {
                position.append("#");
            }
        }
        System.out.println(position.toString());
        route.setPosition(position.toString());
        return routeMapper.addRoute(route) > 0;
    }

    @Override
    public Map<Integer, List<Route>> getRoutes(String startId, String endId) {
        List<Route> list = routeMapper.getRoutes();
        //乘车方案集合
        List<Route> routeListR = null;
        List<Route> routeListL = null;
        Map<Integer, List<Route>> map = new HashMap<>();
        for (Route route1 : list) {
            int flag = 0;
            int a = 0;
            int b = 0;
            String[] arr = route1.getRouteInf().split("#");
            for (int i = 0; i < arr.length; i++) {
                if (startId.equals(arr[i])) {
                    flag = 1;
                    a = i;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                if (endId.equals(arr[i])) {
                    flag = 2;
                    b = i;
                }
            }
            if (flag == 2) {
                    List<Site> sites=siteMapper.getSites(null);
                if (a > b) {
                    routeListR.add(route1);
                } else if (a < b) {
                    routeListL.add(route1);
                }
            }
        }
        map.put(1, routeListR);
        map.put(0, routeListL);
        return map;
    }
}
