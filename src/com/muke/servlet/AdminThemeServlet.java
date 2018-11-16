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
@WebServlet("/adminThemeServlet")
public class AdminThemeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Theme theme=new Theme();
	private IThemeService themeService=new ThemeServiceImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String action=request.getParameter("action");
		try {
			//使用反射定义方法
			Method method=getClass().getDeclaredMethod(action, HttpServletRequest.class,
					HttpServletResponse.class);
			//调用方法
			method.invoke(this, request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//搜索主题信息
	private void searchTheme(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Admin admin = (Admin) request.getSession().getAttribute("admin");
		if(admin.equals(null)){
			response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
			return;
		}
		String key=request.getParameter("key");//关键字
		String pageNum=request.getParameter("pageNum");//当前页
		if(pageNum==null|| pageNum.equals("")){
			pageNum="1";
		}
		Page page=new Page();
		page.setCurPage(Integer.parseInt(pageNum));//设置当前页
		page=themeService.query(key, page);//根据关键字和分页信息查询主题信息
		Gson gson=new Gson();
		String json=gson.toJson(page);//将page转化为json
		//{"curPage":1,totalPage:5,"data":[{"theid":1,"thename":"java"},{"theid":1,"thename":"java"}]}
		response.getWriter().println("{\"theme\":"+json+"}");
		}catch (NullPointerException e){
		response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
		return;
	}
	}
	
	//添加信息
	private void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String thename=request.getParameter("thename");
		if(themeService.isExist(thename)==false) {
			Theme theme = new Theme();
			theme.setThename(thename);
			int result = themeService.add(theme);
			if (result > 0) {
				response.getWriter().print("{\"res\":1,\"info\":\"添加主题成功！\"}");
			} else {
				response.getWriter().print("{\"res\":-1,\"info\":\"添加主题失败！\"}");
			}
		}else{
			response.getWriter().print("{\"res\":-1,\"info\":\"该主题已存在,添加主题失败！\"}");
		}
	}
	
	//删除信息
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String theid=request.getParameter("theid");
		int result = themeService.delete(Integer.parseInt(theid));
		if (result > 0) {
			response.getWriter().print("{\"res\":1,\"info\":\"删除主题成功！\"}");
		} else {
			response.getWriter().print("{\"res\":-1,\"info\":\"删除主题失败！\"}");
		}
	}
	
	//获取全部主题信息
		private void getAllTheme(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			List<Theme> list=themeService.getAll();
			Gson gson=new Gson();
			String json=gson.toJson(list);
			response.getWriter().print("{\"theme\":"+json+"}");
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
