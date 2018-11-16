package com.muke.pojo;

/**
 * 
 * @Description: 管理员信息实体类
 * @author:Zheng Yanbo
 * @time:2017年7月24日 下午3:16:43
 *
 */
public class Admin implements Cloneable{
	private int id;			// id
	private String name;	// 账号
	private String pwd;		// 密码
	private int authority;	// 权力
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	
	@Override
	public Admin clone() {
		Admin admin = null;
		try{
			admin = (Admin)super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return admin;
	}
}
