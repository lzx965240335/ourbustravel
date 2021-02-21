package com.cykj.service.impl;
import com.cykj.bean.BusInf;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.BusInfMapper;
import com.cykj.mapper.RouteMapper;
import com.cykj.mapper.SiteMapper;
import com.cykj.service.BusService;
import com.cykj.util.BusTravel;
import com.cykj.util.BusTravelTime;
import com.cykj.util.LayuiJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BusServiceImpl implements BusService {
    DecimalFormat df = new DecimalFormat("0.00%");
    private SimpleDateFormat simpleFormat=new SimpleDateFormat("HH:mm");
    @Autowired
    private BusInfMapper busInfMapper;

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private SiteMapper siteMapper;

    @Override
    public int addBus(BusInf busInf) {
        return busInfMapper.addBus(busInf);
    }

    @Override
    public boolean deleteBus(BusInf busInf) {
        return busInfMapper.deleteBus(busInf.getBusId())>0;
    }

    @Override
    public int updateBus(BusInf busInf) {
        return busInfMapper.updateBus(busInf);
    }

    @Override
    public LayuiJson selectBus(Map map) {
        List<BusInf> busInfs = busInfMapper.selectBus(map);
        int count = busInfMapper.count(map);
        return new LayuiJson(count,busInfs);
    }

    @Override
    public int updateState(BusInf busInf) {
        return busInfMapper.updateState(busInf);
    }

    @Override
    public LayuiJson selectBusWeiXiu(Map map) {
        List<BusInf> busInfs = busInfMapper.selectBusWeiXiu(map);
        for (BusInf busInf: busInfs) {

        }
        int count = busInfMapper.count(map);
        return new LayuiJson(count,busInfs);
    }


    //获取最近公交的时间
    @Override
    public List<HashMap<String, Object>>  getMinimumBus(List<Route> routes){
        List<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            HashMap<String, Object> map = new HashMap<>();
            if (route.getRightOrLeft()==1){
                map.put("rightId",101);
                map.put("rightOrLeft",1);
            }else {
                map.put("leftId",route.getRouteId());
                map.put("rightOrLeft",0);
            }
            //查找这条线点上的所有公交
            List<BusInf> busInfs = busInfMapper.selectBusByRouteId(map);
            System.out.println("线路："+route.getRouteInf());
            System.out.println("站点："+route.getStartSiteImpl().getSiteId());

            String[] split = route.getRouteInf().split("#");
            HashMap<String, Object> map1 = getMinSite(busInfs, split, route.getStartSiteImpl().getSiteId() + "");
            map1.put("routeId",route.getRouteId());
            list.add(map1);
        }
        return list;
    }

    //获取最小距离站  返回距离多少站
    private HashMap<String, Object> getMinSite(List<BusInf> busInfs, String[] split, String siteId){
        int index=0;
        for (int i = 0; i < split.length; i++) {
            //判断站点所在索引
            if (split[i].equals(siteId+"")){
                index=i;
                break;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        for (int i = 0; i < busInfs.size(); i++) {
            System.out.println("汽车停战点："+busInfs.get(i).getStopSiteId());
            int number=-1;
            for (int j = index; j >0 ; j--) {
                number++;
                if (busInfs.get(i).getStopSiteId()==Integer.parseInt(split[j])){
                    map.put("bus",busInfs.get(i));
                    map.put("minSite",number-1);
                    return map;
                }
            }
        }
        map.put("minSite",-1);
        return map;
    }

    //更新公交信息
    public  HashMap<String, Object> refreshBusInf(String routeId,String siteId){
        Route route = routeMapper.selectRouteTimeById(Integer.parseInt(routeId));
        HashMap<String, Object> map = route.getRouteTypeMap();
        //查找这条线点上的所有公交
        List<BusInf> busInfs = busInfMapper.selectBusByRouteId(map);
        String[] split = route.getRouteInf().split("#");
        HashMap<String, Object> map1 = getMinSite(busInfs, split, siteId);
        BusInf bus = (BusInf) map1.get("bus");
        map1.put("bus",bus);
        //获取发车时间毫秒值
        BusTravel busTravel = (BusTravel) BusTravelTime.map.get(routeId);
        if (busTravel!=null){
            if (busTravel.getList().size()>0){
                Date date = new Date(busTravel.getList().get(0));
                String format = simpleFormat.format(date);
                map1.put("time",format);
            }else {
                map1.put("time","暂无车辆");
            }
        }
        return map1;
    }

    //   定时更新所有公交信息
    public  HashMap<String, Object> getBusInfs(String routeId){
        HashMap<String, Object> map =new HashMap<>();
        Route route = routeMapper.selectRouteTimeById(Integer.parseInt(routeId));
        if (route!=null){
            HashMap<String, Object> map1 = route.getRouteTypeMap();
            //获取发车时间毫秒值
            BusTravel busTravel = (BusTravel) BusTravelTime.map.get(routeId);
            if (busTravel!=null){
                List<Long> list = busTravel.getTimeList(true);
                busTimes(list,map,busTravel);
            };
            map.put("route",route);
        }
        return map;
    }

    private void busTimes(List<Long> list,HashMap<String, Object> map,BusTravel busTravel){
        HashMap<String, Object> map1 = new HashMap<>();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        List<String> list4 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Long aLong = list.get(i);
            Date date = new Date(aLong);
            String format = simpleFormat.format(date);
            if (aLong>busTravel.getTimeLong("06:00") && aLong<busTravel.getTimeLong("10:00")){//06:00-10
                list1.add(format);
            }else if (aLong>busTravel.getTimeLong("10:00") && aLong<busTravel.getTimeLong("16:00")){//10点-16点
                list2.add(format);
            }else if (aLong>busTravel.getTimeLong("16:00") && aLong<busTravel.getTimeLong("20:00")){//16点-20点
                list3.add(format);
            }else if (aLong>busTravel.getTimeLong("20:00") && aLong<busTravel.getTimeLong("24:00")){//20点-24点
                list4.add(format);
            }
        }
        map1.put("a",list1);//06:00-10:00
        map1.put("b",list2);//10:00-16:00
        map1.put("c",list3);//16:00-20:00
        map1.put("d",list4);//20:00-23:59

        map.put("times",map1);
    }

    //获取毫秒值


}
