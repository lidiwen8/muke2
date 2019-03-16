package com.muke.dao;

import java.util.Date;
import java.util.List;

import com.muke.pojo.Reply;
import com.muke.util.Page;

public interface IReplyDao {
    /**
     * 回帖
     *
     * @param reply 回复内容
     * @return
     */
    int replyMsg(Reply reply);

    /**
     * 根据帖子ID查询回复内容
     *
     * @param msgid 帖子ID
     * @param page
     * @return
     */
    Page queryBymsgid(int msgid, Page page);

    long queryReplyConutBymsgid(int msgid);

    /**
     * 根据时间查询回帖数量
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    long queryCountByDate(Date startDate, Date endDate);

    long queryCountBy();//回复总数

    long queryVisit();//访问总量

    Reply queryid(int replyid);

    int updateReply(Reply reply);//更新reply信息

    int updateReply2(Reply reply);

    List getReplylikeUserid(int replyid);

    int updateReplylike(Reply reply);//回复点赞

    int updateReplyState(int replyid,int rstate);//更新回复信息的状态

    int deleteReply(int replyid);//删除回复信息,注意此时要重新统计帖子的回复总数

    List getReplyUseremail(int msgid,int userid);//得到回复帖子用户已激活的邮箱(不包括楼主本人)

    List AdmingetReplyUseremail(int msgid);

    List AdmingetMsgUseremail(int msgid);
}
