package com.muke.service;

import java.util.Date;
import java.util.List;

import com.muke.pojo.Message;
import com.muke.pojo.MessageCriteria;
import com.muke.pojo.MessageInfo;
import com.muke.pojo.Reply;
import com.muke.util.Page;

/**
 * 帖子信息业务接口层
 *
 * @author Administrator
 */
public interface IMessageService {
    /**
     * 发帖
     *
     * @param message
     * @return
     */
    int addMsg(Message message);

    /**
     * 根据帖子ID查询帖子信息
     *
     * @param msgid
     * @return
     */
    MessageInfo getMsg(int msgid);

    /**
     * 根据帖子ID查询帖子信息,单独查询不增加浏览量
     *
     * @param msgid
     * @return
     */
    MessageInfo getMsgNoincreaseCount(int msgid);

    /**
     * 查询最新
     *
     * @param page
     * @return
     */
    Page queryNew(Page page);

    Page queryHot(Page page);

    Page queryTheme(Page page);


    int queryMsgState(int msgid);

    int deleteMsg(int msgid);//假删除帖子

    int restoreMsg(int msgid);

    int userdeleteMsg(int msgid);

    int updateMsg(MessageInfo messageInfo);

    Page search(MessageCriteria messageCriteria, Page page);

    Page search1(MessageCriteria messageCriteria, Page page);

    Page searchUserMsg(MessageCriteria messageCriteria, Page page);

    Page queryReply(MessageCriteria messageCriteria, Page page);

    long queryMsgCountByDate(Date startDate, Date endDate);    // 根据时间查发贴数量

    long queryMsgCountBy();//发帖总数

    long queryReplyCount(int userid);//根据userid查询回复总数

    long queryDistinctReplyCount(int userid);//根据userid查询回过帖的帖子数目，同一个帖子回复多次当成一次，帖子msgid不重复

    long queryMsgCount(int state);//根据帖子状态可以查询当前帖子数，例如查询精品贴等等

    int msgCountBytheid(int theid);//统计主题帖数,根据主题theid

    int delete(int msgid);//真删除帖子

    int updateMessagelike(Message message);//帖子点赞

    List getMessagelikeUserid(int msgid);

}
