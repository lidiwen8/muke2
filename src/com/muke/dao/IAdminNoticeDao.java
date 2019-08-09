package com.muke.dao;

import com.muke.pojo.Notice;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:40
 * @Description:
 */
public interface IAdminNoticeDao {
    int addNotice(Notice notice);
    int updateNotice(Notice notice);
    int deleteNotice(int noteid);
    int changeNoticeState(int noteid,int state);
}
