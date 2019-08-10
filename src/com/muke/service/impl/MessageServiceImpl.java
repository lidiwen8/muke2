package com.muke.service.impl;

import java.util.Date;
import java.util.List;

import com.muke.dao.ICountDao;
import com.muke.dao.IMessageDao;
import com.muke.dao.impl.CountDaoImpl;
import com.muke.dao.impl.MessageDaoImpl;
import com.muke.pojo.Message;
import com.muke.pojo.MessageCriteria;
import com.muke.pojo.MessageInfo;
import com.muke.service.IMessageService;
import com.muke.util.Page;

public class MessageServiceImpl implements IMessageService {
    private IMessageDao messagedao = new MessageDaoImpl();
    private ICountDao countdao = new CountDaoImpl();

    @Override
    public int addMsg(Message message) {
        return messagedao.add(message);
    }

    @Override
    public MessageInfo getMsg(int msgid) {
        //增加浏览量
        countdao.updateAccessCount(msgid);
        return messagedao.get(msgid);
    }

    @Override
    public MessageInfo getMsgNoincreaseCount(int msgid) {
        return messagedao.get(msgid);
    }


    @Override
    public Page queryNew(Page page) {
        return messagedao.queryNew(page);
    }

    @Override
    public Page queryHot(Page page) {
        return messagedao.queryHot(page);
    }

    @Override
    public Page queryTheme(Page page) {
        return messagedao.queryTheme(page);
    }

    @Override
    public Page queryHomepageNew(Page page) {
        return messagedao.queryHomepageNew(page);
    }

    @Override
    public Page queryHomepageHot(Page page) {
        return messagedao.queryHomepageHot(page);
    }

    @Override
    public Page queryHomepageTheme(Page page) {
        return messagedao.queryHomepageTheme(page);
    }


    @Override
    public int deleteMsg(int msgid) {
        return messagedao.updateState(msgid, -1);
    }

    @Override
    public int restoreMsg(int msgid) {
        return messagedao.updateState(msgid, 0);
    }

    @Override
    public int userdeleteMsg(int msgid) {
        return messagedao.updateState(msgid, 3);
    }

    @Override
    public int queryMsgState(int msgid) {
        return messagedao.queryMsgState(msgid);
    }

    @Override
    public int updateMsg(MessageInfo messageInfo) {
        return messagedao.updateMsg(messageInfo);
    }

    @Override
    public Page search(MessageCriteria messageCriteria, Page page) {
        return messagedao.query(messageCriteria, page);
    }

    @Override
    public Page searchUserMyMsg(MessageCriteria messageCriteria, Page page) {
        return messagedao.searchUserMyMsg(messageCriteria, page);
    }

    @Override
    public Page searchUserAllMyMsg(MessageCriteria messageCriteria, Page page){
        return messagedao.searchUserAllMyMsg(messageCriteria, page);
    }

    @Override
    public Page search1(MessageCriteria messageCriteria, Page page) {
        return messagedao.query1(messageCriteria, page);
    }

    @Override
    public Page searchUserCnterMsg(MessageCriteria messageCriteria, Page page) {
        return messagedao.searchUserCnterMsg(messageCriteria, page);
    }

    @Override
    public Page searchUserMsg(MessageCriteria messageCriteria, Page page) {
        return messagedao.searchUserMsg(messageCriteria, page);
    }

    @Override
    public long queryMsgCountByDate(Date startDate, Date endDate) {
        return messagedao.queryCountByDate(startDate, endDate);
    }

    @Override
    public long queryMsgCountBy() {
        return messagedao.queryCountBy();
    }

    @Override
    public int queryMsgReplyident(int msgid){
        return messagedao.queryMsgReplyident(msgid);
    }


    @Override
    public long queryMsgCount(int state) {
        return messagedao.queryMsgCount(state);
    }

    @Override
    public long queryReplyCount(int userid) {
        return messagedao.queryReplyCount(userid);
    }

    @Override
    public long queryDistinctReplyCount(int userid) {
        return messagedao.queryDistinctReplyCount(userid);
    }

    @Override
    public Page queryReply(MessageCriteria messageCriteria, Page page) {
        return messagedao.queryReply(messageCriteria, page);
    }

    @Override
    public Page queryUserCenterReply(MessageCriteria messageCriteria, Page page) {
        return messagedao.queryUserCenterReply(messageCriteria, page);
    }

    @Override
    public int msgCountBytheid(int theid) {
        return messagedao.msgCountBytheid(theid);
    }

    @Override
    public int delete(int msgid) {
        return messagedao.delete(msgid);
    }


    @Override
    public List getMessagelikeUserid(int msgid) {
        return messagedao.getMessagelikeUserid(msgid);
    }

    @Override
    public int updateMessagelike(Message message) {
        return messagedao.updateMessagelike(message);
    }

    @Override
    public int upadateReplyident(int msgid, int replyident) {
        return messagedao.upadateReplyident(msgid, replyident);
    }
}
