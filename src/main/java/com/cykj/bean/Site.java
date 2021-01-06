package com.cykj.bean;

import org.springframework.stereotype.Component;

@Component
public class Site {
    private Integer siteId;

    private String longitude;

    private String latitude;

    private String siteName;

    private String setTime;

    private Integer peopleNum;

    private Integer isDot;

    public Site() {
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getIsDot() {
        return isDot;
    }

    public void setIsDot(Integer isDot) {
        this.isDot = isDot;
    }
}