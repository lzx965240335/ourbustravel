package com.cykj.bean;

public class BusInf {
    private Integer busId;

    private String busName;

    private String carNum;

    private String importTime;

    private Integer busState;

    private Integer onloadState;

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
}