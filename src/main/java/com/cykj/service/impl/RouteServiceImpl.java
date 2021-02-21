package com.cykj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cykj.bean.BusInf;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.BusInfMapper;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.RouteService;
import com.cykj.util.LayuiJson;
import com.cykj.util.MyRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.*;

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

    @Autowired
    private BusInfMapper busInfMapper;

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


    //根据线路Id查找路线所有点
    public Map<String, Object> selectRouteById(String routeId){
        Map<String, Object> map = new HashMap<>();
        Route route = routeMapper.getRouteById(Integer.parseInt(routeId));

        //所有站
        String[] strings = route.getRouteInf().split("#");
        ArrayList<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            list1.add(new Integer(strings[i]));
        }
        List<Site> sites = siteMapper.selectSiteByIds(list1);
        route.setSites(sites);
        //所有点
        String[] siteId = route.getPosition().split("#");
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < siteId.length; i++) {
            list.add(new Integer(siteId[i]));
        }
        List<Site> positions = siteMapper.selectSiteByIds(list);
        route.setPositions(positions);

        HashMap<String, Object> typeMap = route.getRouteTypeMap();
        //查找这条线点上的所有公交信息
        List<BusInf> busInfs = busInfMapper.selectBusByRouteId(typeMap);
        map.put("route",route);
        map.put("busInfs",busInfs);
        return map;
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

    //查找线路公交时刻表
    public Route selectRouteTime(String routeId){
        Route route = routeMapper.selectRouteTimeById(Integer.parseInt(routeId));
        HashMap<String, Object> map = route.getRouteTypeMap();
        //公交信息
        List<BusInf> busInfs = busInfMapper.selectBusByRouteId(map);
        route.setBusInfs(busInfs);
        String[] split = route.getRouteInf().split("#");
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(new Integer(split[i]));
        }
        //所有站点
        List<Site> sites = siteMapper.selectSiteByIds(list);
        route.setSites(sites);
        return route;
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
                        boolean existRoute = isExistRoute(list, startIds.get(i).getSiteId() + "", endIds.get(j).getSiteId() + "");
                        if (!existRoute){
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
    }

    //判断线路是否存在
    private boolean isExistRoute(List<Map<String, Object>> list,String startId,String endId){
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map=list.get(i);
            //判断是否为转车
            if (map.get("type").equals("2")){
                Route startRoute= (Route) map.get("startRoute");
                Route endRoute= (Route) map.get("endRoute");
                int start = getIndexOf(startRoute.getRouteInf().split("#"), startId);
                int end= getIndexOf(endRoute.getRouteInf().split("#"), endId);
                if (start!=-1 && end!=-1){
                    System.out.println("该条线路已存在");
                    return true;
                }
            }
        }
        return false;
    }

    //获取线路
    public List<Map<String, Object>> selectRoute(String startId, String endId) {
        List<Route> routes = routeMapper.selectRouteAll();
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
                    System.out.println("本次的站点："+startId+"======="+endId);
                    String[] strings = position.split("#");
                    //两个线路都包含
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("startDot", strings[0]);
                    resultMap.put("endDot", strings[strings.length - 1]);
                    resultMap.put("startId", startId);
                    resultMap.put("endId", endId);
                    //查询出对应路段的所有点集合
                    List<Site> positions = siteMapper.getDots(resultMap);
                    ArrayList<Integer> sitesId = getSiteId(split, i, j+1);

                    List<Site> sites = siteMapper.selectSiteByIds(sitesId);
                    Route route = routes.get(i);
                    //开始线路的公交
                    HashMap<String, Object> hashMap = new HashMap<>();
                    if (route.getRightOrLeft()==1){
                        hashMap.put("rightId",route.getRouteId());
                    }else {
                        hashMap.put("leftId",route.getRouteId());
                    }
                    List<BusInf> busInfs = busInfMapper.selectBusByRouteId(hashMap);
                    int time=-1;
                    if (busInfs.size()>0){
                        time = travelTime(busInfs, sites.get(0), sites.get(sites.size() - 1), sites.size());
                    }

                    routeMap.put("type","1");
                    routeMap.put("time",time);
                    routeMap.put("route", route);
                    routeMap.put("sites", sites);//站点
                    routeMap.put("positions", positions);//所有点
                    list.add(routeMap);//添加进线路集合
                    //判断起始站是否为终点站
                } else if (k < split.length) {
                    System.out.println("包含了起始站！"+routeInf);
                    System.out.println("本次的站点："+startId+"======="+endId);
                    //包含了其中起始线路
                    Map<String, Object> startMap = new HashMap<>();
                    ArrayList<Integer> siteId = getSiteId(split, k, split.length);
                    startMap.put("route", routes.get(i));
                    startMap.put("siteId", siteId);
                    //只包含起始线路
                    startList.add(startMap);
                }
            } else if (j > 0) {
                System.out.println("包含了终点站！"+routeInf);
                System.out.println("本次的站点："+startId+"======="+endId);
                //包含了其中终点线路
                Map<String, Object> endMap = new HashMap<>();
                //只包含终点线路
                ArrayList<Integer> siteId = getSiteId(split, 0, j+1);
                System.out.println("siteId" + siteId.size());
                endMap.put("route", routes.get(i));
                endMap.put("siteId", siteId);
                endList.add(endMap);
            }
        }
        getTransferRoute(list, startList, endList);
        return list;
    }


    //计算行驶时间(返回分钟)
    //busInfList公交集合   startSite开始站点   siteSize站点数量
    private int travelTime(List<BusInf> busInfList,Site startSite,Site endSite,int siteSize){
        double speed=0;
        int stopTime=0;
        for (int i = 0; i < busInfList.size(); i++) {
            speed+=busInfList.get(i).getSpeed();
            stopTime+=busInfList.get(i).getStopTime();
        }
        //汽车平均速度
        speed = speed / busInfList.size();
        double distance = MyRoot.getDistance(startSite.getLatitude(), startSite.getLongitude(), endSite.getLatitude(), endSite.getLongitude());
        System.out.println("全长距离："+distance);
        System.out.println("速度："+speed);
        System.out.println("========================================================================");
        int v = (int) (distance / (speed/60/60));//需要多少秒
        //花费时间加上停战时间
        v+=stopTime/busInfList.size()*siteSize;
        v=v/60;
        System.out.println("时间："+v+" 分钟");
        return v;
    }


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
        System.out.println("开始站点："+start);
        System.out.println("结束站点："+end);
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
                if (siteId.get(i).equals(endId.get(j))) {
                    System.out.println("开始判断相交");
                    //拿到开始点到相交点的集合
                    Route startRoute = (Route) startMap.get("route");
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
                    return jointRouteInfo(startRoute, endRoute, startSites, endSites,siteId, endId);
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
    private Map<String, Object> jointRouteInfo(Route startRoute, Route endRoute, List<Site> startSites, List<Site> endSites,List<Integer> siteId,List<Integer> endId) {
        System.out.println("开始拼接");
        Map<String, Object> map = new HashMap<>();
        List<Site> sites = startSites;
        for (int i = 0; i < endSites.size(); i++) {
            sites.add(endSites.get(i));
        }

        //开始线路的公交
        HashMap<String, Object> hashMap = new HashMap<>();
        if (startRoute.getRightOrLeft()==1){
            hashMap.put("rightId",startRoute.getRouteId());
        }else {
            hashMap.put("leftId",startRoute.getRouteId());
        }
        List<BusInf> infs = busInfMapper.selectBusByRouteId(hashMap);
        int i=-1;
        if (infs.size()>0){
            i = travelTime(infs, startSites.get(0), startSites.get(startSites.size() - 1), siteId.size());
        }

        //结束线路的公交
        if (endRoute.getRightOrLeft()==1){
            hashMap.put("rightId",endRoute.getRouteId());
        }else {
            hashMap.put("leftId",endRoute.getRouteId());
        }
        List<BusInf> busInfs = busInfMapper.selectBusByRouteId(hashMap);
        int i1=-1;
        if (busInfs.size()>0){
            i1 = travelTime(busInfs, endSites.get(0), endSites.get(endSites.size() - 1), endId.size());
        }
        int time= i+i1;

        map.put("type", "2");
        map.put("time",time==-2?-1:time);
        map.put("startRoute", startRoute);
        map.put("endRoute", endRoute);
        map.put("positions", sites);
        map.put("siteIds", siteId);
        map.put("endIds", endId);
        return map;
    }

    //乘车策略
    @Override
    public List<Route> getRoutes(String[] startPos, String[] endPos) {
        //所有方案list
        List<Route> results = new ArrayList<>();
        //查询所有的站点
        List<Site> sites = siteMapper.getSites(null);
        //创建起点距离list和终点距离list
        Map<Site, Double> startDistance = new HashMap<>();
        Map<Site, Double> endDistance = new HashMap<>();
//        List<Double> startDistance = new ArrayList();
//        List<Double> endDistance = new ArrayList();
        //查询所有的线路
        List<Route> list = routeMapper.getRoutes();
        //遍历所有站点,取距离
        for (Site site : sites) {
            double sjuli = getDistance(Double.valueOf(startPos[0]), Double.valueOf(startPos[1]), Double.valueOf(site.getLongitude()), Double.valueOf(site.getLatitude()));
//            double sjuli = Math.sqrt(Math.abs((Double.valueOf(startPos[0]) - Double.valueOf(site.getLongitude())) * ((Double.valueOf(startPos[0]) - Double.valueOf(site.getLongitude()))) + ((Double.valueOf(startPos[1]) - Double.valueOf(site.getLatitude()))) * (Double.valueOf(startPos[1]) - Double.valueOf(site.getLatitude()))));
//            double ejuli = Math.sqrt(Math.abs((Double.valueOf(endPos[0]) - Double.valueOf(site.getLongitude())) * ((Double.valueOf(endPos[0]) - Double.valueOf(site.getLongitude()))) + ((Double.valueOf(endPos[1]) - Double.valueOf(site.getLatitude()))) * (Double.valueOf(endPos[1]) - Double.valueOf(site.getLatitude()))));
            startDistance.put(site, sjuli);

        }
        for (Site site : sites) {
            double ejuli = getDistance(Double.valueOf(endPos[0]), Double.valueOf(endPos[1]), Double.valueOf(site.getLongitude()), Double.valueOf(site.getLatitude()));
            endDistance.put(site, ejuli);
        }
        //获取最小值
//        int oneSite = startDistance.indexOf(Collections.min(startDistance));
//        int twoSite = endDistance.indexOf(Collections.min(endDistance));
        for (Map.Entry<Site, Double> entry1 : startDistance.entrySet()) {
            for (Map.Entry<Site, Double> entry2 : endDistance.entrySet()) {
                if (entry1.getValue() < 1000.0) {
                    if (entry2.getValue() < 1000.0) {
                        return  results = getPolicy(entry1.getKey(), entry2.getKey(), list);
                    }
                }

            }
        }
        return null;
    }
    //所有线路显示
    @Override
    public List<Route> Routes() {
        //查询所有的线路
        List<Route> routes = routeMapper.getRoutes();
        for (Route route : routes) {
            //创建查询线路对应路段的map
            Map<String, String> resultMap = new HashMap<>();
            String[] psarr = route.getPosition().split("#");
            resultMap.put("startDot", psarr[0]);
            resultMap.put("endDot", psarr[psarr.length - 1]);
            resultMap.put("startId", route.getRouteInf().split("#")[0]);
            resultMap.put("endId", route.getRouteInf().split("#")[route.getRouteInf().split("#").length-1]);
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
        }
        return routes;
    }

    public List<Route> getPolicy(Site oneSite, Site twoSite, List<Route> routes) {
        List<Route>result=new ArrayList<>();
        //遍历所有线路
        for (Route route : routes) {
            int flag = 0;
            int a = 0;
            int b = 0;
            String[] arr = route.getRouteInf().split("#");
            for (int i = 0; i < arr.length; i++) {
                if (oneSite.getSiteId() == Integer.parseInt(arr[i])) {
                    flag = 1;
                    a = i;
                }
            }
            for (int i = 0; i < arr.length; i++) {
                if (twoSite.getSiteId() == Integer.parseInt(arr[i])) {
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
                if (a < b ) {
                    //表示是正向路线
                    result.add(route);
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
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
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
