package com.cykj.bean;

public class BusInf {
    private Integer busId;

    private String carNum;

    private String importTime;

    private Integer busState;//车状态 16 维修中  17 正常 20故障

    private Integer onloadState;//行车状态 13 停站    14 行驶中  15 意外事故

    private Integer isDepart;//是否发车 22未发车  21已发车

    private String busName;

    private String driverName;

    private String sixTime;

    private String fixLoad;//维修进度

    private double speed;//速度

    private Integer leftId;//行驶线路反id

    private Integer rightId;//行驶线路正id

    private Integer siteId;//当前所在点id(不是站点)

    private String dic;

    private Route route;//汽车行驶的线路

    private Site site;//汽车所在点

    private Integer rightOrLeft;//当前所行驶方向

    private Integer stopSiteId;//停靠站点Id

    private Site stopSite;

    private Integer stopTime;//停战时间/秒


    public BusInf() {
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    public Site getStopSite() {
        return stopSite;
    }

    public void setStopSite(Site stopSite) {
        this.stopSite = stopSite;
    }


    public Integer getStopSiteId() {
        return stopSiteId;
    }

    public void setStopSiteId(Integer stopSiteId) {
        this.stopSiteId = stopSiteId;
    }

    public Integer getIsDepart() {
        return isDepart;
    }

    public void setIsDepart(Integer isDepart) {
        this.isDepart = isDepart;
    }


    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Integer getLeftId() {
        return leftId;
    }

    public void setLeftId(Integer leftId) {
        this.leftId = leftId;
    }

    public Integer getRightId() {
        return rightId;
    }

    public void setRightId(Integer rightId) {
        this.rightId = rightId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getRightOrLeft() {
        return rightOrLeft;
    }

    public void setRightOrLeft(Integer rightOrLeft) {
        this.rightOrLeft = rightOrLeft;
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

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "BusInf{" +
                "busId=" + busId +
                ", carNum='" + carNum + '\'' +
                ", importTime='" + importTime + '\'' +
                ", busState=" + busState +
                ", onloadState=" + onloadState +
                ", busName='" + busName + '\'' +
                ", driverName='" + driverName + '\'' +
                ", sixTime='" + sixTime + '\'' +
                ", fixLoad='" + fixLoad + '\'' +
                ", speed=" + speed +
                ", leftId=" + leftId +
                ", rightId=" + rightId +
                ", siteId=" + siteId +
                ", site=" + site +
                ", rightOrLeft=" + rightOrLeft +
                ", isDepart=" + isDepart +
                '}';
    }
}