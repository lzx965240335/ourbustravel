package com.cykj.bean;

public class Opinion {
    private Integer opinoinId;

    private String opinionInf;

    private Integer userId;

    private Integer opinionType;

    private String updateTime;

    public Opinion() {
    }

    public Integer getOpinoinId() {
        return opinoinId;
    }

    public void setOpinoinId(Integer opinoinId) {
        this.opinoinId = opinoinId;
    }

    public String getOpinionInf() {
        return opinionInf;
    }

    public void setOpinionInf(String opinionInf) {
        this.opinionInf = opinionInf;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOpinionType() {
        return opinionType;
    }

    public void setOpinionType(Integer opinionType) {
        this.opinionType = opinionType;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}