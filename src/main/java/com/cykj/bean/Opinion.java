package com.cykj.bean;

public class Opinion {
    private Integer opinoinid;

    private String opinioninf;

    private Integer userid;

    private Integer opiniontype;

    private String updatetime;

    public Integer getOpinoinid() {
        return opinoinid;
    }

    public void setOpinoinid(Integer opinoinid) {
        this.opinoinid = opinoinid;
    }

    public String getOpinioninf() {
        return opinioninf;
    }

    public void setOpinioninf(String opinioninf) {
        this.opinioninf = opinioninf == null ? null : opinioninf.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getOpiniontype() {
        return opiniontype;
    }

    public void setOpiniontype(Integer opiniontype) {
        this.opiniontype = opiniontype;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }
}