package com.muke.dao;

import com.muke.pojo.Admin;

/**
 * 管理员数据访问接口
 * @author Administrator
 *
 */
public interface IAdminDao {
	/**
	 * 根据用户名和密码查询管理员信息
	 * @param username 用户名
	 * @param password  密码
	 * @return  管理员信息
	 */
	
		Admin login(String username,String password);
		int updatepwd(Admin admin);
		
}
