package com.muke.dao;

import java.util.List;

import com.muke.pojo.Theme;
import com.muke.util.Page;

/**
 * 主题数据访问接口
 * @author Administrator
 *
 */
public interface IThemeDao {
	
	/**
	 * 新增主题
	 * @param theme
	 * @return
	 */
	int add(Theme theme);
	
	/**
	 * 删除主题
	 * @param theid
	 * @return
	 */
	int delete(int theid);

	/**
	 * 更新主题
	 * @param theme
	 * @return
	 */
	int update(Theme theme);
	/**
	 * 查询全部主题
	 * @return
	 */
	List getAll();
	
	/**
	 * 根据关键字查询主题信息
	 * @param key
	 * @param page
	 * @return
	 */
	Page query(String key,Page page);

	boolean isExist( String thename);

	boolean isExistByid(int theid);

	boolean queryMessageBytheid(int theid);

	Theme queryThemeBytheid(int theid);

	int updateCount(int count,int theid);
//	Page query1(String key, Page page);
}
