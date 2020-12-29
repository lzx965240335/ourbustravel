package com.cykj.bean;

public class Route {
    private Integer routeid;

    private String routename;

    private String bulidtime;

    private String routeinf;

    private String updatetime;

    public Integer getRouteid() {
        return routeid;
    }

    public void setRouteid(Integer routeid) {
        this.routeid = routeid;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename == null ? null : routename.trim();
    }

    public String getBulidtime() {
        return bulidtime;
    }

    public void setBulidtime(String bulidtime) {
        this.bulidtime = bulidtime == null ? null : bulidtime.trim();
    }

    public String getRouteinf() {
        return routeinf;
    }

    public void setRouteinf(String routeinf) {
        this.routeinf = routeinf == null ? null : routeinf.trim();
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }
}