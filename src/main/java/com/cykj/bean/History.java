package com.cykj.bean;

public class History {
    private Integer historyId;

    private String historyInf;

    private String seachTime;

    private Integer userId;

    public History() {
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public String getHistoryInf() {
        return historyInf;
    }

    public void setHistoryInf(String historyInf) {
        this.historyInf = historyInf;
    }

    public String getSeachTime() {
        return seachTime;
    }

    public void setSeachTime(String seachTime) {
        this.seachTime = seachTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}