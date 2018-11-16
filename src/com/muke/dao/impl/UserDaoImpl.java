package com.muke.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.muke.dao.IUserDao;
import com.muke.pojo.Advise;
import com.muke.pojo.MessageInfo;
import com.muke.pojo.User;
import com.muke.util.DBUtil;
import com.muke.util.Page;

public class UserDaoImpl implements IUserDao {
	DBUtil dbutil = new DBUtil();
	
	@Override
	public User query(String username, String pw) {
		String sql = "SELECT * FROM user WHERE username = ? and password = ?";
		Object[] params = {username, pw};
		
		User user = null;

		try {
			user = (User) dbutil.getObject(User.class, sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
	@Override
	public User query1(String username, String email) {
		String sql = "SELECT * FROM user WHERE username = ? and email = ?";
		Object[] params = {username,email};

		User user = null;

		try {
			user = (User) dbutil.getObject(User.class, sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}
	@Override
	public User queryemail(String email) {
		String sql = "SELECT * FROM user WHERE email = ?";
		Object[] params = {email};

		User user = null;

		try {
			user = (User) dbutil.getObject(User.class, sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}

	@Override
	public User queryusername(String username) {
		String sql = "SELECT * FROM user WHERE username = ?";
		Object[] params = {username};

		User user = null;

		try {
			user = (User) dbutil.getObject(User.class, sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}
	@Override
	public User queryuserbyid(int userid){
		String sql = "SELECT * FROM user WHERE userid = ?";
		Object[] params = {userid};
		User user = null;
		try {
			user = (User) dbutil.getObject(User.class, sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return user;
	}
	@Override
	public int add(User user) {
		String sql = "INSERT INTO user (username, password, realname, sex, hobbys, birthday, city, email, qq) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Object[] params = {user.getUsername(), user.getPassword(), user.getRealname(), user.getSex(),
				user.getHobbys(), user.getBirthday(), user.getCity(), user.getEmail(), user.getQq()};
		int res = 0;
		try {
			res = dbutil.execute(sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}

	@Override
	public int update(User user) {
		String sql = "UPDATE user SET "
				+ "password = ?, realname = ?, sex = ?, "
				+ "hobbys = ?, birthday = ?, city = ?, email = ?, qq = ?, mailstate = ?  "
				+ "WHERE userid = ?";
		Object[] params = {user.getPassword(), user.getRealname(), user.getSex(),
				user.getHobbys(), user.getBirthday(), user.getCity(), user.getEmail(), user.getQq(),user.getMailstate(),
				user.getUserid()};
		
		int res = 0;
		
		try {
			res = dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Page queryByName(String username, Page page) {
		String sql = "SELECT * FROM user WHERE username like ? ORDER BY createtime DESC";
		Object[] params = {"%"+username+"%"};
		
		Page resPage = null;
		
		try {
			resPage = dbutil.getQueryPage(User.class, sql, params, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resPage;
	}
	@Override
	public Page queryAdvice(Page page) {
		String sql = "SELECT * FROM advise";
		Page resPage = null;
		try {
			resPage = dbutil.getQueryPage(Advise.class, sql, null, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resPage;
	}
//删除用户
	@Override
	public int updateState(int userid, int state) {
		String sql = "UPDATE user SET "
				+ "state = ? "
				+ "WHERE userid = ?";
		Object[] params = {state, userid};
		
		int res = 0;
		
		try {
			res = dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	@Override
	public int updateAdvise(int id, int state) {
		String sql = "UPDATE advise SET "
				+ "states = ? "
				+ "WHERE id = ?";
		Object[] params = {state, id};

		int res = 0;

		try {
			res = dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	@Override
	public int updatemail(String username, String mail,int mailstate) {
		String sql = "UPDATE user SET "
				+ "email = ? ,mailstate = ? "
				+ "WHERE username = ?";
		Object[] params = {mail, mailstate,username};
		int res = 0;

		try {
			res = dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	@Override
	public Advise getAdvisedetails(int id){
			StringBuffer sBuffer = new StringBuffer();
			Advise advise=null;
			sBuffer.append("select * from advise where id= ?");
			try {
				advise =(Advise) dbutil.getObject(Advise.class,sBuffer.toString(),new Object[]{id});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return advise;
		}
	@Override
	public int updatePw(User user) {
		String sql="UPDATE `user` SET  `password`=?  WHERE (`username`=?) ";
		Object[] params={user.getPassword(),user.getUsername()};
		int rs=0;
		try {
			rs=dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public boolean isExist(String username) {
		String sql="select count(*) as count from user where binary username=? ";
		boolean result=true;
		Map<String, Object> map=null;
				try {
					map=dbutil.getObject(sql, new Object[]{username});
					int count=Integer.parseInt(map.get("count").toString());
					if(count>0){
						result=false;//用户名已存在
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		return result;
	}
	@Override
	public boolean isExistmail(String mail) {
		String sql="select count(*) as count from user where email=? ";
		boolean result=true;
		Map<String, Object> map=null;
		try {
			map=dbutil.getObject(sql, new Object[]{mail});
			int count=Integer.parseInt(map.get("count").toString());
			if(count>0){
				result=false;//邮箱号已存在
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	@Override
	public  int saveAdvise(String advise,String number){
		Date day=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql="INSERT INTO advise (description,number,createDate) VALUES (?, ?, ?)";
		Object[] params={advise,number,df.format(day)};
		int rs=0;
		try {
			rs=dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public int deleteUser(int userid){
		String sql = "delete from user WHERE userid =?";
		Object[] params = {userid};
		int res = 0;
		try {
			res = dbutil.execute(sql, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}
	@Override
	public int deleteUserimg(int userid){
		String sql="update user set user_img =? where userid =?";
		Object[] params={null,userid};
		int rs=0;
		try {
			rs=dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	@Override
	public int saveUserimg(String username,String user_img){
		String sql="update user set user_img =? where username =?";
		Object[] params={user_img,username};
		int rs=0;
		try {
			rs=dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public int insertlogintime(String name){
		Date day = new Date();
		SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//登录时间
		String sql="update user set logintime =? where username =?";
		Object[] params={da.format(day),name};
		int rs=0;
		try {
			rs=dbutil.execute(sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

}
