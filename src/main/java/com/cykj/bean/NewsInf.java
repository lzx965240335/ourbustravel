package com.cykj.bean;


import org.springframework.stereotype.Component;

@Component
public class NewsInf {
    private Integer newsId;

    private String newsTime;

    private String newTitle;

    private String newsUrl;

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

    public String getNewsUrl() {
        return newsUrl;
    }

    public int getNewsState() {
        return newsState;
    }

    public void setNewsState(int newsState) {
        System.out.println("创建："+newTitle);
        this.newsState = newsState;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public Dic getDic() {
        return dic;
    }

    public void setDic(Dic dic) {
        this.dic = dic;
    }


    @Override
    public String toString() {
        return "NewsInf{" +
                "newsId=" + newsId +
                ", newsTime='" + newsTime + '\'' +
                ", newTitle='" + newTitle + '\'' +
                ", newsUrl='" + newsUrl + '\'' +
                ", newsState=" + newsState +
                ", dic=" + dic +
                '}';
    }
}