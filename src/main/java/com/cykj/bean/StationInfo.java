package com.cykj.bean;

import java.util.List;

public class StationInfo {
    private String id;
    private String name;
    private Object location;
    private String adcode ;
    private String citycode;
    private List<Lines> buslines;

    public StationInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public List<Lines> getBuslines() {
        return buslines;
    }

    public void setBuslines(List<Lines> buslines) {
        this.buslines = buslines;
    }
}
