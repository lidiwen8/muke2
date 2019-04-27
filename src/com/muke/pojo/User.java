package com.muke.pojo;

import java.util.Date;

/**
 * 
 * @Description: 用户信息实体类
 * @author:lidiwen
 * @time:2017年7月24日 下午3:30:08
 *
 */
public class User implements Cloneable{
	private int userid;
	private String username;
	private String password;
	private String realname;
	private String sex;
	private String hobbys;
	private String birthday;
	private String city;
	private String email;
	private String qq;
	private Date createtime;
	private int state;
	private int mailstate;
	private String user_img;
	private String logintime;//最后一次登陆时间
	private String description;
	private int loginNum;//登陆次数
	private String likemsgid;//收藏帖子

	
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getHobbys() {
		return hobbys;
	}
	public void setHobbys(String hobbys) {
		this.hobbys = hobbys;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "User [userid=" + userid + ", username=" + username + ", password=" + password + ", realname=" + realname
				+ ", sex=" + sex + ", hobbys=" + hobbys + ", birthday=" + birthday + ", city=" + city + ", email="
				+ email + ", qq=" + qq + ", createtime=" + createtime + "]";
	}
	
	@Override
	public User clone() {
		User user = null;
		try {
			user = (User) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public int getMailstate() {
		return mailstate;
	}

	public void setMailstate(int mailstate) {
		this.mailstate = mailstate;
	}

	public String getUser_img() {
		return user_img;
	}

	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}

	public String getLogintime() {
		return logintime;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}

	public String getLikemsgid() {
		return likemsgid;
	}

	public void setLikemsgid(String likemsgid) {
		this.likemsgid = likemsgid;
	}
}
