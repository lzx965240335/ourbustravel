package com.cykj.service.impl;

import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
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
    public LayuiJson selectRoutes(Map map){
        List<Route> routes = routeMapper.selectRoutes(map);
        int count = routeMapper.routeCount(map);
        return new LayuiJson(count,routes);
    }


    //增加线路
    @Override
    public boolean addRoute(Route route) {
        int maxId = routeMapper.getMaxId("t_site");

        StringBuilder position= new StringBuilder(maxId + "#");
        for (int i = 1; i < route.getSites().size(); i++) {
            position.append(maxId + i);
            if (i<route.getSites().size()-1){
                position.append("#");
            }
        }
        System.out.println(position.toString());
        route.setPosition(position.toString());
        return routeMapper.addRoute(route)>0;
    }
    //根据路线查询站点
    public List<Site> getSites(String routeId){
        String position = routeMapper.getSiteId(Integer.parseInt(routeId));
        List<Integer> list = getList(position);
        System.out.println(list.size());
        return routeMapper.getPosition(list);
    }
    //根据线路查询所有点
    @Override
    public List<Site> getPosition(String routeId){
        String position = routeMapper.getPositionId(Integer.parseInt(routeId));
        List<Integer> list = getList(position);
        return routeMapper.getPosition(list);
    }


    private List<Integer> getList(String position){
        String[] siteId=position.split("#");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < siteId.length; i++) {
            list.add(new Integer(siteId[i]));
        }
        return list;
    }

    //根据id查route
    public Route getRouteById(String routeId){
        return routeMapper.getRouteById(Integer.parseInt(routeId));
    }

    //删除线路
    public boolean deleteRoute(String routeId){
        Route route = routeMapper.getRouteById(Integer.parseInt(routeId));
        String[] siteId=route.getPosition().split("#");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < siteId.length; i++) {
            list.add(new Integer(siteId[i]));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("routeId",Integer.parseInt(routeId));
        map.put("list",list);
        int i = routeMapper.deleteRoute(map);
        return i>0;
    }

    @Override
    public Map<Integer, List<Route>> getRoutes(String startId, String endId) {
        List<Route> list = routeMapper.getRoutes();
        //正向乘车
        List<Route> routeListR = new ArrayList<>();
        //逆向乘车
        List<Route> routeListL = new ArrayList<>();
        //所有方案map
        Map<Integer, List<Route>> map = new HashMap<>();
        //遍历所有线路
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
            //表示传来的最近的起点站和终点站存在
            if (flag == 2) {
                //创建查询线路对应路段的map
                Map<String,String> resultMap=new HashMap<>();
                String[]psarr=route1.getPosition().split("#");
                resultMap.put("startDot",psarr[0]);
                resultMap.put("endDot",psarr[psarr.length-1]);
                resultMap.put("startId",startId);
                resultMap.put("endId",endId);
                //查询出对应路段的所有点集合
                List<Site> sites=siteMapper.getDots(resultMap);
                //建立对应路段坐标数组集合
                List<String[]> pos=new ArrayList<>();
                for (Site site: sites) {
                    String[] p={site.getLongitude(),site.getLatitude()};
                    pos.add(p);
                }
                //创建线路及对应需要的该线路的数组集合map
                route1.setList(pos);
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
