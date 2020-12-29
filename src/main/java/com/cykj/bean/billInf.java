package com.cykj.bean;

public class billInf {
    private Integer billid;

    private Integer userid;

    private Integer billtype;

    private Double cost;

    private String billtime;

    public Integer getBillid() {
        return billid;
    }

    public void setBillid(Integer billid) {
        this.billid = billid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getBilltype() {
        return billtype;
    }

    public void setBilltype(Integer billtype) {
        this.billtype = billtype;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getBilltime() {
        return billtime;
    }

    public void setBilltime(String billtime) {
        this.billtime = billtime == null ? null : billtime.trim();
    }
}