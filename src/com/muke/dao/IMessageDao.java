package com.muke.dao;

import java.util.Date;

import com.muke.pojo.Message;
import com.muke.pojo.MessageCriteria;
import com.muke.pojo.MessageInfo;
import com.muke.util.Page;

/**
* 帖子信息数据访问接口
* @author Administrator
*
*/
public interface IMessageDao {
	/**
	 * 添加帖子
	 * @param msg 帖子信息
	 * @return
	 */
	int add(Message msg);
	
	/**
	 * 删除贴子
	 * @param msgid 帖子ID
	 * @return
	 */
	int delete(int msgid);
	
	/**
	 * 更新帖子
	 * @param msg 帖子信息
	 * @return
	 */
	int updateMsg(MessageInfo msgInfo);
	
	/**
	 * 更新状态
	 * @param msgid 帖子ID
 	 * @param state 状态
	 * @return
	 */
	int updateState(int msgid,int state);
	
	/**
	 * 查询指定帖子信息
	 * @param msgid 帖子ID
	 * @return
	 */
	MessageInfo get(int msgid);
	
	/**
	 * 多条件查询帖子信息
	 * @param messageCriteria 复合条件
	 * @param page 分页信息
	 * @return 查询结果
	 */
	Page query(MessageCriteria messageCriteria,Page page);
	
	/**
	 * 查询最新的帖子信息
	 * @param page 分页信息
	 * @return
	 */
	Page queryNew(Page page);
	
	/**
	 * 查询最热的帖子信息
	 * @param page
	 * @return
	 */
	Page queryHot(Page page);
	
	/**
	 * 查询最热主题信息
	 * @param page
	 * @return
	 */
	Page queryTheme(Page page);
	
	/**
	 * 根据时间查询帖子数量
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	long queryCountByDate(Date startDate,Date endDate);
	long queryCountBy();
}
