package com.muke.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.muke.dao.IReplyDao;
import com.muke.pojo.Reply;
import com.muke.pojo.ReplyInfo;
import com.muke.util.DBUtil;
import com.muke.util.Page;

public class ReplyDaoImpl implements IReplyDao {
    private DBUtil dbutil = new DBUtil();

    @Override
    public int replyMsg(Reply reply) {
        String sql = "INSERT INTO reply(msgid,userid,replycontents,replyip) VALUES(?,?,?,?)";
        int rs = 0;
        try {
            rs = dbutil.execute(sql, new Object[]{reply.getMsgid(), reply.getUserid(), reply.getReplycontents(), reply.getReplyip()});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rs;
    }

    @Override
    public Page queryBymsgid(int msgid, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT replyid,msgid,replycontents,replytime,replyip, ");
        sBuffer.append(" u.userid,u.username,realname,sex,city,user_img,replyupdatetime,likecount  ");
        sBuffer.append(" FROM reply r");
        sBuffer.append(" INNER JOIN user u on r.userid=u.userid ");
        sBuffer.append(" WHERE r.msgid=? ");
        sBuffer.append("ORDER BY replytime ");
        Page resPage = null;
        resPage = dbutil.getQueryPage(ReplyInfo.class, sBuffer.toString(), new Object[]{msgid}, page);
        return resPage;
    }

    @Override
    public Page getAuthorReply(int msgid, int userid, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT replyid,msgid,replycontents,replytime,replyip, ");
        sBuffer.append(" u.userid,u.username,realname,sex,city,user_img,replyupdatetime,likecount  ");
        sBuffer.append(" FROM reply r");
        sBuffer.append(" INNER JOIN user u on r.userid=u.userid ");
        sBuffer.append(" WHERE r.msgid=? and r.userid=? ");
        sBuffer.append("ORDER BY replytime ");
        Page resPage = null;
        resPage = dbutil.getQueryPage(ReplyInfo.class, sBuffer.toString(), new Object[]{msgid,userid}, page);
        return resPage;
    }

    @Override
    public long queryReplyConutBymsgid(int msgid) {
        String sql = "SELECT count(*) AS count FROM reply r INNER JOIN user u on r.userid=u.userid WHERE r.msgid=? ORDER BY replytime";
        Object[] params = {msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            long count = (Long) map.get("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long queryAutherReplyConutBymsgid(int msgid,int userid){
        String sql = "SELECT COUNT(*) as count FROM (SELECT r.userid FROM reply r INNER JOIN user u on r.userid=u.userid WHERE r.msgid=? ORDER BY replytime) as a WHERE a.userid=?";
        Object[] params = {msgid,userid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            long count = (Long) map.get("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long queryReplyConutInTotalByreplytime(int msgid,Date replytime){
        String sql = "SELECT COUNT(*) AS count FROM (SELECT replytime FROM reply r INNER JOIN user u on r.userid=u.userid WHERE r.msgid=? ORDER BY replytime) as a WHERE replytime<=?";
        Object[] params = {msgid,replytime};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            long count = (Long) map.get("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long queryCountByDate(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) AS count FROM reply WHERE replytime > ? AND replytime < ?";
        Object[] params = {startDate, endDate};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            long count = (Long) map.get("count");

            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long queryCountBy() {
        String sql = "SELECT COUNT(*) AS count FROM reply";
        Map map = null;
        try {
            map = dbutil.getObject(sql);
            long count = (Long) map.get("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long queryVisit() {
        String sql = "SELECT SUM(accessCount) as sum FROM count";
        Map map = null;
        try {
            map = dbutil.getObject(sql);
            long count = Integer.parseInt(map.get("sum").toString());
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Reply queryid(int replyid) {
        String sql = "SELECT * FROM reply WHERE replyid = ?";
        Object[] params = {replyid};
        Reply reply = null;
        try {
            reply = (Reply) dbutil.getObject(Reply.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reply;
    }

    @Override
    public int updateReply(Reply reply) {
        String sql = "update reply set replycontents=? ,replyupdatetime=? where replyid=?";
        Object[] params = {reply.getReplycontents(), reply.getReplyupdatetime(), reply.getReplyid()};
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
    public int updateReply2(Reply reply) {
        String sql = "update reply set replycontents=? where replyid=?";
        Object[] params = {reply.getReplycontents(), reply.getReplyid()};
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
    public int updateReplylike(Reply reply) {
        String sql = "update reply set likecount=?,likeuserid=? where replyid=?";
        Object[] params = {reply.getLikecount(), reply.getLikeuserid(), reply.getReplyid()};
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
    public List getReplylikeUserid(int replyid) {
        String sql = "SELECT likeuserid FROM reply where replyid=?";
        Object[] params = {replyid};
        List list = null;
        try {
            list = dbutil.getQueryList(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int updateReplyState(int replyid, int rstate) {
        String sql = "update reply set rstate=? where replyid=?";
        Object[] params = {rstate, replyid};
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
    public int deleteReply(int replyid) {
        String sql = "DELETE FROM reply WHERE replyid = ?";
        Object[] params = {replyid};
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
    public List getReplyUseremail(int msgid, int userid) {
        String sql = "select email from user where userid in (select r.userid FROM message m  LEFT JOIN reply r ON r.msgid=m.msgid where m.msgid= ? and r.userid!= ? AND m.state=3) and mailstate=1";
        Object[] params = {msgid, userid};
        List list = null;
        try {
            list = dbutil.getQueryList(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List AdmingetReplyUseremail(int msgid) {
        String sql = "select email from user where (userid in (select r.userid FROM message m  LEFT JOIN reply r ON r.msgid=m.msgid where m.msgid= ? AND m.state=-1) or userid in (select userid from message where msgid=?)) and mailstate=1";
        Object[] params = {msgid,msgid};
        List list = null;
        try {
            list = dbutil.getQueryList(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List AdmingetMsgUseremail(int msgid) {
        String sql = "select email from user where userid in (select m.userid FROM message m where m.msgid= ? AND m.state=-1) and mailstate=1";
        Object[] params = {msgid};
        List list = null;
        try {
            list = dbutil.getQueryList(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

}
