package com.muke.dao.impl;

import com.muke.dao.IUserRportDao;
import com.muke.pojo.Report;
import com.muke.util.DBUtil;
import com.muke.util.Page;


/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:45
 * @Description:
 */
public class UserRportDaoImpl implements IUserRportDao {
    DBUtil dbutil = new DBUtil();
    @Override
    public int insertRport(Report report) {
        String sql;
        Object[] params=null;
        if(report.getReplyid()==-1){
            sql = "INSERT INTO report (uid,username,report_type,msgid,report_detail,report_detail_type,time) VALUES (?, ?, ?, ?, ?, ?, ?)";
            params = new Object[]{report.getUid(), report.getUsername(), report.getReportType(), report.getMsgid(), report.getReportDetail(), report.getReportDetailType(), report.getTime()};
        }else {
            sql = "INSERT INTO report (uid,username,report_type,replyid,msgid,report_detail,report_detail_type,time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            params = new Object[]{report.getUid(), report.getUsername(), report.getReportType(), report.getReplyid(), report.getMsgid(), report.getReportDetail(), report.getReportDetailType(), report.getTime()};
        }
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int changeState(int reid, int state) {
        String sql = "UPDATE report SET state=? WHERE reid=?";
        Object[] params = {state,reid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public Page queryRport(Page page) {
        String sql = "SELECT * FROM report ORDER BY time DESC";
        Object[] params = {};

        Page resPage = null;

        try {
            resPage = dbutil.getQueryPage(Report.class, sql, params, page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPage;
    }
}
