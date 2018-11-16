package com.muke.service;

import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.util.Page;

public interface IUserService {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	int userRegister(User user);
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	User userLogin(String username,String password);
	User userPass(String username,String email);
	User useremail(String email);
	User queryuserbyid(int userid);
	User username(String username);
	Advise getAdvisedetails(int id);
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	int update(User user);
	
	/**
	 * 根据用户名和分页信息查询数据
	 * @param username
	 * @param page
	 * @return
	 */
	Page searchByName(String username,Page page);
	Page searchAdvice(Page page);//查询建议

	
	/**
	 * 删除信息
	 * @param userid
	 * @return
	 */
	int deleteUser(int userid);

	int delete(int userid);//删除用户
	int deleteAdvise(int id);
	/**
	 * 恢复状态
	 * @param userid
	 * @return
	 */
	int restoreUser(int userid);

	int restoreAdvise(int id);
	//修改密码
	int updatePw(User user);

	int updatemail(String username,String mail,int state);//更新邮箱

	int modifyUser(int userid, int state);
	/**
	 * 验证用户名是否存在
	 * @param username 用户名
	 * @return  true：存在 false：不存在
	 */
    boolean isExist(String username);
	boolean isExistmail(String mail);
	int saveAdvise(String advise,String number);

	int saveUserimg(String username,String user_img);
	int deleteUserimg(int userid);
	int insertlogintime(String name);//插入登陆时间

}

 
