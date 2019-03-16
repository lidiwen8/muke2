package com.muke.pojo;

import java.util.Date;

/**
 * @Description: 回帖信息类（联表查询）
 * @author:lidiwen
 * @time:2017年7月24日 下午3:29:10
 */
public class ReplyInfo {

    private int replyid;
    private int msgid;
    private String replycontents;
    private Date replytime;
    private String replyip;
    private int userid;
    private String username;        // 回帖人姓名
    private String realname;        // 回帖人昵称
    private String sex;                // 回帖人性别
    private String city;            // 回帖人城市
    private String user_img;            // 回帖人头像
    private Long replyupdatetime;//回复最近一次被用户修改时间
    private int likecount;//点赞数
    private String likeuserid;//点赞用户
    private int rstate;//回帖状态

    public int getReplyid() {
        return replyid;
    }

    public void setReplyid(int replyid) {
        this.replyid = replyid;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getReplycontents() {
        return replycontents;
    }

    public void setReplycontents(String replycontents) {
        this.replycontents = replycontents;
    }

    public Date getReplytime() {
        return replytime;
    }

    public void setReplytime(Date replytime) {
        this.replytime = replytime;
    }

    public String getReplyip() {
        return replyip;
    }

    public void setReplyip(String replyip) {
        this.replyip = replyip;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public Long getReplyupdatetime() {
        return replyupdatetime;
    }

    public void setReplyupdatetime(Long replyupdatetime) {
        this.replyupdatetime = replyupdatetime;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public String getLikeuserid() {
        return likeuserid;
    }

    public void setLikeuserid(String likeuserid) {
        this.likeuserid = likeuserid;
    }

    public int getRstate() {
        return rstate;
    }

    public void setRstate(int rstate) {
        this.rstate = rstate;
    }

    @Override
    public String toString() {
        return "ReplyInfo [replyid=" + replyid + ", msgid=" + msgid + ", replycontents=" + replycontents
                + ", replytime=" + replytime + ", replyip=" + replyip + ", userid=" + userid + ", username=" + username
                + ", realname=" + realname + ", sex=" + sex + ", city=" + city + "]";
    }

}
