package com.cykj.bean;

public class ChatInf {
    private Integer chatid;

    private Integer adminid;

    private Integer userid;

    private String msgcontent;

    private String msgtype;

    private String msgrole;

    public Integer getChatid() {
        return chatid;
    }

    public void setChatid(Integer chatid) {
        this.chatid = chatid;
    }

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getMsgcontent() {
        return msgcontent;
    }

    public void setMsgcontent(String msgcontent) {
        this.msgcontent = msgcontent == null ? null : msgcontent.trim();
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype == null ? null : msgtype.trim();
    }

    public String getMsgrole() {
        return msgrole;
    }

    public void setMsgrole(String msgrole) {
        this.msgrole = msgrole == null ? null : msgrole.trim();
    }
}