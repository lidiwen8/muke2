package com.muke.dao;

import com.muke.pojo.Notice;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 22:32
 * @Description:
 */
public interface IUserNoticeDao {
    int deleteNoticeByuser(int noteid,int userid);
    Page getAllNoticeByuser(int userid);
    Notice getNoticeBynoteid(int noteid);
    int setReadUserid(int noteid,String readuserid);
}
