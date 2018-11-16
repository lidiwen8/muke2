package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.MessageInfo;
import com.muke.pojo.Reply;
import com.muke.pojo.User;
import com.muke.service.IMessageService;
import com.muke.service.IUserService;
import com.muke.service.impl.MessageServiceImpl;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.Page;
import com.muke.util.SessionCountUtil;
import com.muke.util.ToolsUtil;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/messageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IMessageService messageservice=new MessageServiceImpl();
	IUserService userService = new UserServiceImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1查看主题帖 2 查询帖子回复信息 3 查询最新5贴 4查询最热5帖   5查询最热5主题的最热帖
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

	// 查看帖子详细信息
		private void getMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
			String msgId=request.getParameter("msgId");//帖子Id
			MessageInfo messageInfo = new MessageInfo();
			messageInfo=messageservice.getMsg(Integer.parseInt(msgId));
			User user=userService.queryuserbyid(messageInfo.getUserid());
			if(!user.getUser_img().equals(null)){
				request.setAttribute("user.user_img",user.getUser_img());
			}
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			String json=gson.toJson(messageInfo);
			response.getWriter().print("{\"res\":1,\"message\":"+json+"}");
		}

		// 查询帖子回复内容
		private void getReply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
					String pageNum=request.getParameter("pageNum");
					if(pageNum==null || pageNum.equals("")){
						pageNum="1";
					}
					String msgId=request.getParameter("msgId");
					Page page=new Page();
					page.setCurPage(Integer.parseInt(pageNum));
					page=messageservice.getReply(Integer.parseInt(msgId), page);
			        Reply reply=messageservice.queryid(Integer.parseInt(msgId));
			        User user=userService.queryuserbyid(reply.getUserid());
			        if(!user.getUser_img().equals(null)){
						request.setAttribute("user.user_img",user.getUser_img());
					}
					Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
					String json=gson.toJson(page);
					response.getWriter().print("{\"reply\":"+json+"}");
		}

		// 最新帖子
		private void topNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
				String pageNum=request.getParameter("pageNum");
				if(pageNum==null || pageNum.equals("")){
					pageNum="1";
				}
				Page page=new Page();
				page.setCurPage(Integer.parseInt(pageNum));
				page=messageservice.queryNew(page);
				Gson gson = new GsonBuilder().setDateFormat("yyyy年MM月dd日 HH:mm:ss").create();
			    String json=gson.toJson(page);
				response.getWriter().print("{\"res\": 1,\"newMsg\":"+json+"}");
		}

		// 最热帖子
		private void topHot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
			String pageNum=request.getParameter("pageNum");
			if(pageNum==null || pageNum.equals("")){
				pageNum="1";
			}
			Page page=new Page();
			page.setCurPage(Integer.parseInt(pageNum));
			page=messageservice.queryHot(page);
			Gson gson = new GsonBuilder().setDateFormat("M/d").create();
			String json=gson.toJson(page);
			response.getWriter().print("{\"res\": 1,\"hotMsg\":"+json+"}");
		}

	// 点赞数
//	private void zan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
//		String pageNum=request.getParameter("pageNum");
//		if(pageNum==null || pageNum.equals("")){
//			pageNum="1";
//		}
//		Page page=new Page();
//		page.setCurPage(Integer.parseInt(pageNum));
//		page=messageservice.queryHot(page);
//		Gson gson = new GsonBuilder().setDateFormat("M/d").create();
//		String json=gson.toJson(page);
//		response.getWriter().print("{\"res\": 1,\"hotMsg\":"+json+"}");
//	}

		// 查询最热5主题的，最新帖
		private void topTheme(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String pageNum=request.getParameter("pageNum");
			if(pageNum==null || pageNum.equals("")){
				pageNum="1";
			}
			Page page=new Page();
			page.setCurPage(Integer.parseInt(pageNum));
			page=messageservice.queryTheme(page);
			Gson gson = new GsonBuilder().setDateFormat("M/d").create();
			String json=gson.toJson(page);
			response.getWriter().print("{\"res\": 1, \"themeMsg\":"+json+"}");
		}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
