package com.muke.service;

import com.muke.pojo.Report;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:24
 * @Description:
 */
public interface IUserRportService {
    int insertRport(Report report);

    int changeState(int reid,int state);

    int queryByreplyid(int replyid,int uid);//返回值为0说明，没有任何人举报，返回1代表有人举报了(不是用户本人)，返回2代表用户已经举报过了

    int queryBymsgid(int msgid,int uid);//返回值为0说明，没有任何人举报，返回1代表有人举报了(不是用户本人)，返回2代表用户已经举报过了

    Page queryRport(Page page);
}
