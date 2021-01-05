package com.cykj.bean;

public class AMap {
  //  {className:'AMap.LngLat',lat:24.492822,lng:118.176976,kT:24.492822,KL:118.176976}
   private String className;
   private Double lat;//纬度
   private Double lng;//经度
   private Double kT;
   private Double KL;

    public AMap() {
    }

    public AMap(String className, Double lat, Double lng, Double kT, Double KL) {
        this.className = className;
        this.lat = lat;
        this.lng = lng;
        this.kT = kT;
        this.KL = KL;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getkT() {
        return kT;
    }

    public void setkT(Double kT) {
        this.kT = kT;
    }

    public Double getKL() {
        return KL;
    }

    public void setKL(Double KL) {
        this.KL = KL;
    }
}
