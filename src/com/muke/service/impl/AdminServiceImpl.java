package com.muke.service.impl;

import com.muke.dao.IAdminDao;
import com.muke.dao.impl.AdminDaoImpl;
import com.muke.pojo.Admin;
import com.muke.service.IAdminService;
/**
 * 管理员业务实现类
 * @author Administrator
 *
 */
public class AdminServiceImpl implements IAdminService {
		IAdminDao adminDao= new AdminDaoImpl();
	@Override
	public Admin login(String username, String password) {
		return adminDao.login(username, password);
	}
	@Override
	public int updatepwd(Admin admin) {
		return adminDao.updatepwd(admin);
	}


}
