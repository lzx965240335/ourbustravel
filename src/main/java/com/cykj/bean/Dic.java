package com.cykj.bean;

import org.springframework.stereotype.Component;

@Component
public class Dic {
    private Integer dicId;

    private String dickey;

    private String dicValue;

    private String dicType;

    public Dic() {
    }

    public Integer getDicId() {
        return dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }

    public String getDickey() {
        return dickey;
    }

    public void setDickey(String dickey) {
        this.dickey = dickey;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }


    @Override
    public String toString() {
        return "Dic{" +
                "dicId=" + dicId +
                ", dickey='" + dickey + '\'' +
                ", dicValue='" + dicValue + '\'' +
                ", dicType='" + dicType + '\'' +
                '}';
    }
}