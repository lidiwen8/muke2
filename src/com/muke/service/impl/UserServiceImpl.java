package com.muke.service.impl;

import com.muke.dao.IUserDao;
import com.muke.dao.impl.UserDaoImpl;
import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.service.IUserService;
import com.muke.util.Page;

public class UserServiceImpl implements IUserService {

	IUserDao iUserDao = new UserDaoImpl();
	
	@Override
	public User userLogin(String username, String pw) {
		// TODO Auto-generated method stub
		return iUserDao.query(username, pw);
	}

	public User userPass(String username, String email) {
		// TODO Auto-generated method stub
		return iUserDao.query1(username, email);
	}
	@Override
	public User useremail(String email) {
		// TODO Auto-generated method stub
		return iUserDao.queryemail(email);
	}
	@Override
	public User username(String username) {
		// TODO Auto-generated method stub
		return iUserDao.queryusername(username);
	}
	@Override
	public Advise getAdvisedetails(int id){
		return  iUserDao.getAdvisedetails(id);
	}

	@Override
	public int update(User user) {
		// TODO Auto-generated method stub
		return iUserDao.update(user);
	}

	@Override
	public int userRegister(User user) {
		// TODO Auto-generated method stub
		return iUserDao.add(user);
	}

	@Override
	public Page searchByName(String username, Page page) {
		return iUserDao.queryByName(username, page);
	}
	@Override
	public Page searchAdvice(Page page){
		return iUserDao.queryAdvice(page);
	}

	@Override
	public int deleteUser(int userid) {
		return iUserDao.updateState(userid, -1);
	}
	@Override
	public int delete(int userid) {
		return iUserDao.deleteUser(userid);
	}
	@Override
	public int deleteAdvise(int id) {
		return iUserDao.updateAdvise(id, -1);
	}

	@Override
	public int restoreUser(int userid) {
		return iUserDao.updateState(userid, 0);
	}
	@Override
	public int restoreAdvise(int id) {
		return iUserDao.updateAdvise(id, 0);
	}
	@Override
	public int updatePw(User user) {
		return iUserDao.updatePw(user);
	}

	@Override
	public int modifyUser(int userid, int state) {
		return iUserDao.updateState(userid, state);
	}

	@Override
	public boolean isExist(String username) {
		return iUserDao.isExist(username);
	}

	@Override
	public boolean isExistmail(String mail) {
		return iUserDao.isExistmail(mail);
	}

	@Override
	public int updatemail(String username,String mail,int state) {
		return iUserDao.updatemail(username,mail,state);
	}//更新邮箱
	@Override
	public int saveAdvise(String advise,String number){//存网站建议和反馈
		return iUserDao.saveAdvise(advise,number);
	}
	@Override
	public int saveUserimg(String username,String user_img){return iUserDao.saveUserimg(username,user_img);}
	@Override
	public int deleteUserimg(int userid){return iUserDao.deleteUserimg(userid);}
	@Override
	public User queryuserbyid(int userid){return iUserDao.queryuserbyid(userid);}

	@Override
	//插入登陆时间
	public int insertlogintime(String name){
		return iUserDao.insertlogintime(name);
	}

}
