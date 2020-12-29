package com.cykj.bean;

public class Driver {
    private Integer drivId;

    private String drivAccount;

    private String drivPassword;

    private String drivName;

    public Driver() {
    }

    public Integer getDrivId() {
        return drivId;
    }

    public void setDrivId(Integer drivId) {
        this.drivId = drivId;
    }

    public String getDrivAccount() {
        return drivAccount;
    }

    public void setDrivAccount(String drivAccount) {
        this.drivAccount = drivAccount;
    }

    public String getDrivPassword() {
        return drivPassword;
    }

    public void setDrivPassword(String drivPassword) {
        this.drivPassword = drivPassword;
    }

    public String getDrivName() {
        return drivName;
    }

    public void setDrivName(String drivName) {
        this.drivName = drivName;
    }
}