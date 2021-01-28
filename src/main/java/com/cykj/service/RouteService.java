package com.cykj.service;

import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.util.LayuiJson;

import java.util.List;
import java.util.Map;

public interface RouteService {
    //条件查询线路
    LayuiJson selectRoutes(Map map);

    //增加线路
    boolean addRoute(Route route);

    //根据路线查询站点
    List<Site> getSites(String routeId);

    //根据线路查询所有点
    public List<Site> getPosition(String routeId);

    //根据id查route
    Route getRouteById(String routeId);

    //删除线路
    boolean deleteRoute(String routeId);

    boolean updateRoute(Route route);

    public List<Map<String, Object>> selectRoutePath(List<Site> startId, List<Site> endIds);

    //查询乘车方案
    Map<Integer, List<Route>> getRoutes(String[] startPos, String[] endPos);
}
