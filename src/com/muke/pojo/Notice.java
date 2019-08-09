package com.muke.pojo;

import java.util.Date;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:23
 * @Description: 站内通知实体类
 */
public class Notice {
    private int noteid;
    private String auther;
    private String content;    // 回帖的内容
    private Long time;            // 发布时间-时间戳
    private String title;            // 标题
    private int deleteuserid;//删除用户id以,隔开
    private String readuserid;//已读用户id以,隔开
    private int state;//状态

    public int getNoteid() {
        return noteid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDeleteuserid() {
        return deleteuserid;
    }

    public void setDeleteuserid(int deleteuserid) {
        this.deleteuserid = deleteuserid;
    }

    public String getReaduserid() {
        return readuserid;
    }

    public void setReaduserid(String readuserid) {
        this.readuserid = readuserid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
