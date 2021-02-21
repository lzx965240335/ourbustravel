package com.cykj.util;

import com.cykj.bean.BusInf;
import com.cykj.bean.Route;
import com.cykj.bean.Site;
import com.cykj.mapper.BusInfMapper;
import com.cykj.mapper.SiteMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BusTravelThread extends Thread {

    private final long STOP_TIME = 1000;//汽车停站时间

    private final int  PROBABILITY=1000;//交通事故概率

    private final int  RANK_ONE=40;//交通事故级别概率 1

    private final int  RANK_TWO=70;//交通事故级别概率 2

    private final int  RANK_THREE=90;//交通事故级别概率 3

    private final int  RANK_FOUR=100;//交通事故级别概率 4

    private SiteMapper siteMapper;
    private BusInfMapper busInfMapper;

    private BusInf busInf;
    private Site site;//当前所在位置
    private int index;//当前车所在位置索引
    private List<Site> sites;//所有点

    public BusTravelThread(BusInf busInf, BusInfMapper busInfMapper, SiteMapper siteMapper) {
        this.busInf = busInf;
        this.busInfMapper = busInfMapper;
        this.siteMapper = siteMapper;
    }

    @Override
    public void run() {
        site = busInf.getSite();
        Integer rightOrLeft = busInf.getRightOrLeft();
        Route route = busInf.getRoute();
        //更改汽车为行驶中
        busInf.setOnloadState(14);
        busInf.setIsDepart(21);
        busInfMapper.updateBus(busInf);
        //行车路线
        String[] split = route.getPosition().split("#");
        index = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            list.add(new Integer(split[i]));
            //获取下一个点的id
            if (split[i].equals(site.getSiteId().toString())) {
                index = i + 1 < split.length ? i + 1 : -1;
            }
        }
        List<Site> sites = siteMapper.selectSiteByIds(list);
        //汽车持续行驶
        while (index != -1) {
//            System.out.println("index" + index);
            Site nextSite = sites.get(index);
//            System.out.println("======================修改前======================"+busInf.getBusId());
//            System.out.println(busInf.getCarNum()+"  getStopSiteId:"+busInf.getStopSiteId()+"  pid:"+nextSite.getPid());
            if (nextSite.getPid() != -1) {
                Integer pid = nextSite.getPid();
                //更改汽车到达站点
                busInf.setOnloadState(13);//状态
                busInf.setStopSiteId(nextSite.getPid());
                System.out.println("汽车到达站点:" + pid + " 停战时长" + STOP_TIME+"点id："+nextSite.getSiteId()+"公交："+busInf.getBusId());
                try {
                    sleep(STOP_TIME);//休眠停止时长
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //更改汽车为行驶中
                busInf.setOnloadState(14);
            }
            //更改汽车坐标
            busInf.setSiteId(nextSite.getSiteId());
//            System.out.println("======================修改后======================"+busInf.getBusId());
//            System.out.println(busInf.getCarNum()+"  getStopSiteId:"+busInf.getStopSiteId());
            busInfMapper.updateBus(busInf);
            //是否发生意外事故
//            System.out.println(site);
//            System.out.println("nextSite:" + nextSite);
            double distance = MyRoot.getDistance(site.getLatitude(), site.getLongitude(), nextSite.getLatitude(), nextSite.getLongitude()) * 1000;
//            System.out.println("距离：" + distance);
            double speed = busInf.getSpeed() * 1000 / 60 / 60 *10;//汽车每秒速度
//            System.out.println("速度：" + speed + "米/秒");
            double time = distance / speed;//需要多少秒
            site = sites.get(index);
            index = index + 1 < sites.size() ? index + 1 : -1;//重新赋值索引
            try {
//                System.out.println("花费时长：" + time + "秒");
                sleep(new Double(time * 1000).longValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            accident();
        }



        for (int i = 0; i < BusTravelTime.busIds.size(); i++) {
            if (busInf.getBusId().equals(BusTravelTime.busIds.get(i))){
                BusTravelTime.busIds.remove(BusTravelTime.busIds.get(i));
                break;
            }
        }


        //循环结束
        System.out.println(busInf.getCarNum()+"汽车到达终点站！"+busInf.getStopSiteId());
        if (busInf.getRightOrLeft()==1){
            busInf.setRightOrLeft(0);
        }else {
            busInf.setRightOrLeft(1);
        }
        //更改汽车为行驶中
        busInf.setOnloadState(13);
        busInf.setIsDepart(22);
        busInfMapper.updateBus(busInf);
    }

    //汽车发送交通事故
    private void accident(){
        Random random = new Random();
        int i = random.nextInt(PROBABILITY);
        if (i==1){
            busInf.setBusState(20);
            busInf.setOnloadState(15);
            busInfMapper.updateBus(busInf);
            index=-1;
            System.out.println("发送交通事故:"+busInf.getCarNum());
        }
    }


}
