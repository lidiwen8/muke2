package com.muke.pojo;

import java.util.Date;

/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:24
 * @Description:
 */
public class Report {
    private int reid;
    private int uid;
    private String username;
    private String reportType;
    private int replyid;
    private int msgid;
    private String reportDetail;
    private String reportDetailType;
    private Date time;
    private int state;


    public int getReid() {
        return reid;
    }

    public void setReid(int reid) {
        this.reid = reid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

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

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }

    public String getReportDetailType() {
        return reportDetailType;
    }

    public void setReportDetailType(String reportDetailType) {
        this.reportDetailType = reportDetailType;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
