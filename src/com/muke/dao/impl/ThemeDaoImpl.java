package com.muke.dao.impl;

import java.util.List;
import java.util.Map;

import com.muke.dao.IThemeDao;
import com.muke.pojo.Theme;
import com.muke.util.DBUtil;
import com.muke.util.Page;

public class ThemeDaoImpl implements IThemeDao {
    DBUtil dbutil = new DBUtil();

    @Override
    public int add(Theme theme) {
        String sql = "INSERT INTO `theme` ( `thename`) VALUES (?)";
        Object[] params = {theme.getThename()};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int delete(int theid) {
        String sql = "DELETE FROM theme WHERE theid=?";
        Object[] params = {theid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int update(Theme theme) {
        String sql = "update theme set thename=? where theid=?";
        Object[] params = {theme.getThename(), theme.getTheid()};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int updateCount(int count,int theid){
        String sql = "update theme set count=? where theid=?";
        Object[] params = {count,theid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }


    @Override
    public List getAll() {
        String sql = "SELECT * FROM theme";
        List list = null;
        try {
            list = dbutil.getQueryList(Theme.class, sql, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Page query(String key, Page page) {
        String sql = "SELECT * FROM theme WHERE thename LIKE ?";
        Object[] params = {"%" + key + "%"};
        Page respage = null;
        respage = dbutil.getQueryPage(Theme.class, sql, params, page);
        return respage;
    }

    @Override
    public boolean isExist(String thename) {
        String sql = "select count(*) as count from theme where binary thename =?";
        boolean result = false;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{thename});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = true;//主题名已存在
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isExistByid(int theid) {
        String sql = "select count(*) as count from theme where theid =?";
        boolean result = false;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{theid});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = true;//主题存在
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean queryMessageBytheid(int theid) {
        String sql = "select count(*) as count from message where theid =?";
        boolean result = false;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{theid});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = true;//包含该主题的帖子存在
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Theme queryThemeBytheid(int theid){
        String sql = "SELECT * FROM theme WHERE theid = ?";
        Object[] params = {theid};
        Theme theme = null;
        try {
            theme = (Theme) dbutil.getObject(Theme.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return theme;
    }

//	public Page query1(String thename, Page page) {
//		String sql="SELECT * FROM theme WHERE thename=?";
//		Object[] params={"'"+thename+"'"};
//		Page respage=null;
//		respage=dbutil.getQueryPage(Theme.class, sql, params, page);
//		return respage;
//	}

}
