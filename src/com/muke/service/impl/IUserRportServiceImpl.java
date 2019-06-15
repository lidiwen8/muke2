package com.muke.service.impl;

import com.muke.dao.IUserRportDao;
import com.muke.dao.impl.UserRportDaoImpl;
import com.muke.pojo.Report;
import com.muke.service.IUserRportService;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:44
 * @Description:
 */
public class IUserRportServiceImpl implements IUserRportService {
    private IUserRportDao reportdao = new UserRportDaoImpl();
    @Override
    public int insertRport(Report report) {
        return reportdao.insertRport(report);
    }

    @Override
    public int changeState(int reid, int state) {
        return reportdao.changeState(reid,state);
    }

    @Override
    public Page queryRport(Page page) {
        return reportdao.queryRport(page);
    }
}
