package com.cykj.bean;

public class LogInf {
    private Integer logid;

    private String logmodule;

    private String logaction;

    private String logremark;

    private String logtime;

    private Integer roleid;

    private Integer peopleid;

    public Integer getLogid() {
        return logid;
    }

    public void setLogid(Integer logid) {
        this.logid = logid;
    }

    public String getLogmodule() {
        return logmodule;
    }

    public void setLogmodule(String logmodule) {
        this.logmodule = logmodule == null ? null : logmodule.trim();
    }

    public String getLogaction() {
        return logaction;
    }

    public void setLogaction(String logaction) {
        this.logaction = logaction == null ? null : logaction.trim();
    }

    public String getLogremark() {
        return logremark;
    }

    public void setLogremark(String logremark) {
        this.logremark = logremark == null ? null : logremark.trim();
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime == null ? null : logtime.trim();
    }

    public Integer getRoleid() {
        return roleid;
    }

    public void setRoleid(Integer roleid) {
        this.roleid = roleid;
    }

    public Integer getPeopleid() {
        return peopleid;
    }

    public void setPeopleid(Integer peopleid) {
        this.peopleid = peopleid;
    }
}