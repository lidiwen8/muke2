package com.muke.service.impl;

import com.muke.dao.IAdminNoticeDao;
import com.muke.dao.impl.AdminNoticeDaoImpl;
import com.muke.pojo.Notice;
import com.muke.service.IAdminNoticeService;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:36
 * @Description:
 */
public class AdminNoticeServiceImpl implements IAdminNoticeService {
    private IAdminNoticeDao iAdminNoticeDao=new AdminNoticeDaoImpl();
    @Override
    public int addNotice(Notice notice) {
        return iAdminNoticeDao.addNotice(notice);
    }

    @Override
    public int updateNotice(Notice notice) {
        return iAdminNoticeDao.updateNotice(notice);
    }

    @Override
    public int deleteNotice(int noteid) {
        return iAdminNoticeDao.deleteNotice(noteid);
    }

    @Override
    public int changeNoticeState(int noteid, int state) {
        return iAdminNoticeDao.changeNoticeState(noteid,state);
    }
}
