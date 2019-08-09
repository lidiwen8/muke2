package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.dao.impl.ThemeDaoImpl;
import com.muke.pojo.Admin;
import com.muke.pojo.Theme;
import com.muke.service.IThemeService;
import com.muke.service.impl.ThemeServiceImpl;
import com.muke.util.Page;

/**
 * Servlet implementation class ThemeServlet
 */
/*@WebServlet(name = "adminThemeServlet", urlPatterns = { "/adminThemeServlet" })*/
@WebServlet("/admin/adminThemeServlet")
public class AdminThemeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IThemeService themeService = new ThemeServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        try {
            //使用反射定义方法
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class,
                    HttpServletResponse.class);
            //调用方法
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //搜索主题信息
    private void searchTheme(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");//关键字
        String pageNum = request.getParameter("pageNum");//当前页
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));//设置当前页
        page = themeService.query(key, page);//根据关键字和分页信息查询主题信息
        Gson gson = new Gson();
        String json = gson.toJson(page);//将page转化为json
        //{"curPage":1,totalPage:5,"data":[{"theid":1,"thename":"java"},{"theid":1,"thename":"java"}]}
        response.getWriter().println("{\"theme\":" + json + "}");
    }

    //添加信息
    private void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String thename = request.getParameter("thename");
        if (themeService.isExist(thename) == false) {
            Theme theme = new Theme();
            theme.setThename(thename);
            int result = themeService.add(theme);
            if (result > 0) {
                //发送更新信号
                UserMessageServlet.sendMessage("add");
                response.getWriter().print("{\"res\":1,\"info\":\"添加主题成功！\"}");
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"添加主题失败！\"}");
            }
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"该主题已存在,添加主题失败！\"}");
        }
    }

    //编辑主题信息
    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String thename = request.getParameter("thename");
        int theid = Integer.parseInt(request.getParameter("theid"));
        if (themeService.isExistByid(theid) == true) {
            if (themeService.queryThemeBytheid(theid).getThename().equals(thename)) {
                response.getWriter().print("{\"res\":-1,\"info\":\"主题信息未做任何修改,没有更新哟！\"}");
                return;
            }
            if (themeService.isExist(thename) == false) {
                Theme theme = new Theme();
                theme.setThename(thename);
                theme.setTheid(theid);
                int result = themeService.update(theme);
                if (result > 0) {
                    //发送更新信号
                    UserMessageServlet.sendMessage("editTheme"+theid);
                    response.getWriter().print("{\"res\":1,\"info\":\"编辑主题成功！\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"编辑主题失败！\"}");
                }
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"该主题已存在,添加主题失败！\"}");
            }
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"该主题id不存在,编辑主题信息失败！\"}");
        }
    }

    private void getThemeMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int theid = Integer.parseInt(request.getParameter("theid"));
        Theme theme = themeService.queryThemeBytheid(theid);
        if (theme != null) {
            Gson gson = new GsonBuilder().setDateFormat("MM-dd HH:mm").create();
            String dataJSON = gson.toJson(theme);
            response.getWriter().print("{\"res\": 1, \"theme\":" + dataJSON + "}");
//            response.getWriter().print("{\"res\":1,\"theid\":"+theme.getTheid()+",\"thename\":'"+theme.getThename()+"'}");
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"获取主题信息失败！\"}");
        }
    }

    //包含该主题信息的帖子不存在，删除主题信息
    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String theid = request.getParameter("theid");
        //删除主题信息前，要判断该是否包含该主题信息的帖子
        if (themeService.queryMessageBytheid(Integer.parseInt(theid)) == false) {
            int result = themeService.delete(Integer.parseInt(theid));
            if (result > 0) {
                //发送更新信号
                UserMessageServlet.sendMessage("deleteTheme"+theid);
                response.getWriter().print("{\"res\":1,\"info\":\"删除主题成功！\"}");
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"删除主题失败！\"}");
            }
        } else {
            response.getWriter().print("{\"res\":2,\"info\":\"该主题信息存在已发布的帖子，删除后不可恢复，请确认是否要删除?\"}");
        }
    }

    //包含该主题信息的帖子存在，删除主题信息
    private void delete1(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String theid = request.getParameter("theid");
        int result = themeService.delete(Integer.parseInt(theid));
        if (result > 0) {
            //发送更新信号
            UserMessageServlet.sendMessage("deleteTheme"+theid);
            response.getWriter().print("{\"res\":1,\"info\":\"删除主题成功！\"}");
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"删除主题失败！\"}");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
