package com.cykj.bean;

public class Departuretime {

    private Integer id;
    private String busName;
    private String departureTime;
    private String afterSite;


    private Integer routeId;//线路id
    private String startTime;//首班时间
    private String endTime;//末班时间
    private Integer intervalTime;//间隔时间


    public Departuretime() {
    }

    public Departuretime(Integer id, String busName, String departureTime, String afterSite) {
        this.id = id;
        this.busName = busName;
        this.departureTime = departureTime;
        this.afterSite = afterSite;
    }


    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getAfterSite() {
        return afterSite;
    }

    public void setAfterSite(String afterSite) {
        this.afterSite = afterSite;
    }
}
