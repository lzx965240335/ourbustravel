package com.cykj.bean;

public class Advert {
    private Integer advertid;

    private String advertimg;

    private String adverturl;

    private String begtime;

    private String endtime;

    private Integer advertmoney;

    private String adverttitle;

    private String company;

    public Integer getAdvertid() {
        return advertid;
    }

    public void setAdvertid(Integer advertid) {
        this.advertid = advertid;
    }

    public String getAdvertimg() {
        return advertimg;
    }

    public void setAdvertimg(String advertimg) {
        this.advertimg = advertimg == null ? null : advertimg.trim();
    }

    public String getAdverturl() {
        return adverturl;
    }

    public void setAdverturl(String adverturl) {
        this.adverturl = adverturl == null ? null : adverturl.trim();
    }

    public String getBegtime() {
        return begtime;
    }

    public void setBegtime(String begtime) {
        this.begtime = begtime == null ? null : begtime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public Integer getAdvertmoney() {
        return advertmoney;
    }

    public void setAdvertmoney(Integer advertmoney) {
        this.advertmoney = advertmoney;
    }

    public String getAdverttitle() {
        return adverttitle;
    }

    public void setAdverttitle(String adverttitle) {
        this.adverttitle = adverttitle == null ? null : adverttitle.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }
}