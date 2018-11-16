package com.muke.dao.impl;

import com.muke.dao.IAdminDao;
import com.muke.pojo.Admin;
import com.muke.util.DBUtil;

public class AdminDaoImpl implements IAdminDao {
	DBUtil dbutil = new DBUtil();
/**
 * 管理员数据访问实现类
 */
	@Override
	public Admin login(String username, String password) {
		String sql="SELECT * from admin WHERE name=? AND binary pwd=?";
		Object[] params={username,password};
		Admin admin=null;
		try {
			admin=(Admin) dbutil.getObject(Admin.class, sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return admin;
	}
@Override
public int updatepwd(Admin admin) {
	String sql="UPDATE admin SET  pwd=? WHERE name=?";
	Object[] params={admin.getPwd(),admin.getName()};
	int rs=0;
	try {
		rs=dbutil.execute(sql, params);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return rs;
}


}
