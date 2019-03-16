package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Timestamp;
import java.util.Date;
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
import com.muke.service.IMessageService;
import com.muke.service.IReplyService;
import com.muke.service.IThemeService;
import com.muke.service.IUserService;
import com.muke.service.impl.IReplyServiceImpl;
import com.muke.service.impl.MessageServiceImpl;
import com.muke.service.impl.ThemeServiceImpl;
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
    IMessageService messageservice = new MessageServiceImpl();
    IReplyService iReplyService = new IReplyServiceImpl();
    IThemeService themeService = new ThemeServiceImpl();
    IUserService userService = new UserServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1查看主题帖 2 查询帖子回复信息 3 查询最新5贴 4查询最热5帖   5查询最热5主题的最热帖
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        //which  if else  或者反射都可以,现在用反射
        try {
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            //调用方法
            method.invoke(this, request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // 查看帖子详细信息
    private void getMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msgId = request.getParameter("msgId");//帖子Id
        MessageInfo messageInfo = new MessageInfo();
        try {
            if (messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId)).equals(null)) {
                response.getWriter().print("{\"res\":-1,\"message\":\"该帖子不存在！\"}");
                return;
            }
        }catch (NumberFormatException e){
            response.getWriter().print("{\"res\":-1,\"message\":\"该帖子不存在！\"}");
            return;
        }
        catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"message\":\"该帖子不存在！\"}");
            return;
        }
        if (request.getSession().getAttribute("admin") != null) {
            messageInfo = messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId));
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String json = gson.toJson(messageInfo);
            response.getWriter().print("{\"res\":1,\"message\":" + json + "}");
            return;
        }
        int state = messageservice.queryMsgState(Integer.parseInt(msgId));
        if (state == -1) {
            response.getWriter().print("{\"res\":-1,\"message\":\"该帖子已经被管理员屏蔽了！如有疑问，请联系管理员QQ:1632029393!\"}");
            return;
        }
        if (request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            if (user.getUserid() == messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId)).getUserid()) {
                messageInfo = messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId));
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                String json = gson.toJson(messageInfo);
                response.getWriter().print("{\"res\":1,\"message\":" + json + "}");
                return;
            }
        }
        if (state == 3) {
            User user = userService.queryuserbyid(messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId)).getUserid());
            if (user.getMailstate() == 1) {
                response.getWriter().print("{\"res\":-1,\"message\":\"该帖子已经被该贴楼主暂时屏蔽了！如有疑问，请联系楼主QQ邮箱:" + user.getEmail() + "!\"}");
                return;
            } else {
                response.getWriter().print("{\"res\":-1,\"message\":\"该帖子已经被该贴楼主暂时屏蔽了！\"}");
                return;
            }
        }
        messageInfo = messageservice.getMsg(Integer.parseInt(msgId));
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(messageInfo);
        response.getWriter().print("{\"res\":1,\"message\":" + json + "}");
    }

    // 查询帖子回复内容
    private void getReply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        String msgId = request.getParameter("msgId");
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        page = iReplyService.getReply(Integer.parseInt(msgId), page);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(page);
        response.getWriter().print("{\"reply\":" + json + "}");
    }

    //查询其它用户发表的帖子信息而且没有被自屏的
    private void searchUserMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        try {
            int userid = Integer.parseInt(request.getParameter("userid"));
            if (pageNum == null || pageNum.equals("")) {
                pageNum = "1";
            }
            Page page = new Page();
            page.setCurPage(Integer.parseInt(pageNum));
            //创建封装查询条件对象
            MessageCriteria messageCriteria = new MessageCriteria();
            messageCriteria.setUserid(userid);
            messageCriteria.setOrderRule(MessageCriteria.OrderRuleEnum.ORDER_BY_MSG_TIME);//排序条件
            messageCriteria.setState(0);//查询非禁用状态
            page = messageservice.searchUserMsg(messageCriteria, page);
            if (page == null) {
                response.getWriter().print("{\"res\":-1,\"message\":不存在该用户}");
                return;
            }
            Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd").create();
            String json = gson.toJson(page);
            response.getWriter().print("{\"res\":1,\"message\":" + json + "}");
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"message\":不存在该用户}");
            return;
        }
    }

    // 最新帖子
    private void topNew(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        page = messageservice.queryNew(page);
        Gson gson = new GsonBuilder().setDateFormat("yyyy年MM月dd日 HH:mm:ss").create();
        String json = gson.toJson(page);
        response.getWriter().print("{\"res\": 1,\"newMsg\":" + json + "}");
    }

    // 最热帖子
    private void topHot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        page = messageservice.queryHot(page);
        Gson gson = new GsonBuilder().setDateFormat("M/d").create();
        String json = gson.toJson(page);
        response.getWriter().print("{\"res\": 1,\"hotMsg\":" + json + "}");
    }


    // 查询最热5主题的，最新帖
    private void topTheme(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        page = messageservice.queryTheme(page);
        Gson gson = new GsonBuilder().setDateFormat("M/d").create();
        String json = gson.toJson(page);
        response.getWriter().print("{\"res\": 1, \"themeMsg\":" + json + "}");
    }

    //主页搜索相关帖子
    private void searchMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String key = request.getParameter("key");
        String username = request.getParameter("username");
        String theid = request.getParameter("theid");
        String pageNum = request.getParameter("pageNum");

        if (theid == null || theid.equals("")) {
            theid = "-1";
        }

        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }


        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));

        MessageCriteria messageCriteria = new MessageCriteria();
        messageCriteria.setKey(key);
        messageCriteria.setUsername(username);
        messageCriteria.setTheid(Integer.parseInt(theid));
        messageCriteria.setState(0);
        messageCriteria.setOrderRule(MessageCriteria.OrderRuleEnum.ORDER_BY_MSG_TIME);

        Page resPage = messageservice.search1(messageCriteria, page);

        Gson gson = new GsonBuilder().setDateFormat("MM-dd HH:mm").create();
        String dataJSON = gson.toJson(resPage);
        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
    }

    //获取全部主题信息
    private void getAllTheme(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Theme> list = themeService.getAll();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        response.getWriter().print("{\"theme\":" + json + "}");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
