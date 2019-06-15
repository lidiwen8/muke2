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

    Page queryRport(Page page);
}
