package com.cykj.bean;

public class newsInf {
    private Integer newsid;

    private String newstime;

    private String newtitle;

    private String newsurl;

    public Integer getNewsid() {
        return newsid;
    }

    public void setNewsid(Integer newsid) {
        this.newsid = newsid;
    }

    public String getNewstime() {
        return newstime;
    }

    public void setNewstime(String newstime) {
        this.newstime = newstime == null ? null : newstime.trim();
    }

    public String getNewtitle() {
        return newtitle;
    }

    public void setNewtitle(String newtitle) {
        this.newtitle = newtitle == null ? null : newtitle.trim();
    }

    public String getNewsurl() {
        return newsurl;
    }

    public void setNewsurl(String newsurl) {
        this.newsurl = newsurl == null ? null : newsurl.trim();
    }
}