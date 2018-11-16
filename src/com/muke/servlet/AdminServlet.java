package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.muke.pojo.Admin;
import com.muke.service.IAdminService;
import com.muke.service.impl.AdminServiceImpl;
import com.muke.util.Md5Encrypt;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/adminServlet")
public class AdminServlet extends HttpServlet {
	private IAdminService adminService = new AdminServiceImpl();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			String action= request.getParameter("action");
			//which  if else  或者反射都可以,现在用反射
		try {
			Method method=getClass().getDeclaredMethod(action,HttpServletRequest.class,HttpServletResponse.class);
			//调用方法
			method.invoke(this, request,response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//登录
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String username=request.getParameter("username");
		 String password=request.getParameter("password");
		String rememberme = request.getParameter("rememberme");
		String password1=password;
		 password= Md5Encrypt.MD5(password);
		 Admin admin=adminService.login(username, password);
		 if(admin == null){
			 //用户名或者密码错误  json方式输出
			 response.getWriter().print("{\"res\":-1,\"info\":\"用户名或密码错误\"}");
			 return;
		 }else{
			 //登录成功
			 //用session和cookie保存数据
			 if(rememberme!=null) {
				  //创建两个Cookie对象
				 Cookie nameCookie = new Cookie("username", username);
				 //设置Cookie的有效期为3天
				 nameCookie.setMaxAge(60 * 60 * 24 * 3);
				 Cookie pwdCookie = new Cookie("password", password1);
				 pwdCookie.setMaxAge(60 * 60 * 24 * 3);
				 response.addCookie(nameCookie);
				 response.addCookie(pwdCookie);
			 }
			request.getSession().setAttribute("admin", admin);
			 response.getWriter().print("{\"res\":1,\"info\":\"登录成功\"}");
				
		 }
		 
	}

	//退出
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  HttpSession session=request.getSession();
		  session.removeAttribute("admin");
		response.getWriter().println("退出成功！");
	}

	//修改密码
	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取session中的admin
		try {
			Admin admin = (Admin) request.getSession().getAttribute("admin");
			String name = admin.getName();
			String pwd = admin.getPwd();
			String oldpassword = request.getParameter("oldpassword");
			String newpassword = request.getParameter("newpassword");
			String newpassword2 = request.getParameter("newpassword2");
			if (oldpassword.equals(null) || oldpassword.equals("")) {  //输入旧密码为空
				response.getWriter().print("{\"res\":2, \"info\":\"旧密码输入为空，修改失败！\"}");
				return;
			}
			//验证session中的密码是否与输入的旧密码相同
			oldpassword= Md5Encrypt.MD5(oldpassword);
			if (pwd.equals(oldpassword)) {
				if (newpassword.equals(null) || newpassword.equals("")) {
					response.getWriter().print("{\"res\":3, \"info\":\"新密码输入为空，修改失败！\"}");
					return;
				} else {
					if (!newpassword.equals(newpassword2)) {
						response.getWriter().print("{\"res\":4, \"info\":\"确认密码与新密码输入不一致，修改失败\"}");
						return;
					} else {
						admin.setName(name);
						newpassword= Md5Encrypt.MD5(newpassword);
						admin.setPwd(newpassword);
						int res = adminService.updatepwd(admin);
						//将session中的pwd更换掉
						admin.setName(name);
						admin.setPwd(newpassword);
						request.getSession().setAttribute("admin", admin);
						response.getWriter().print("{\"res\": 1, \"info\":\"修改成功！\"}");
					}
				}
			}else{
				response.getWriter().print("{\"res\": 6, \"info\":\"旧密码输入不正确！\"}");
				return;
			}

		}catch (NullPointerException e){
			response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
