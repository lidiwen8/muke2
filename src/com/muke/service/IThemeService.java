package com.muke.service;

import java.util.List;

import com.muke.pojo.Theme;
import com.muke.util.Page;

/**
 * 主题业务层接口
 * 
 * @author Administrator
 *
 */
public interface IThemeService {
	int add(Theme theme);

	int delete(int theid);
    boolean isExist(String thename);
	List getAll();

	Page query(String key, Page page);

}
