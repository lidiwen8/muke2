package com.muke.dao;

import com.muke.pojo.Report;
import com.muke.util.Page;

/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:45
 * @Description:
 */
public interface IUserRportDao {
    int insertRport(Report report);

    int changeState(int reid,int state);

    Page queryRport(Page page);
}
