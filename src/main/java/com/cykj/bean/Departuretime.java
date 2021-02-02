package com.cykj.bean;

public class Departuretime {

    private Integer id;
    private String busName;
    private String departureTime;
    private String afterSite;

    public Departuretime() {

    }

    public Departuretime(Integer id, String busName, String departureTime, String afterSite) {
        this.id = id;
        this.busName = busName;
        this.departureTime = departureTime;
        this.afterSite = afterSite;
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
