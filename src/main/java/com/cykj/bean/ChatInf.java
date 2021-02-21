package com.cykj.bean;

public class ChatInf {
    private Integer chatid;

    private Integer adminid;

    private Integer userid;

    private String msgcontent;

    private String msgtype;

    private String msgrole;

    private String sendTime;

    private UserInf userInf;

    public ChatInf() {

    }

    public ChatInf(Integer adminid, Integer userid, String msgcontent, String msgtype, String msgrole) {
        this.adminid = adminid;
        this.userid = userid;
        this.msgcontent = msgcontent;
        this.msgtype = msgtype;
        this.msgrole = msgrole;
    }

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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public UserInf getUserInf() {
        return userInf;
    }

    public void setUserInf(UserInf userInf) {
        this.userInf = userInf;
    }
}