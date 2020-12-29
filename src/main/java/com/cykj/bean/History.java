package com.cykj.bean;

public class History {
    private Integer historyid;

    private String historyinf;

    private String seachtime;

    private Integer userid;

    public Integer getHistoryid() {
        return historyid;
    }

    public void setHistoryid(Integer historyid) {
        this.historyid = historyid;
    }

    public String getHistoryinf() {
        return historyinf;
    }

    public void setHistoryinf(String historyinf) {
        this.historyinf = historyinf == null ? null : historyinf.trim();
    }

    public String getSeachtime() {
        return seachtime;
    }

    public void setSeachtime(String seachtime) {
        this.seachtime = seachtime == null ? null : seachtime.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}