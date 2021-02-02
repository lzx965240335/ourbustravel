package com.cykj.bean;

public class BusInf {
    private Integer busId;

    private String carNum;

    private String importTime;

    private Integer busState;

    private Integer onloadState;

    private String busName;

    private String driverName;

    private String sixTime;

    private String fixLoad;//维修进度

    private Integer speed;//速度

    private Integer routeId;//行驶线路

    public BusInf() {
    }

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public Integer getBusState() {
        return busState;
    }

    public void setBusState(Integer busState) {
        this.busState = busState;
    }

    public Integer getOnloadState() {
        return onloadState;
    }

    public void setOnloadState(Integer onloadState) {
        this.onloadState = onloadState;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getSixTime() {
        return sixTime;
    }

    public void setSixTime(String sixTime) {
        this.sixTime = sixTime;
    }

    public String getFixLoad() {
        return fixLoad;
    }

    public void setFixLoad(String fixLoad) {
        this.fixLoad = fixLoad;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }
}