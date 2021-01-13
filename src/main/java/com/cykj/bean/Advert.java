package com.cykj.bean;

public class Advert {
    private Integer advertId;

    private String advertImg;

    private String advertUrl;

    private String begTime;

    private String endTime;

    private Integer advertMoney;

    private String advertTitle;

    private Integer advertState;

    private String company;

    private  Dic dic;

    public Advert() {
    }

    public Integer getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Integer advertId) {
        this.advertId = advertId;
    }

    public String getAdvertImg() {
        return advertImg;
    }

    public void setAdvertImg(String advertImg) {
        this.advertImg = advertImg;
    }

    public String getAdvertUrl() {
        return advertUrl;
    }

    public void setAdvertUrl(String advertUrl) {
        this.advertUrl = advertUrl;
    }

    public String getBegTime() {
        return begTime;
    }

    public void setBegTime(String begTime) {
        this.begTime = begTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTIme(String endTime) {
        this.endTime = endTime;
    }

    public Integer getAdvertMoney() {
        return advertMoney;
    }

    public void setAdvertMoney(Integer advertMoney) {
        this.advertMoney = advertMoney;
    }

    public String getAdvertTitle() {
        return advertTitle;
    }

    public void setAdvertTitle(String advertTitle) {
        this.advertTitle = advertTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Dic getDic() {
        return dic;
    }

    public void setDic(Dic dic) {
        this.dic = dic;
    }

    public Integer getAdvertState() {
        return advertState;
    }

    public void setAdvertState(Integer advertState) {
        this.advertState = advertState;
    }
}