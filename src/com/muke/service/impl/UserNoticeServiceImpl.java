package com.muke.service.impl;

import com.muke.dao.IUserNoticeDao;
import com.muke.dao.impl.UserNoticeDaoImpl;
import com.muke.pojo.Notice;
import com.muke.service.IUserNoticeService;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:28
 * @Description:
 */
public class UserNoticeServiceImpl implements IUserNoticeService {

    private IUserNoticeDao iUserNoticeDao = new UserNoticeDaoImpl();
    @Override
    public int deleteNoticeByuser(int noteid, int userid) {
        return iUserNoticeDao.deleteNoticeByuser(noteid,userid);
    }

    @Override
    public Page getAllNoticeByuser(int userid) {
        return iUserNoticeDao.getAllNoticeByuser(userid);
    }

    @Override
    public Notice getNoticeBynoteid(int noteid) {
        return iUserNoticeDao.getNoticeBynoteid(noteid);
    }

    @Override
    public int setReadUserid(int noteid, String readuserid) {
        return iUserNoticeDao.setReadUserid(noteid,readuserid);
    }
}
