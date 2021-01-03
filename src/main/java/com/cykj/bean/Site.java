package com.cykj.bean;

import org.springframework.stereotype.Component;

@Component
public class Site {
    private Integer siteId;

    private String siteX;

    private String siteY;

    private String siteName;

    private String setTime;

    private Integer peopleNum;

    public Site() {
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteX() {
        return siteX;
    }

    public void setSiteX(String siteX) {
        this.siteX = siteX;
    }

    public String getSiteY() {
        return siteY;
    }

    public void setSiteY(String siteY) {
        this.siteY = siteY;
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
}