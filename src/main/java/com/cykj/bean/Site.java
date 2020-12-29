package com.cykj.bean;

public class Site {
    private Integer siteid;

    private String sitex;

    private String sitey;

    private String sitename;

    private String settime;

    private Integer peoplenum;

    public Integer getSiteid() {
        return siteid;
    }

    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    public String getSitex() {
        return sitex;
    }

    public void setSitex(String sitex) {
        this.sitex = sitex == null ? null : sitex.trim();
    }

    public String getSitey() {
        return sitey;
    }

    public void setSitey(String sitey) {
        this.sitey = sitey == null ? null : sitey.trim();
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename == null ? null : sitename.trim();
    }

    public String getSettime() {
        return settime;
    }

    public void setSettime(String settime) {
        this.settime = settime == null ? null : settime.trim();
    }

    public Integer getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(Integer peoplenum) {
        this.peoplenum = peoplenum;
    }
}