package com.muke.service.impl;

import java.util.List;

import com.muke.dao.IThemeDao;
import com.muke.dao.impl.ThemeDaoImpl;
import com.muke.pojo.Theme;
import com.muke.service.IThemeService;
import com.muke.util.Page;

public class ThemeServiceImpl implements IThemeService {
    private IThemeDao themedao = new ThemeDaoImpl();

    @Override
    public int add(Theme theme) {
        return themedao.add(theme);
    }

    @Override
    public int delete(int theid) {
        return themedao.delete(theid);
    }

    @Override
    public int update(Theme theme) {
        return themedao.update(theme);
    }

    @Override
    public int updateCount(int count,int theid){
        return themedao.updateCount(count,theid);
    }


    @Override
    public List getAll() {
        return themedao.getAll();
    }

    @Override
    public Theme queryThemeBytheid(int theid){
        return themedao.queryThemeBytheid(theid);
    }

    @Override
    public boolean isExist(String thename) {
        return themedao.isExist(thename);
    }

    @Override
    public boolean isExistByid(int theid) {
        return themedao.isExistByid(theid);
    }

    @Override
    public boolean queryMessageBytheid(int theid) {
        return themedao.queryMessageBytheid(theid);
    }

    @Override
    public Page query(String key, Page page) {
        return themedao.query(key, page);
    }


}
