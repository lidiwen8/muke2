package com.muke.dao.impl;

import com.muke.dao.ICountDao;
import com.muke.util.DBUtil;

import java.util.Map;

public class CountDaoImpl implements ICountDao {
    DBUtil dbutil = new DBUtil();

    @Override
    public int updateAccessCount(int msgid) {
        String sql = "UPDATE count SET accessCount=accessCount+1 WHERE msgid=?";
        Object[] params = {msgid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int updateReplyCount(int msgid) {
        String sql = "UPDATE count SET replyCount=replyCount-1 WHERE msgid=?";
        Object[] params = {msgid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }
    @Override
    public int getReplyCount(int msgid){
        String sql = "SELECT replyCount AS count FROM count WHERE msgid=?";
        Object[] params = {msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            int count = (int)map.get("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
