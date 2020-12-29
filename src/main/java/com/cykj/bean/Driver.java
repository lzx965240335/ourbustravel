package com.cykj.bean;

public class Driver {
    private Integer drivid;

    private String drivaccount;

    private String drivpassword;

    private String drivname;

    public Integer getDrivid() {
        return drivid;
    }

    public void setDrivid(Integer drivid) {
        this.drivid = drivid;
    }

    public String getDrivaccount() {
        return drivaccount;
    }

    public void setDrivaccount(String drivaccount) {
        this.drivaccount = drivaccount == null ? null : drivaccount.trim();
    }

    public String getDrivpassword() {
        return drivpassword;
    }

    public void setDrivpassword(String drivpassword) {
        this.drivpassword = drivpassword == null ? null : drivpassword.trim();
    }

    public String getDrivname() {
        return drivname;
    }

    public void setDrivname(String drivname) {
        this.drivname = drivname == null ? null : drivname.trim();
    }
}