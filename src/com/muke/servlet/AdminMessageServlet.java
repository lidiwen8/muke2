package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.*;
import com.muke.util.DateUtil;
import com.muke.util.Page;
import com.muke.pojo.MessageCriteria.OrderRuleEnum;
import com.muke.service.IMessageService;
import com.muke.service.impl.MessageServiceImpl;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/adminMessageServlet")
public class AdminMessageServlet extends HttpServlet {
	       
    /**
	 * 
	 */
	private static final long serialVersionUID = -6329581478430273613L;
	
	IMessageService iMessageService = new MessageServiceImpl();

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
//		String action = request.getParameter("action");
//		if(action.equals("searchMsg")) {
//			searchMsg(request, response);
//		} else if(action.equals("getMsgCount")) {
//			getMsgCount(request, response);
//		} else if(action.equals("getReplyCount")) {
//			getReplyCount(request, response);
//		} else if(action.equals("deleteMsg")){
//			deleteMsg(request, response);
//		} else if(action.equals("restoreMsg")){
//			restoreMsg(request, response);
//		}
	}

	private void searchMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Admin admin = (Admin) request.getSession().getAttribute("admin");
			if(admin.equals(null)){
				response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
				return;
			}
		String key = request.getParameter("key");
		String username = request.getParameter("username");
		String theid = request.getParameter("theid");
		String pageNum = request.getParameter("pageNum");
		
		if (theid == null || theid.equals("")){
			theid = "-1";
		}
		
		if (pageNum == null || pageNum.equals("")){
			pageNum = "1";
		}

		Page page = new Page();
		page.setCurPage(Integer.parseInt(pageNum));
		
		MessageCriteria messageCriteria = new MessageCriteria();
		messageCriteria.setKey(key);
		messageCriteria.setUsername(username);
		messageCriteria.setTheid(Integer.parseInt(theid));
		messageCriteria.setOrderRule(OrderRuleEnum.ORDER_BY_MSG_TIME);
		
		Page resPage = iMessageService.search(messageCriteria, page);
		
		Gson gson = new GsonBuilder().setDateFormat("MM-dd HH:mm").create();
		String dataJSON = gson.toJson(resPage);
		//System.out.println(dataJSON);
		
		response.getWriter().print("{\"res\": 1, \"data\":"+dataJSON+"}");
		}catch (NullPointerException e){
			response.getWriter().print("{\"res\": 5, \"info\":\"未登录！\"}");
			return;
		}
	}

	private void restoreMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msgid = request.getParameter("msgid");
		if (msgid == null || msgid.equals("")){
			msgid = "-1";
		}
		
		int res = iMessageService.restoreMsg(Integer.parseInt(msgid));
		
		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"恢复失败\"}");
		}
	}

	private void deleteMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String msgid = request.getParameter("msgid");
		if (msgid == null || msgid.equals("")){
			msgid = "-1";
		}
		
		int res = iMessageService.deleteMsg(Integer.parseInt(msgid));
		
		if (res == 1){
			response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
		}
		else {
			response.getWriter().print("{\"res\": "+res+", \"info\":\"删除失败\"}");
		}
	}

	private void getReplyCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date date = new Date();
		
		// 本日发贴量
		long today = iMessageService.queryReplyCountByDate(DateUtil.getToday(date), DateUtil.getTomorrow(date));
		long week = iMessageService.queryReplyCountByDate(DateUtil.getWeekAgo(date), DateUtil.getTomorrow(date));
		long month = iMessageService.queryReplyCountByDate(DateUtil.getMonthAgo(date), DateUtil.getTomorrow(date));
		long replytotal = iMessageService.queryReplyCountBy();
		long visits = iMessageService.queryVisit();//网站访问总量

		//		
		response.getWriter().print("{\"res\": 1, \"data\": {\"today\": "+today+", \"week\": "+week+", \"month\": "+month+", \"replytotal\": " + replytotal + ", \"visits\": " + visits + "}}");
	}

	private void getMsgCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date date = new Date();
		
		// 本日发贴量
		long today = iMessageService.queryMsgCountByDate(DateUtil.getToday(date), DateUtil.getTomorrow(date));
		long week = iMessageService.queryMsgCountByDate(DateUtil.getWeekAgo(date), DateUtil.getTomorrow(date));
		long month = iMessageService.queryMsgCountByDate(DateUtil.getMonthAgo(date), DateUtil.getTomorrow(date));
		long msgtotal = iMessageService.queryMsgCountBy();

		
		//		
		response.getWriter().print("{\"res\": 1, \"data\": {\"today\": "+today+", \"week\": "+week+", \"month\": "+month+", \"msgtotal\": " + msgtotal + "}}");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
