package com.cykj.bean;

public class GoodsInf {
    private Integer goodsid;

    private String goodsname;

    private String goodsimg;

    private Integer goodspoint;

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname == null ? null : goodsname.trim();
    }

    public String getGoodsimg() {
        return goodsimg;
    }

    public void setGoodsimg(String goodsimg) {
        this.goodsimg = goodsimg == null ? null : goodsimg.trim();
    }

    public Integer getGoodspoint() {
        return goodspoint;
    }

    public void setGoodspoint(Integer goodspoint) {
        this.goodspoint = goodspoint;
    }
}