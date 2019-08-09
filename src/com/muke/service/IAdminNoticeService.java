package com.muke.service;

import com.muke.pojo.Notice;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:36
 * @Description:
 */
public interface IAdminNoticeService {
    int addNotice(Notice notice);
    int updateNotice(Notice notice);
    int deleteNotice(int noteid);
    int changeNoticeState(int noteid,int state);
}
