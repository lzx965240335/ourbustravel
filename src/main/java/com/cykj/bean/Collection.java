package com.cykj.bean;

public class Collection {
    private Integer collectionid;

    private String time;

    private String begsite;

    private String endsite;

    public Integer getCollectionid() {
        return collectionid;
    }

    public void setCollectionid(Integer collectionid) {
        this.collectionid = collectionid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getBegsite() {
        return begsite;
    }

    public void setBegsite(String begsite) {
        this.begsite = begsite == null ? null : begsite.trim();
    }

    public String getEndsite() {
        return endsite;
    }

    public void setEndsite(String endsite) {
        this.endsite = endsite == null ? null : endsite.trim();
    }
}