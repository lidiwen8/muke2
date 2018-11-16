package com.muke.service.impl;

import java.util.Date;

import com.muke.dao.ICountDao;
import com.muke.dao.IMessageDao;
import com.muke.dao.IReplyDao;
import com.muke.dao.impl.CountDaoImpl;
import com.muke.dao.impl.MessageDaoImpl;
import com.muke.dao.impl.ReplyDaoImpl;
import com.muke.pojo.Message;
import com.muke.pojo.MessageCriteria;
import com.muke.pojo.MessageInfo;
import com.muke.pojo.Reply;
import com.muke.service.IMessageService;
import com.muke.util.Page;

public class MessageServiceImpl implements IMessageService{
		private IMessageDao messagedao=new MessageDaoImpl();
		private ICountDao countdao=new CountDaoImpl();
		private IReplyDao replydao=new ReplyDaoImpl();
	@Override
	public int addMsg(Message message) {
		return messagedao.add(message);
	}

	@Override
	public int replyMsg(Reply reply) {
		return replydao.replyMsg(reply);
	}

	@Override
	public MessageInfo getMsg(int msgid) {
		//增加浏览量
		countdao.updateAccessCount(msgid);
		return messagedao.get(msgid);
	}

	@Override
	public Page getReply(int msgid, Page page) {
		return replydao.queryBymsgid(msgid, page);
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
	public int deleteMsg(int msgid) {
		return messagedao.updateState(msgid, -1);
	}

	@Override
	public int restoreMsg(int msgid) {
		return messagedao.updateState(msgid, 0);
	}

	@Override
	public int updateMsg(MessageInfo messageInfo) {
		return  messagedao.updateMsg(messageInfo);
	}

	@Override
	public Page search(MessageCriteria messageCriteria, Page page) {
		return messagedao.query(messageCriteria, page);
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
	public long queryReplyCountByDate(Date startDate, Date endDate) {
		return replydao.queryCountByDate(startDate, endDate);
	}
	@Override
	public long queryReplyCountBy() {
		return replydao.queryCountBy();
	}
	@Override
	public long queryVisit() {
		return replydao.queryVisit();
	}
	@Override
	public Reply queryid(int replyid) {
		return replydao.queryid(replyid);
	}
}
