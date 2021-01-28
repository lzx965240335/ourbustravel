package com.cykj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//线的Service
@Service
public class RouteServiceImpl implements RouteService {

    private final int MAX_ROUTE=10;//最多查找出几条方案
    private final int MAX_START_SITE=5;//最多查找几个开始站点
    private final int MAX_END_SITE=5;//最多查找几个结束站点


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


    //增加线路
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

    //根据路线查询站点
    public List<Site> getSites(String routeId) {
        String position = routeMapper.getSiteId(Integer.parseInt(routeId));
        List<Integer> list = getList(position);
        System.out.println(list.size());
        return routeMapper.getPosition(list);
    }

    //根据线路查询所有点
    @Override
    public List<Site> getPosition(String routeId) {
        String position = routeMapper.getPositionId(Integer.parseInt(routeId));
        List<Integer> list = getList(position);
        return routeMapper.getPosition(list);
    }


    private List<Integer> getList(String position) {
        String[] siteId = position.split("#");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < siteId.length; i++) {
            list.add(new Integer(siteId[i]));
        }
        return list;
    }

    //根据id查route
    public Route getRouteById(String routeId) {
        return routeMapper.getRouteById(Integer.parseInt(routeId));
    }

    //删除线路
    public boolean deleteRoute(String routeId) {
        Route route = routeMapper.getRouteById(Integer.parseInt(routeId));
        String[] siteId = route.getPosition().split("#");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < siteId.length; i++) {
            list.add(new Integer(siteId[i]));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("routeId", Integer.parseInt(routeId));
        map.put("list", list);
        int i = routeMapper.deleteRoute(map);
        return i > 0;
    }

    @Override
    public boolean updateRoute(Route route) {
        Route route1 = routeMapper.getRouteById(route.getRouteId());
        String[] siteId = route1.getPosition().split("#");
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < siteId.length; i++) {
            list.add(new Integer(siteId[i]));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("routeId", route.getRouteId());
        map.put("list", list);
        int maxId = routeMapper.getMaxId("t_site");
        StringBuilder position = new StringBuilder(maxId + "#");
        for (int i = 1; i < route.getSites().size(); i++) {
            position.append(maxId + i);
            if (i < route.getSites().size() - 1) {
                position.append("#");
            }
        }
        map.put("routeName",route.getRouteName());
        map.put("rightOrLeft",route.getRightOrLeft());
        map.put("startSite",route.getStartSite());
        map.put("endSite",route.getEndSite());
        map.put("routeInf",route.getRouteInf());
        map.put("position",position.toString());
        map.put("sites",route.getSites());
        return routeMapper.updateRoute(map)>0;
    }

    @Override
    public List<Map<String, Object>> selectRoutePath(List<Site> startIds, List<Site> endIds) {
        List<Map<String, Object>> list = new ArrayList<>();
        getRouteList(list, startIds, endIds);
        return list;
    }

    //循环获取点
    private void getRouteList(List<Map<String, Object>> list, List<Site> startIds, List<Site> endIds) {
        //遍历前两个站点
        for (int i = 0; i < startIds.size(); i++) {
            if (i<MAX_START_SITE){
                for (int j = 0; j < endIds.size(); j++) {
                    if (j<MAX_END_SITE){
                        List<Map<String, Object>> maps = selectRoute(startIds.get(i).getSiteId() + "", endIds.get(j).getSiteId() + "");
                        for (int k = 0; k < maps.size(); k++) {
                            //累计添加站点
                            list.add(maps.get(k));
                            if (list.size()>MAX_ROUTE){
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    //获取线路
    public List<Map<String, Object>> selectRoute(String startId, String endId) {
        List<Route> routes = routeMapper.getRoutes();
        //包含了所有线路
        List<Map<String, Object>> list = new ArrayList<>();
        //包含了其中起点线路
        List<Map<String, Object>> startList = new ArrayList<>();
        //只包含终点线路
        List<Map<String, Object>> endList = new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {
            Map<String, Object> routeMap = new HashMap<>();
            String routeInf = routes.get(i).getRouteInf();
            //站点的id
            String[] split = routeInf.split("#");
            //获取起始站和终点站的索引
            int k =getIndexOf(split,startId);
            int j = getIndexOf(split,endId);
            if (k != -1) {
                if (j != -1 && k < j) {
                    String position = routes.get(i).getPosition();
                    System.out.println("包含了起始站和终点站！！"+routeInf);
                    String[] strings = position.split("#");
                    //两个线路都包含
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("startDot", strings[0]);
                    resultMap.put("endDot", strings[strings.length - 1]);
                    resultMap.put("startId", startId);
                    resultMap.put("endId", endId);
                    //查询出对应路段的所有点集合
                    List<Site> positions = siteMapper.getDots(resultMap);
                    ArrayList<Integer> sitesId = getSiteId(split, i, j);

                    List<Site> sites = siteMapper.selectSiteByIds(sitesId);
                    Route route = routes.get(i);
                    routeMap.put("type", 1);
                    routeMap.put("route", route);
                    routeMap.put("sites", sites);//站点

                    routeMap.put("positions", positions);//所有点
                    list.add(routeMap);//添加进线路集合
                    //判断起始站是否为终点站
                } else if (k < split.length) {
                    System.out.println("包含了起始站！");
                    //包含了其中起始线路
                    Map<String, Object> startMap = new HashMap<>();
                    ArrayList<Integer> siteId = getSiteId(split, k, split.length);
                    startMap.put("route", routes.get(i));
                    startMap.put("siteId", siteId);
                    //只包含起始线路
                    startList.add(startMap);
                }
            } else if (j > 0) {
                System.out.println("包含了终点站！");
                //包含了其中终点线路
                Map<String, Object> endMap = new HashMap<>();
                //只包含终点线路
                ArrayList<Integer> siteId = getSiteId(split, 0, j);
                System.out.println("siteId" + siteId.size());
                endMap.put("route", routes.get(i));
                endMap.put("siteId", siteId);
                endList.add(endMap);
            }
        }
        getTransferRoute(list, startList, endList);
        return list;
    }

//    //判断这个站点是否已经在方案中存在,防止一条线路有多个站点方案
//    private boolean isExistRoute(){
//
//        return false;
//    }



    //索引遍历
    private int getIndexOf(String[] strings,String id){
        int indeOf=-1;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals(id)){
                return i;
            }
        }
        return indeOf;
    }


    //根据索引拿id
    private ArrayList<Integer> getSiteId(String[] split, int start, int end) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = start; i < end; i++) {
            if (split[i].length()>0){
                list.add(new Integer(split[i]));
            }
        }
        return list;
    }

    //获取转车线路
    private void getTransferRoute(List<Map<String, Object>> list, List<Map<String, Object>> startList, List<Map<String, Object>> endList) {
        System.out.println("获取转车类型");
        for (int i = 0; i < startList.size(); i++) {
            Map<String, Object> startMap = startList.get(i);
            for (int j = 0; j < endList.size(); j++) {
                Map<String, Object> endMap = endList.get(j);
                Map<String, Object> map = isExistSite(startMap, endMap);
                if (map != null) {
                    list.add(map);
                }
            }
        }
    }

    //遍历集合判断是否存在相交
    private Map<String, Object> isExistSite(Map<String, Object> startMap, Map<String, Object> endMap) {
        List<Integer> siteId = (ArrayList<Integer>) startMap.get("siteId");
        List<Integer> endId = (ArrayList<Integer>) endMap.get("siteId");
        for (int i = 0; i < siteId.size(); i++) {
            for (int j = 0; j < endId.size(); j++) {
                System.out.println("开始的id：" + siteId.get(i));
                System.out.println("结束的id：" + endId.get(j));
                if (siteId.get(i).equals(endId.get(j))) {
                    System.out.println("开始判断相交");
                    //拿到开始点到相交点的集合
                    Route startRoute = (Route) startMap.get("route");
                    System.out.println("开始的站：" + startRoute.getPosition());
                    String[] position = startRoute.getPosition().split("#");
                    List<Site> startSites = selectSiteByScope(position, siteId.get(0).toString(), siteId.get(i).toString());
                    //到相交点到结束点
                    Route endRoute = (Route) endMap.get("route");
                    System.out.println("size:" + endId.size());
                    System.out.println("endId" + endId.get(j).toString());
                    System.out.println("后" + endId.get(endId.size() - 1).toString());
                    String[] endPosition = endRoute.getPosition().split("#");
                    List<Site> endSites = selectSiteByScope(endPosition, endId.get(j).toString(), endId.get(endId.size() - 1).toString());
                    //获取封装map
                    return jointRouteInfo(startRoute, endRoute, startSites, endSites);
                }
            }
        }
        return null;
    }

    //范围查找点
    private List<Site> selectSiteByScope(String[] split, String startId, String endId) {
        System.out.println("开始范围查找点:" + split.length);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("startDot", split[0]);
        resultMap.put("endDot", split[split.length - 1]);
        resultMap.put("startId", startId);
        resultMap.put("endId", endId);
        //查询出对应路段的所有点集合
        return siteMapper.getDots(resultMap);
    }

    //拼接id和点集合
    private Map<String, Object> jointRouteInfo(Route startRoute, Route endRoute, List<Site> startSites, List<Site> endSites) {
        System.out.println("开始拼接");
        Map<String, Object> map = new HashMap<>();
        List<Site> sites = startSites;
        for (int i = 0; i < endSites.size(); i++) {
            sites.add(endSites.get(i));
        }
        map.put("type", 2);
        map.put("startRoute", startRoute);
        map.put("endRoute", endRoute);
        map.put("positions", sites);
        return map;
    }
    //乘车策略
    @Override
    public Map<Integer, List<Route>> getRoutes(String[] startPos, String[] endPos) {

        //查询所有的站点
        List<Site> sites = siteMapper.getSites(null);
        //创建起点距离list和终点距离list
        Map<Site,Double> startDistance=new HashMap<>();
        Map<Site,Double> endDistance=new HashMap<>();
//        List<Double> startDistance = new ArrayList();
//        List<Double> endDistance = new ArrayList();
        //查询所有的线路
        List<Route> list = routeMapper.getRoutes();
        //正向乘车
        List<Route> routeListR = new ArrayList<>();
        //逆向乘车
        List<Route> routeListL = new ArrayList<>();
        //所有方案map
        Map<Integer, List<Route>> map = new HashMap<>();
        //遍历所有站点,取距离
        for (Site site : sites) {
            double sjuli= getDistance(Double.valueOf(startPos[0]),Double.valueOf(startPos[1]),Double.valueOf(site.getLongitude()),Double.valueOf(site.getLatitude()));

//            double sjuli = Math.sqrt(Math.abs((Double.valueOf(startPos[0]) - Double.valueOf(site.getLongitude())) * ((Double.valueOf(startPos[0]) - Double.valueOf(site.getLongitude()))) + ((Double.valueOf(startPos[1]) - Double.valueOf(site.getLatitude()))) * (Double.valueOf(startPos[1]) - Double.valueOf(site.getLatitude()))));
//            double ejuli = Math.sqrt(Math.abs((Double.valueOf(endPos[0]) - Double.valueOf(site.getLongitude())) * ((Double.valueOf(endPos[0]) - Double.valueOf(site.getLongitude()))) + ((Double.valueOf(endPos[1]) - Double.valueOf(site.getLatitude()))) * (Double.valueOf(endPos[1]) - Double.valueOf(site.getLatitude()))));
            System.out.println("起点距离" + sjuli);
            startDistance.put(site,sjuli);

        }
        for (Site site : sites) {
            double ejuli= getDistance(Double.valueOf(endPos[0]),Double.valueOf(endPos[1]),Double.valueOf(site.getLongitude()),Double.valueOf(site.getLatitude()));
            System.out.println("终点距离" + ejuli);
            endDistance.put(site,ejuli);
        }
        //获取最小值
//        int oneSite = startDistance.indexOf(Collections.min(startDistance));
//        int twoSite = endDistance.indexOf(Collections.min(endDistance));
        for (Map.Entry<Site,Double> entry1 : startDistance.entrySet()) {
            for (Map.Entry<Site,Double> entry2 : endDistance.entrySet()) {
                if (entry1.getValue()<1000.0){
                    if (entry2.getValue()<1000.0){
                        Map<Route,Integer> resultMap=getPolicy(entry1.getKey(),entry2.getKey(),list);
                        System.out.println(resultMap);
                        if (resultMap!=null){
                            for (Map.Entry<Route,Integer> entry : resultMap.entrySet()) {
                                if (entry.getValue()==1){
                                    routeListR.add(entry.getKey());
                                }else {
                                    routeListL.add(entry.getKey());
                                }
                                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            }
                        }else {

                        }
                    }
                }

            }
        }
        map.put(1, routeListR);
        map.put(0, routeListL);
        return map;
    }

    public Map<Route,Integer> getPolicy(Site oneSite, Site twoSite, List<Route> routes) {
        Map<Route,Integer> result=new HashMap<>();
        //遍历所有线路
        for (Route route : routes) {
            int flag = 0;
            int a = 0;
            int b = 0;
            String[] arr = route.getRouteInf().split("#");
            for (int i = 0; i < arr.length; i++) {
                if (oneSite.getSiteId().equals(arr[i])) {
                    flag = 1;
                    a = i;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                if (twoSite.getSiteId().equals(arr[i])) {
                    flag = 2;
                    b = i;
                }
            }
            //表示传来的最近的起点站和终点站存在
            if (flag == 2) {
                //创建查询线路对应路段的map
                Map<String, String> resultMap = new HashMap<>();
                String[] psarr = route.getPosition().split("#");
                resultMap.put("startDot", psarr[0]);
                resultMap.put("endDot", psarr[psarr.length - 1]);
                resultMap.put("startId", oneSite.getSiteId().toString());
                resultMap.put("endId", twoSite.getSiteId().toString());
                //查询出对应路段的所有点集合
                List<Site> minsites = siteMapper.getDots(resultMap);
                //建立对应路段坐标数组集合
                List<String[]> pos = new ArrayList<>();
                for (Site site : minsites) {
                    String[] p = {site.getLongitude(), site.getLatitude()};
                    pos.add(p);
                }
                //创建线路及对应需要的该线路的数组集合map
                route.setList(pos);
                if (a > b&&route.getRightOrLeft()==1) {
                    //表示是正向路线
                    result.put(route,1);
                } else if (a > b&&route.getRightOrLeft()==0) {
                    //表示是反向路线
                    result.put(route,0);
                }

            }
        }
        return result;
    }
    public static final double R = 6378137; // 地球半径
    /**
     * 坐标之间的距离
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 单位米
     */
    public static double getDistance(double lng1,double lat1 ,double lng2 ,double lat2 ) {
        lat1 = Math.toRadians(lat1);
        lng1 = Math.toRadians(lng1);
        lat2 = Math.toRadians(lat2);
        lng2 = Math.toRadians(lng2);
        double d1 = Math.abs(lat1 - lat2);
        double d2 = Math.abs(lng1 - lng2);
        double p = Math.pow(Math.sin(d1 / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(d2 / 2), 2);
        double dis = R * 2 * Math.asin(Math.sqrt(p));
        return dis;
    }

}
