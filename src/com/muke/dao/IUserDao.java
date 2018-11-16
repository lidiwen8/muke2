package com.muke.dao;

import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.util.Page;

public interface IUserDao {
	int add(User user);
	int update(User user);
	User query(String username, String pw);
    Advise getAdvisedetails(int id);
	Page queryByName(String username, Page page);
	Page queryAdvice(Page page);
	/**
	 * 更新用户状态
	 * @param userid
	 * @param state
	 * @return
	 */
	int updateState(int userid, int state);
	int updateAdvise(int id, int state);

	int deleteUser(int userid);

	int updatemail(String username,String mail,int state);//更新邮箱
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	int updatePw(User user);
	/**
	 * 验证用户名是否存在
	 * @param username 用户名
	 * @return  true：存在 false：不存在
	 */
    boolean isExist(String username);
	boolean isExistmail(String mail);//邮箱是不是已经存在
    User query1(String username, String email);
	User queryemail(String email);//根据唯一的邮箱号返回user对象
	User queryusername(String username);
	int saveAdvise(String advise,String number);//网站建议和反馈
	User queryuserbyid(int userid);
	int saveUserimg(String username,String user_img);//保存用户头像路径
	int deleteUserimg(int userid);//删除用户头像路径

	int insertlogintime(String name);//插入登陆时间
}
