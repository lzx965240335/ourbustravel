package com.cykj.bean;

public class LogInf {
    private Integer logId;

    private String logmodule;

    private String logaction;

    private String logremark;

    private String logtime;

    private Integer roleid;

    private Integer peopleid;

    public LogInf() {
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getLogmodule() {
        return logmodule;
    }

    public void setLogmodule(String logmodule) {
        this.logmodule = logmodule;
    }

    public String getLogaction() {
        return logaction;
    }

    public void setLogaction(String logaction) {
        this.logaction = logaction;
    }

    public String getLogremark() {
        return logremark;
    }

    public void setLogremark(String logremark) {
        this.logremark = logremark;
    }

    public String getLogtime() {
        return logtime;
    }

    public void setLogtime(String logtime) {
        this.logtime = logtime;
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