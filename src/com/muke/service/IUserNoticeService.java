package com.muke.service;

import com.muke.pojo.Notice;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:27
 * @Description:
 */
public interface IUserNoticeService {
    int deleteNoticeByuser(int noteid,int userid);
    Page getAllNoticeByuser(int userid);
    Notice getNoticeBynoteid(int noteid);
    int setReadUserid(int noteid,String readuserid);
}
