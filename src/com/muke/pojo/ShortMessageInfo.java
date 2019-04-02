package com.muke.pojo;

import java.util.Date;
/**
 * 帖子大概必要信息(简短版）
 *
 * @author Administrator
 */
public class ShortMessageInfo {
    private int msgid;                // 帖子id
    private int accessCount;        // 浏览量
    private String msgtopic;        // 帖子标题
    private Date msgtime;            // 时间
    private String thename;            // 主题名
    private int likecount;          // 帖子点赞数
    private int replyCount;            // 回复量
    private int state;                // 帖子状态
    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }

    public int getMsgid() {
        return msgid;
    }

    public void setMsgid(int msgid) {
        this.msgid = msgid;
    }

    public String getMsgtopic() {
        return msgtopic;
    }

    public void setMsgtopic(String msgtopic) {
        this.msgtopic = msgtopic;
    }

    public Date getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(Date msgtime) {
        this.msgtime = msgtime;
    }

    public String getThename() {
        return thename;
    }

    public void setThename(String thename) {
        this.thename = thename;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
