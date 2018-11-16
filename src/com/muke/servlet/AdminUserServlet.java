package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.Admin;
import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.service.IUserService;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.Page;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/adminUserServlet")
public class AdminUserServlet extends HttpServlet {
    IUserService iUserService = new UserServiceImpl();
    /**
	 * 
	 */
	private static final long serialVersionUID = 4122206234828080374L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public AdminUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		try {
			//使用反射定义方法
			Method method=getClass().getDeclaredMethod(action, HttpServletRequest.class,
					HttpServletResponse.class);
			//调用方法
			method.invoke(this, request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if(action.equals("getUser")) {
//			getUser(request, response);
//		} else if(action.equals("deleteUser")) {
//			deleteUser(request, response);
//		} else if("restoreUser".equals(action)){
//			restoreUser(request, response);
//		}else if("getAdvise".equals(action)){
//			getAdvise(request, response);
//		}else if("deleteAdvise".equals(action)){
//			deleteAdvise(request, response);
//		}else if("restoreAdvise".equals(action)){
//			restoreAdvise(request,response);
//		}else if("getAdvisedetails".equals(action)){
//			getAdvisedetails(request,response);
//		}else if("delete".equals(action)){
//			delete(request,response);
//		}
	}

	private void restoreUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userid = request.getParameter("userid");
		if (userid == null || userid.equals("")){
			userid = "-1";
		}
		
		int res = iUserService.restoreUser(Integer.parseInt(userid));
		
		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"恢复失败\"}");
		}
	}

	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userid = request.getParameter("userid");
		if (userid == null || userid.equals("")){
			userid = "-1";
		}
		
		int res = iUserService.deleteUser(Integer.parseInt(userid));
		
		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"禁用成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"禁用失败\"}");
		}
	}
	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userid = request.getParameter("userid");
		if (userid == null || userid.equals("")){
			userid = "-1";
		}
		int res = iUserService.delete(Integer.parseInt(userid));
		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"删除失败\"}");
		}
	}
	private void getAdvise(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//		String username = request.getParameter("id");
		try {
			Admin admin = (Admin) request.getSession().getAttribute("admin");
		}catch (Exception e){
			response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
			return;
		}
		String pageNum = request.getParameter("pageNum");
		if (pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}
		Page page = new Page();
		page.setCurPage(Integer.parseInt(pageNum));
		Page resPage = iUserService.searchAdvice(page);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
		String dataJSON = gson.toJson(resPage);
		response.getWriter().print("{\"res\": 1, \"data\":"+dataJSON+"}");
	}

	private void getAdvisedetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");//帖子Id
		Advise advise = new Advise();
		advise = iUserService.getAdvisedetails(Integer.parseInt(id));
		if (advise.getStates() == 0) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json = gson.toJson(advise);
			response.getWriter().print("{\"res\":1,\"advise\":" + json + "}");
		}else{
			response.getWriter().print("{\"res\": -1, \"info\":\"已删除！\"}");
			return;
		}
	}

	private void deleteAdvise(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		if (id == null || id.equals("")){
			id = "-1";
		}

		int res = iUserService.deleteAdvise(Integer.parseInt(id));

		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"删除失败\"}");
		}
	}
	private void restoreAdvise(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		if (id == null || id.equals("")){
			id = "-1";
		}

		int res = iUserService.restoreAdvise(Integer.parseInt(id));
		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"恢复失败\"}");
		}
	}

	private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			Admin admin = (Admin) request.getSession().getAttribute("admin");
			if(admin.equals(null)){
				response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
				return;
			}
		String username = request.getParameter("username");
		String pageNum = request.getParameter("pageNum");

		if (pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}

		Page page = new Page();
		page.setCurPage(Integer.parseInt(pageNum));

		Page resPage = iUserService.searchByName(username, page);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		String dataJSON = gson.toJson(resPage);
		//System.out.println(dataJSON);

		response.getWriter().print("{\"res\": 1, \"data\":"+dataJSON+"}");
		}catch (NullPointerException e){
			response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
			return;
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
