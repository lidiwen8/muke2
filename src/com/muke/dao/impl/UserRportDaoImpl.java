package com.muke.dao.impl;

import com.muke.dao.IUserRportDao;
import com.muke.pojo.Report;
import com.muke.util.DBUtil;
import com.muke.util.Page;

import java.util.Map;


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
    public int queryByreplyid(int replyid, int uid) {
        String sql = "SELECT count(reid) AS count FROM report WHERE uid=? and replyid=?";
        String sql2 = "SELECT count(reid) AS count FROM report WHERE replyid=?";
        Object[] params = {uid,replyid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            if((long)map.get("count")>0)
            {
                return 2;
            }else {
                Object[] params2 = {replyid};
                map = dbutil.getObject(sql2, params2);
                if((long)map.get("count")>0){
                    return 1;
                }else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int queryBymsgid(int msgid, int uid) {
        String sql = "SELECT count(reid) AS count FROM report WHERE uid=? and msgid=?";
        String sql2 = "SELECT count(reid) AS count FROM report WHERE msgid=?";
        Object[] params = {uid,msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            if((long)map.get("count")>0)
            {
                return 2;
            }else {
                Object[] params2 = {msgid};
                map = dbutil.getObject(sql2, params2);
                if((long)map.get("count")>0){
                    return 1;
                }else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
