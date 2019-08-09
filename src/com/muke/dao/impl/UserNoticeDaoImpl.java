package com.muke.dao.impl;

import com.muke.dao.IUserNoticeDao;
import com.muke.pojo.Notice;
import com.muke.service.IUserNoticeService;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:32
 * @Description:
 */
public class UserNoticeDaoImpl implements IUserNoticeDao {
    @Override
    public int deleteNoticeByuser(int noteid, int userid) {
        return 0;
    }

    @Override
    public Page getAllNoticeByuser(int userid) {
        return null;
    }

    @Override
    public Notice getNoticeBynoteid(int noteid) {
        return null;
    }

    @Override
    public int setReadUserid(int noteid, String readuserid) {
        return 0;
    }
}
