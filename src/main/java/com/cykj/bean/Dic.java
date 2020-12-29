package com.cykj.bean;

public class Dic {
    private Integer dicid;

    private String dickey;

    private String dicvalue;

    private String dictype;

    public Integer getDicid() {
        return dicid;
    }

    public void setDicid(Integer dicid) {
        this.dicid = dicid;
    }

    public String getDickey() {
        return dickey;
    }

    public void setDickey(String dickey) {
        this.dickey = dickey == null ? null : dickey.trim();
    }

    public String getDicvalue() {
        return dicvalue;
    }

    public void setDicvalue(String dicvalue) {
        this.dicvalue = dicvalue == null ? null : dicvalue.trim();
    }

    public String getDictype() {
        return dictype;
    }

    public void setDictype(String dictype) {
        this.dictype = dictype == null ? null : dictype.trim();
    }
}