package com.muke.dao;

/**
 * 帖子统计数据访问层接口
 *
 * @author Administrator
 */
public interface ICountDao {

    /**
     * 更新帖子访问量
     *
     * @param msgid
     * @return
     */
    int updateAccessCount(int msgid);

    /**
     * 更新帖子的回复量
     *
     * @param msgid
     * @return
     */
    int updateReplyCount(int msgid);

    int getReplyCount(int msgid);
}
