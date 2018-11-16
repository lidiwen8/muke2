package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.*;
import com.muke.pojo.MessageCriteria.OrderRuleEnum;
import com.muke.service.IMessageService;
import com.muke.service.impl.MessageServiceImpl;
import com.muke.util.*;

/**
 * Servlet implementation class UserMessageServlet
 */
@WebServlet("/userMessageServlet")
public class UserMessageServlet extends HttpServlet {
	private IMessageService messageservice=new MessageServiceImpl();
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//发布帖子  恢复帖子  查看我的帖子
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
//发帖
	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String msgtopic=request.getParameter("msgtopic");//帖子标题
			String theid=request.getParameter("theid");//主题ID
			String msgcontents=request.getParameter("msgcontents");//主题内容
			HttpSession session=request.getSession();
			User user=(User) session.getAttribute("user");
			int userid=user.getUserid();//用户ID
			String msgip=IPUtil.getIP(request);//发帖人的IP
		    msgcontents= EmojiUtil.resolveToByteFromEmoji(msgcontents);//表情包转化为编码
	        String str=msgcontents.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim();
//			msgcontents=HTMLReplace.replace(msgcontents);//帖子内容转换为HTML格式
		if(str.equals("")||str.length()==0||str.equals(null)){
			response.getWriter().print("{\"res\":2,\"info\":\"尊敬的"+user.getUsername()+"用户：提问内容不能全部输入空格或者回车,发帖失败\"}");
			return;
		}
		ToolsUtil toolsUtil =new ToolsUtil();
		String str2=toolsUtil.delHTMLTag(str).replaceAll(" ","").replaceAll("\\s*","");
		if(str2.trim().length()<6||str2.trim().length()>1000){
			response.getWriter().print("{\"res\":3,\"info\":\"尊敬的"+user.getUsername()+"用户：问题内容长度必须在6到1000之间\"}");
			return;
		}
			Message message=new Message();
			message.setMsgtopic(msgtopic);
			message.setTheid(Integer.parseInt(theid));
			message.setMsgcontents(msgcontents);
			message.setUserid(userid);
			message.setMsgip(msgip);
		int rs=messageservice.addMsg(message);
		if(rs>0){
			response.getWriter().print("{\"res\":1,\"info\":\"发帖成功\"}");
		}else{
			response.getWriter().print("{\"res\":-1,\"info\":\"发帖失败\"}");
		}
	}
	
	//查询
	private void getMyMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String pageNum=request.getParameter("pageNum");
	if(pageNum == null || pageNum.equals("")){
		pageNum="1";
	}
	Page page=new Page();
	page.setCurPage(Integer.parseInt(pageNum));
	HttpSession session=request.getSession();
	User user=(User) session.getAttribute("user");
	int  userid=user.getUserid();
	//创建封装查询条件对象
	MessageCriteria messageCriteria=new MessageCriteria();
	messageCriteria.setUserid(userid);
	messageCriteria.setOrderRule(OrderRuleEnum.ORDER_BY_MSG_TIME);//排序条件
	messageCriteria.setState(0);//查询非禁用状态
	page=messageservice.search(messageCriteria,page);
	Gson gson=new GsonBuilder().setDateFormat("yy-MM-dd").create();
	String json=gson.toJson(page);
	response.getWriter().print("{\"res\":1,\"message\":" + json + "}");
	
	}
	
	//回帖
	private void replyMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String msgId=request.getParameter("msgId");
		String replycontent=request.getParameter("replycontent");
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		int userid=user.getUserid();//用户ID
		String replyip=IPUtil.getIP(request);//获取IP
		replycontent= EmojiUtil.resolveToByteFromEmoji(replycontent);//表情包转化为编码
//		replycontent=HTMLReplace.replace(replycontent);
		if(replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals("")||replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().length()==0||replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals(null)){
			response.getWriter().print("{\"res\":2,\"info\":\"尊敬的"+user.getUsername()+"用户：不能全部输入空格或者回车,回帖失败\"}");
			return;
		}
		Reply reply=new Reply();
				reply.setUserid(userid);
				reply.setMsgid(Integer.parseInt(msgId));
				reply.setReplycontents(replycontent);
				reply.setReplyip(replyip);
				int rs=messageservice.replyMsg(reply);
				if(rs>0){
					response.getWriter().print("{\"res\":1,\"info\":\"回帖成功\"}");
				}else{
					response.getWriter().print("{\"res\":-1,\"info\":\"回帖失败\"}");
				}
	}
	//修改问题
	private void updateMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int msgId= Integer.parseInt(request.getParameter("msgId"));
		String msgcontents=request.getParameter("msgcontents");
		String msgip=IPUtil.getIP(request);//发帖人的IP
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		int  userid=user.getUserid();
		MessageInfo messageInfo=messageservice.getMsg(msgId);
		messageInfo.setMsgcontents(msgcontents);
		messageInfo.setMsgtime(new Date());
		messageInfo.setMsgip(msgip);
		if(userid==messageInfo.getUserid()) {//只有本人才有资格修改自己的问题
			int rs = messageservice.updateMsg(messageInfo);
			if (rs > 0) {
				response.getWriter().print("{\"res\":1,\"info\":\"更新成功\"}");
			} else {
				response.getWriter().print("{\"res\":-1,\"info\":\"更新失败\"}");
			}
		}
		else{
			response.getWriter().print("{\"res\":-1,\"info\":\"权限不足，更新失败\"}");
		}
	}
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
