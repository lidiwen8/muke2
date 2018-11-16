package com.muke.dao;

import java.util.Date;

import com.muke.pojo.Reply;
import com.muke.util.Page;

public interface IReplyDao {
	/**
	 * 回帖
	 * @param reply 回复内容
	 * @return
	 */
	int replyMsg(Reply reply);
	
	/**
	 * 根据帖子ID查询回复内容
	 * @param msgid 帖子ID
	 * @param page
	 * @return
	 */
	Page queryBymsgid(int msgid,Page page);
	
	/**
	 * 根据时间查询回帖数量
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return 
	 */
	long queryCountByDate(Date startDate,Date endDate);

	long queryCountBy();//回复总数
	long queryVisit();//访问总量
	Reply queryid(int replyid);
}
