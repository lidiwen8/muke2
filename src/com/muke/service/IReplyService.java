package com.muke.service;

import com.muke.pojo.Reply;
import com.muke.util.Page;

import java.util.Date;
import java.util.List;

public interface IReplyService {

    /**
     * 回帖
     *
     * @param reply
     * @return
     */
    int replyMsg(Reply reply);

    /**
     * 根据帖子ID查询回复信息
     *
     * @param msgid 帖子ID
     * @param page
     * @return
     */
    Page getReply(int msgid, Page page);

    Page getAuthorReply(int msgid,int userid, Page page);//只得到楼主回复

    //根据帖子id查询总回复数
    long queryReplyConutBymsgid(int msgid);

    long queryReplyCountByDate(Date startDate, Date endDate);    // 根据时间回贴数量

    long queryReplyCountBy();//回复总数

    long queryVisit();//访问总量

    Reply queryid(int replyid);

    int updateReply(Reply reply);//用户更新reply信息

    int updateReply2(Reply reply);//管理员更新reply信息

    int updateReplylike(Reply reply);//回复点赞

    List getReplylikeUserid(int replyid);

    int updateReplyState(int replyid, int rstate);//更新回复信息的状态

    int deleteReply(int replyid);//删除回复信息,注意此时要重新统计帖子的回复总数

    List getReplyUseremail(int msgid,int userid);//得到回复帖子用户已激活的邮箱(不包括楼主本人)

    List AdmingetReplyUseremail(int msgid);//管理员得到帖子的所有用户(包括楼主)

    List AdmingetMsgUseremail(int msgid);//管理员得到楼主已激活的邮箱



}
