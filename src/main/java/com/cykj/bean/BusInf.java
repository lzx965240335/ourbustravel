package com.cykj.bean;

public class BusInf {
    private Integer busid;

    private String carnum;

    private String importtime;

    private Integer busstate;

    private Integer onloadstate;

    public Integer getBusid() {
        return busid;
    }

    public void setBusid(Integer busid) {
        this.busid = busid;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum == null ? null : carnum.trim();
    }

    public String getImporttime() {
        return importtime;
    }

    public void setImporttime(String importtime) {
        this.importtime = importtime == null ? null : importtime.trim();
    }

    public Integer getBusstate() {
        return busstate;
    }

    public void setBusstate(Integer busstate) {
        this.busstate = busstate;
    }

    public Integer getOnloadstate() {
        return onloadstate;
    }

    public void setOnloadstate(Integer onloadstate) {
        this.onloadstate = onloadstate;
    }
}