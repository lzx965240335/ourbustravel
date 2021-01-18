package com.cykj.bean;


import org.springframework.stereotype.Component;

@Component
public class NewsInf {
    private Integer newsId;

    private String newsTime;

    private String newTitle;

    private String newsMsg;

    private int newsState;

    private Dic dic;

    public NewsInf() {
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String newTitle) {
        this.newTitle = newTitle;
    }

    public String getNewsMsg() {
        return newsMsg;
    }

    public void setNewsMsg(String newsMsg) {
        this.newsMsg = newsMsg;
    }

    public int getNewsState() {
        return newsState;
    }

    public void setNewsState(int newsState) {
        this.newsState = newsState;
    }


    public Dic getDic() {
        return dic;
    }

    public void setDic(Dic dic) {
        this.dic = dic;
    }

}