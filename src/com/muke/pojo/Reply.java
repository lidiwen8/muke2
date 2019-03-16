package com.muke.pojo;

import java.util.Date;

/**
 * @Description: 回复信息实体类
 * @author:lidiwen
 * @time:2018年7月24日 下午3:27:07
 */
public class Reply {
    private int replyid;
    private int msgid;
    private int userid;
    private String replycontents;    // 回帖的内容
    private Date replytime;            // 回帖时间
    private String replyip;            // 回帖人ip
    private Long replyupdatetime;//回复最近一次修改时间戳
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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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
        return "Reply [replyid=" + replyid + ", msgid=" + msgid + ", userid=" + userid + ", replycontents="
                + replycontents + ", replytime=" + replytime + ", replyip=" + replyip + "]";
    }

}
