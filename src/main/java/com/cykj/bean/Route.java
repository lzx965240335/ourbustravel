package com.cykj.bean;

public class Route {
    private Integer routeId;

    private String routeName;

    private String buildTime;

    private String routeInf;

    private String updateTime;

    private Integer startSite;

    private Integer endSite;

    private Integer rightOrLeft;

    public Route() {
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public String getRouteInf() {
        return routeInf;
    }

    public void setRouteInf(String routeInf) {
        this.routeInf = routeInf;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStartSite() {
        return startSite;
    }

    public void setStartSite(Integer startSite) {
        this.startSite = startSite;
    }

    public Integer getEndSite() {
        return endSite;
    }

    public void setEndSite(Integer endSite) {
        this.endSite = endSite;
    }

    public Integer getRightOrLeft() {
        return rightOrLeft;
    }

    public void setRightOrLeft(Integer rightOrLeft) {
        this.rightOrLeft = rightOrLeft;
    }
}