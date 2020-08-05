package com.muke.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.muke.dao.IMessageDao;
import com.muke.pojo.Message;
import com.muke.pojo.MessageCriteria;
import com.muke.pojo.MessageInfo;
import com.muke.pojo.ShortMessageInfo;
import com.muke.util.DBUtil;
import com.muke.util.Page;

import javax.servlet.annotation.WebServlet;

public class MessageDaoImpl implements IMessageDao {
    DBUtil dbutil = new DBUtil();

    @Override
    public int add(Message msg) {
        String sql = "INSERT INTO message(userid, msgtopic, msgcontents,  msgip, theid) VALUES ( ?, ?, ?, ?, ?)";
        Object[] params = {msg.getUserid(), msg.getMsgtopic(), msg.getMsgcontents(), msg.getMsgip(), msg.getTheid()};
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
    public int delete(int msgid) {
        String sql = "DELETE FROM message WHERE msgid = ?";
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
    public int updateMsg(MessageInfo msgInfo) {
        String sql = "update message  SET msgcontents = ? ,theid = ? ,msgip = ?,msgtopic = ?,msgupdatetime=?  where msgid = ?";
        Object[] params = {msgInfo.getMsgcontents(), msgInfo.getTheid(), msgInfo.getMsgip(), msgInfo.getMsgtopic(), msgInfo.getMsgupdatetime(), msgInfo.getMsgid()};
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
    public int updateState(int msgid, int state) {
        String sql = "update message set state=? where msgid=?";
        Object[] params = {state, msgid};

        int result = 0;

        try {
            result = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int upadateReplyident(int msgid,int replyident){
        String sql = "update message set replyident=? where msgid=?";
        Object[] params = {replyident, msgid};

        int result = 0;

        try {
            result = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int updateMessagelike(Message message) {
        String sql = "update message set likecount=?,likeuserid=? where msgid=?";
        Object[] params = {message.getLikecount(), message.getLikeuserid(), message.getMsgid()};
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
    public List getMessagelikeUserid(int msgid) {
        String sql = "SELECT likeuserid FROM message where msgid=?";
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

    @Override
    public int queryMsgState(int msgid) {
        String sql = "select state from message where msgid=?";
        Object[] params = {msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            int state = (int) map.get("state");
            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //根据帖子id查询详细信息
    @Override
    public MessageInfo get(int msgid) {
        StringBuffer sBuffer = new StringBuffer();
        MessageInfo messageInfo = null;
        sBuffer.append(" select a.msgid, msgtopic, msgcontents, msgtime, msgip,msgupdatetime,likecount,a.state, a.theid, ");
        sBuffer.append(" c.thename,a.userid, username, realname, sex, city, user_img ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append("where a.msgid=? ");
        try {
            messageInfo = (MessageInfo) dbutil.getObject(MessageInfo.class, sBuffer.toString(), new Object[]{msgid});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return messageInfo;
    }

    @Override
    public Page query(MessageCriteria messageCriteria, Page page) {

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select a.msgid, msgtopic, msgcontents, msgtime,msgupdatetime,msgip,likecount, a.state, ");
        sBuffer.append(" a.theid, c.thename,a.userid, username, realname, sex, city,user_img, ");
        /*sBuffer.append(" d.accessCount, d.replyCount,max(e.replytime) as replytime ");*/
        sBuffer.append(" d.accessCount, d.replyCount ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append(" LEFT JOIN count d ON a.msgid=d.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND a.userid=? ");
                params.add(userId);
            }
            String username = messageCriteria.getUsername();
            if (username != null && username.trim().length() > 0) {
                sBuffer.append(" AND b.realname LIKE ? ");
                params.add("%" + username + "%");
            }
            int theid = messageCriteria.getTheid();
            if (theid > 0) {
                sBuffer.append(" AND a.theid=? ");
                params.add(theid);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND a.state>=? ");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND a.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }
        sBuffer.append(" GROUP BY a.msgid ");
//		sBuffer.append(" GROUP BY a.msgid, msgtopic, msgcontents, msgtime, msgip, a.state, ");
//		sBuffer.append(" a.theid, c.thename,a.userid, username, realname, sex, city, ");
//		sBuffer.append(" d.accessCount, d.replyCount ");

        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }

    @Override
    public Page searchUserAllMyMsg(MessageCriteria messageCriteria, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select a.msgid, msgtopic, msgtime,likecount, replyident,a.state, ");
        sBuffer.append(" d.accessCount, d.replyCount ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append(" LEFT JOIN count d ON a.msgid=d.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND a.userid=? ");
                params.add(userId);
            }
            int theid = messageCriteria.getTheid();
            if (theid > 0) {
                sBuffer.append(" AND a.theid=? ");
                params.add(theid);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND a.state>=? ");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND a.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }
        sBuffer.append(" GROUP BY a.msgid ");
        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }


    @Override
    public Page searchUserMyMsg(MessageCriteria messageCriteria, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select a.msgid, msgtopic, msgtime,likecount, replyident,a.state, ");
        sBuffer.append(" d.accessCount, d.replyCount ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append(" LEFT JOIN count d ON a.msgid=d.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND a.userid=? ");
                params.add(userId);
            }
            String username = messageCriteria.getUsername();
            if (username != null && username.trim().length() > 0) {
                sBuffer.append(" AND b.realname LIKE ? ");
                params.add("%" + username + "%");
            }
            int theid = messageCriteria.getTheid();
            if (theid > 0) {
                sBuffer.append(" AND a.theid=? ");
                params.add(theid);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND a.state>=? ");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND a.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }
        sBuffer.append(" GROUP BY a.msgid ");
        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }


    @Override
    public Page query1(MessageCriteria messageCriteria, Page page) {

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select a.msgid, msgtopic, msgcontents, msgtime, msgip,msgupdatetime,likecount, a.state, ");
        sBuffer.append(" a.theid, c.thename,a.userid, username, realname, sex, city,user_img, ");
        /*sBuffer.append(" d.accessCount, d.replyCount,max(e.replytime) as replytime ");*/
        sBuffer.append(" d.accessCount, d.replyCount ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append(" LEFT JOIN count d ON a.msgid=d.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND a.userid=? ");
                params.add(userId);
            }
            String username = messageCriteria.getUsername();
            if (username != null && username.trim().length() > 0) {
                sBuffer.append(" AND b.realname LIKE ? ");
                params.add("%" + username + "%");
            }
            int theid = messageCriteria.getTheid();
            if (theid > 0) {
                sBuffer.append(" AND a.theid=? ");
                params.add(theid);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND a.state>=? AND a.state<3");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND a.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }

        sBuffer.append(" GROUP BY a.msgid ");
        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }

    @Override
    public Page searchUserCnterMsg(MessageCriteria messageCriteria, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select a.msgid, msgtopic, msgtime,likecount, ");
//         sBuffer.append(" c.thename, ");
        sBuffer.append(" d.accessCount, d.replyCount ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append(" LEFT JOIN count d ON a.msgid=d.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND a.userid=? ");
                params.add(userId);
            }
            String username = messageCriteria.getUsername();
            if (username != null && username.trim().length() > 0) {
                sBuffer.append(" AND b.realname LIKE ? ");
                params.add("%" + username + "%");
            }
            int theid = messageCriteria.getTheid();
            if (theid > 0) {
                sBuffer.append(" AND a.theid=? ");
                params.add(theid);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND a.state>=? AND a.state<3");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND a.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }

        sBuffer.append(" GROUP BY a.msgid ");
        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }

    @Override
    public Page queryReply(MessageCriteria messageCriteria, Page page) {

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select m.msgid, msgtopic, msgcontents, msgtime, msgupdatetime,msgip,likecount, m.state, ");
        /*sBuffer.append(" d.accessCount, d.replyCount,max(e.replytime) as replytime ");*/
        sBuffer.append(" c.accessCount, c.replyCount ");
        sBuffer.append(" FROM message m ");
        sBuffer.append(" LEFT JOIN count c ON m.msgid=c.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND m.msgid in (SELECT distinct msgid AS msgid FROM reply where userid=?) ");
                params.add(userId);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND m.state>=? AND m.state<3 ");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND m.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }
        sBuffer.append(" GROUP BY m.msgid ");
/*
		sBuffer.append(" GROUP BY a.msgid, msgtopic, msgcontents, msgtime, msgip, a.state, ");
		sBuffer.append(" a.theid, c.thename,a.userid, username, realname, sex, city, ");
		sBuffer.append(" d.accessCount, d.replyCount ");*/

        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }

    @Override
    public Page queryUserCenterReply(MessageCriteria messageCriteria, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select m.msgid, msgtopic, msgtime,likecount, ");
        sBuffer.append(" c.accessCount, c.replyCount ");
        sBuffer.append(" FROM message m ");
        sBuffer.append(" LEFT JOIN count c ON m.msgid=c.msgid ");
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND m.msgid in (SELECT distinct msgid AS msgid FROM reply where userid=?) ");
                params.add(userId);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND m.state>=? AND m.state<3 ");
                params.add(state);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND m.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }
        sBuffer.append(" GROUP BY m.msgid ");
        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }

        Page resPage = null;
        resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }

    @Override
    public Page searchUserMsg(MessageCriteria messageCriteria, Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" select a.msgid, msgtopic, msgcontents, msgtime,msgupdatetime, msgip,likecount, a.state, ");
        sBuffer.append(" a.theid, c.thename,a.userid, username, realname, sex, city,user_img, ");
        /*sBuffer.append(" d.accessCount, d.replyCount,max(e.replytime) as replytime ");*/
        sBuffer.append(" d.accessCount, d.replyCount ");
        sBuffer.append(" FROM message a ");
        sBuffer.append(" LEFT JOIN user b ON a.userid=b.userid ");
        sBuffer.append(" LEFT JOIN theme c ON a.theid=c.theid ");
        sBuffer.append(" LEFT JOIN count d ON a.msgid=d.msgid ");
        /*		sBuffer.append(" LEFT JOIN reply e ON a.msgid=e.msgid ");*/
        sBuffer.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (messageCriteria != null) {
            // 根据查询条件拼接SQL语句
            int userId = messageCriteria.getUserid();
            if (userId > 0) {
                sBuffer.append(" AND a.userid=? ");
                params.add(userId);
            }
            String username = messageCriteria.getUsername();
            if (username != null && username.trim().length() > 0) {
                sBuffer.append(" AND b.realname LIKE ? ");
                params.add("%" + username + "%");
            }
            int theid = messageCriteria.getTheid();
            if (theid > 0) {
                sBuffer.append(" AND a.theid=? ");
                params.add(theid);
            }
            int state = messageCriteria.getState();
            if (state >= -1) {
                sBuffer.append(" AND a.state>=? AND a.state<? ");
                params.add(state, 3);
            }
            String key = messageCriteria.getKey();
            if (key != null && key.trim().length() > 0) {
                sBuffer.append(" AND a.msgtopic LIKE ? ");
                params.add("%" + key + "%");
            }
        }

        sBuffer.append(" GROUP BY a.msgid ");
/*
		sBuffer.append(" GROUP BY a.msgid, msgtopic, msgcontents, msgtime, msgip, a.state, ");
		sBuffer.append(" a.theid, c.thename,a.userid, username, realname, sex, city, ");
		sBuffer.append(" d.accessCount, d.replyCount ");*/

        //排序规则
        switch (messageCriteria.getOrderRule()) {
            case ORDER_BY_ACCESS_COUNT:
                sBuffer.append(" ORDER BY d.accessCount ");
                break;
            case ORDER_BY_MSG_TIME:
                sBuffer.append(" ORDER BY msgtime ");
                break;
            default:
                break;
        }
        //是否升序或者降序
        if (!messageCriteria.isAsc()) {
            sBuffer.append(" DESC ");
        }
        Page resPage = null;
        resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), params.toArray(), page);
        return resPage;
    }

    @Override
    public Page queryNew(Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT m.msgid,msgtopic,msgcontents,msgtime,msgupdatetime,likecount, ");
        sBuffer.append("realname, ");
        sBuffer.append("  accessCount,replyCount ");
        sBuffer.append(" FROM message m ");
        sBuffer.append(" LEFT JOIN user u ON u.userid=m.userid ");
        sBuffer.append(" LEFT JOIN count c ON c.msgid=m.msgid ");
        sBuffer.append(" WHERE m.state>=0 AND m.state<3 ");
        sBuffer.append(" GROUP BY m.msgid ");
        sBuffer.append(" ORDER BY m.msgtime DESC ");
        Page resPage = null;
        resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), null, page);
        return resPage;
    }

    @Override
    public Page queryHot(Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT m.msgid,msgtopic,msgcontents,msgtime,msgupdatetime,likecount, ");
        sBuffer.append(" realname, ");
        sBuffer.append("  accessCount,replyCount ");
        sBuffer.append(" FROM message m");
        sBuffer.append(" LEFT JOIN user u ON u.userid=m.userid ");
        sBuffer.append(" LEFT JOIN count c ON c.msgid=m.msgid ");
        sBuffer.append(" WHERE m.state>=0 AND m.state<3 ");
        sBuffer.append(" GROUP BY m.msgid ");
        sBuffer.append(" ORDER BY c.accessCount DESC ");
        Page resPage = null;
        resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), null, page);
        return resPage;

    }

    @Override
    public Page queryTheme(Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("SELECT b.msgid, msgtopic, msgcontents, msgtime,b.likecount, b.state, ");
        sBuffer.append("a.thename, ");
        sBuffer.append("realname, ");
        sBuffer.append("d.accessCount, d.replyCount, ");
        sBuffer.append("max(e.replytime) as replytime ");
        sBuffer.append("FROM theme a ");
        sBuffer.append("LEFT JOIN message b ON a.theid = b.theid ");
        sBuffer.append("LEFT JOIN user c ON b.userid = c.userid ");
        sBuffer.append("LEFT JOIN count d ON b.msgid = d.msgid ");
        sBuffer.append("LEFT JOIN reply e on b.msgid = e.msgid ");
        sBuffer.append("WHERE b.state >= 0 AND b.state<3 AND b.msgtime IN ");
        sBuffer.append("( SELECT MAX(msgtime) ");
        sBuffer.append("FROM ");
        sBuffer.append("message f ");
        sBuffer.append("WHERE ");
        sBuffer.append("f.state >= 0 AND f.state<3 AND ");
        sBuffer.append("b.theid = f.theid) ");
        sBuffer.append("GROUP BY ");
        sBuffer.append("b.msgid, msgtopic, msgcontents, msgtime, msgip, b.state, ");
        sBuffer.append("b.theid, a.thename, a.count, ");
        sBuffer.append("b.userid, username, realname, sex, city, user_img, ");
        sBuffer.append("d.accessCount, d.replyCount ");
        sBuffer.append("ORDER BY a.count DESC ");
        Page resPage = null;
        try {
            resPage = dbutil.getQueryPage(MessageInfo.class, sBuffer.toString(), null, page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPage;
    }

    @Override
    public Page queryHomepageNew(Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT m.msgid,msgtopic,msgtime, ");
        sBuffer.append("  accessCount ");
        sBuffer.append(" FROM message m ");
        sBuffer.append(" LEFT JOIN user u ON u.userid=m.userid ");
        sBuffer.append(" LEFT JOIN count c ON c.msgid=m.msgid ");
        sBuffer.append(" WHERE m.state>=0 AND m.state<3 ");
        sBuffer.append(" GROUP BY m.msgid ");
        sBuffer.append(" ORDER BY m.msgtime DESC ");
        Page resPage = null;
        resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), null, page);
        return resPage;
    }

    @Override
    public Page queryHomepageHot(Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT m.msgid,msgtopic,msgtime, ");
        sBuffer.append("  accessCount ");
        sBuffer.append(" FROM message m");
        sBuffer.append(" LEFT JOIN user u ON u.userid=m.userid ");
        sBuffer.append(" LEFT JOIN count c ON c.msgid=m.msgid ");
        sBuffer.append(" WHERE m.state>=0 AND m.state<3 ");
        sBuffer.append(" GROUP BY m.msgid ");
        sBuffer.append(" ORDER BY c.accessCount DESC ");
        Page resPage = null;
        resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), null, page);
        return resPage;
    }

    @Override
    public Page queryHomepageTheme(Page page) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("SELECT b.msgid, msgtopic, msgtime, ");
        sBuffer.append("a.thename, ");
        sBuffer.append("d.accessCount, ");
        sBuffer.append("max(e.replytime) as replytime ");
        sBuffer.append("FROM theme a ");
        sBuffer.append("LEFT JOIN message b ON a.theid = b.theid ");
        sBuffer.append("LEFT JOIN user c ON b.userid = c.userid ");
        sBuffer.append("LEFT JOIN count d ON b.msgid = d.msgid ");
        sBuffer.append("LEFT JOIN reply e on b.msgid = e.msgid ");
        sBuffer.append("WHERE b.state >= 0 AND b.state<3 AND b.msgtime IN ");
        sBuffer.append("( SELECT MAX(msgtime) ");
        sBuffer.append("FROM ");
        sBuffer.append("message f ");
        sBuffer.append("WHERE ");
        sBuffer.append("f.state >= 0 AND f.state<3 AND ");
        sBuffer.append("b.theid = f.theid) ");
        sBuffer.append("GROUP BY ");
        sBuffer.append("b.msgid, msgtopic, msgcontents, msgtime, msgip, b.state, ");
        sBuffer.append("b.theid, a.thename, a.count, ");
        sBuffer.append("b.userid, username, realname, sex, city, user_img, ");
        sBuffer.append("d.accessCount, d.replyCount ");
        sBuffer.append("ORDER BY a.count DESC ");
        Page resPage = null;
        try {
            resPage = dbutil.getQueryPage(ShortMessageInfo.class, sBuffer.toString(), null, page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPage;
    }

    @Override
    public long queryCountByDate(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) AS count FROM message WHERE msgtime > ? AND msgtime < ?";
        Object[] params = {startDate+" 00:00:00", endDate+" 00:00:00"};
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
        String sql = "SELECT COUNT(*) AS count FROM message";

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

    public long queryMsgCount(int state) {
        String sql = "SELECT COUNT(*) AS count FROM message where state=?";
        Object[] params = {state};
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
    public int queryMsgReplyident(int msgid){
        String sql = "SELECT replyident AS count FROM message where msgid=?";
        Object[] params = {msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            int count = (int) map.get("count");
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2;
    }

    public long queryReplyCount(int userid) {
        String sql = "SELECT COUNT(*) AS count FROM reply where userid=? and reply.msgid in (SELECT msgid from message where state >=0 and state <3)";
        Object[] params = {userid};
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
    public long queryDistinctReplyCount(int userid) {
        String sql = "SELECT COUNT(distinct r.msgid) AS count FROM reply r LEFT JOIN message m ON m.msgid=r.msgid  where r.userid=? and (m.state>=0 and m.state<3)";
        Object[] params = {userid};
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
    public int msgCountBytheid(int theid) {
        String sql = "select count(*) as count from message where theid =?";
        Map<String, Object> map = null;
        int count = 0;
        try {
            map = dbutil.getObject(sql, new Object[]{theid});
            count = Integer.parseInt(map.get("count").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
