package com.muke.pojo;

import java.util.Date;

/**
 * 
 * @Description: 帖子实体类
 * @author:Zheng Yanbo
 * @time:2017年7月24日 下午3:23:21
 *
 */
public class Message {
	
	private int msgid;			// 帖ID
	private int userid;			// 发布人
	private String msgtopic;	// 帖子标题
	private String msgcontents;	// 帖子内容
	private Date msgtime;		// 发帖时间
	private String msgip;		// 创建主题帖的人的IP
	private int theid;			// 主题ID
	private int state;			// 帖子状态
	
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
	public String getMsgtopic() {
		return msgtopic;
	}
	public void setMsgtopic(String msgtopic) {
		this.msgtopic = msgtopic;
	}
	public String getMsgcontents() {
		return msgcontents;
	}
	public void setMsgcontents(String msgcontents) {
		this.msgcontents = msgcontents;
	}
	public Date getMsgtime() {
		return msgtime;
	}
	public void setMsgtime(Date msgtime) {
		this.msgtime = msgtime;
	}
	public String getMsgip() {
		return msgip;
	}
	public void setMsgip(String msgip) {
		this.msgip = msgip;
	}
	public int getTheid() {
		return theid;
	}
	public void setTheid(int theid) {
		this.theid = theid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "Message [msgid=" + msgid + ", userid=" + userid + ", msgtopic=" + msgtopic + ", msgcontents="
				+ msgcontents + ", msgtime=" + msgtime + ", msgip=" + msgip + ", theid=" + theid + ", state=" + state
				+ "]";
	}	
}
