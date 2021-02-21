package com.cykj.bean;

import java.util.HashMap;
import java.util.List;

public class Route {
    private Integer routeId;

    private String routeName;

    private String buildTime;

    private String routeInf;

    private String updateTime;

    private double money;

    //起点站
    private Integer startSite;
    //起点站名
    private String startName;
    //终点站
    private Integer endSite;
    //终点站名
    private String endName;

    //方向
    private Integer rightOrLeft;

    private Site startSiteImpl;

    private Site endSiteImpl;

    //所有点
    private String position;

    //路线集合
    private List<Site> sites;

    //点集合
    private List<Site> positions;

    //搜索数据线路坐标集合
    private List<String[]> list;

    //发车时刻表
    private Departuretime departuretime;

    //公交信息
    private List<BusInf> busInfs;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<BusInf> getBusInfs() {
        return busInfs;
    }

    public void setBusInfs(List<BusInf> busInfs) {
        this.busInfs = busInfs;
    }

    public List<Site> getPositions() {
        return positions;
    }

    public void setPositions(List<Site> positions) {
        this.positions = positions;
    }

    public List<String[]> getList() {
        return list;
    }

    public void setList(List<String[]> list) {
        this.list = list;
    }

    public Integer getRightOrLeft() {
        return rightOrLeft;
    }

    public void setRightOrLeft(Integer rightOrLeft) {
        this.rightOrLeft = rightOrLeft;
    }

    public Site getStartSiteImpl() {
        return startSiteImpl;
    }

    public void setStartSiteImpl(Site startSiteImpl) {
        this.startSiteImpl = startSiteImpl;
    }

    public Site getEndSiteImpl() {
        return endSiteImpl;
    }

    public void setEndSiteImpl(Site endSiteImpl) {
        this.endSiteImpl = endSiteImpl;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }


    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getRouteInf() {
        return routeInf;
    }

    public void setRouteInf(String routeInf) {
        this.routeInf = routeInf;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStartSite() {
        return startSite;
    }

    public void setStartSite(Integer startSite) {
        this.startSite = startSite;
    }

    public Integer getEndSite() {
        return endSite;
    }

    public void setEndSite(Integer endSite) {
        this.endSite = endSite;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Departuretime getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(Departuretime departuretime) {
        this.departuretime = departuretime;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public HashMap<String,Object> getRouteTypeMap(){
        HashMap<String, Object> map = new HashMap<>();
        if (rightOrLeft==1){
            map.put("rightId",routeId);
            map.put("rightOrLeft",1);
        }else {
            map.put("leftId",routeId);
            map.put("rightOrLeft",0);
        }
        return map;
    }



    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", routeName='" + routeName + '\'' +
                ", buildTime='" + buildTime + '\'' +
                ", routeInf='" + routeInf + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", startSite=" + startSite +
                ", endSite=" + endSite +
                ", rightOrLeft=" + rightOrLeft +
                ", startSiteImpl=" + startSiteImpl +
                ", endSiteImpl=" + endSiteImpl +
                ", position='" + position + '\'' +
                ", sites=" + sites +
                '}';
    }
}