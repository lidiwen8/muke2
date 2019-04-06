package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.*;
import com.muke.service.IReplyService;
import com.muke.service.impl.IReplyServiceImpl;
import com.muke.util.DateUtil;
import com.muke.util.Page;
import com.muke.pojo.MessageCriteria.OrderRuleEnum;
import com.muke.service.IMessageService;
import com.muke.service.impl.MessageServiceImpl;
import com.muke.util.SendEmail;
import com.muke.util.StrUtil;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/admin/adminMessageServlet")
public class AdminMessageServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -6329581478430273613L;

    private IMessageService iMessageService = new MessageServiceImpl();
    private IReplyService iReplyService = new IReplyServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        messageCriteria.setOrderRule(OrderRuleEnum.ORDER_BY_MSG_TIME);

        Page resPage = iMessageService.search(messageCriteria, page);

        Gson gson = new GsonBuilder().setDateFormat("MM-dd HH:mm").create();
        String dataJSON = gson.toJson(resPage);
        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
    }

    private void restoreMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msgid = request.getParameter("msgid");
        if (msgid == null || msgid.equals("")) {
            msgid = "-1";
        }

        int res = iMessageService.restoreMsg(Integer.parseInt(msgid));

        if (res == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"恢复失败\"}");
        }
    }

    private void deleteMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String msgid = request.getParameter("msgid");
        if (msgid == null || msgid.equals("")) {
            msgid = "-1";
        }
        MessageInfo messageInfo = iMessageService.getMsgNoincreaseCount(Integer.parseInt(msgid));
        int res = iMessageService.deleteMsg(Integer.parseInt(msgid));

        if (res == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
            int finalMsgid = Integer.parseInt(msgid);
            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    List emailList=null;
                    if(messageInfo.getReplyCount()>0){
                         emailList = iReplyService.AdmingetReplyUseremail(finalMsgid);
                    }else {
                        emailList=iReplyService.AdmingetMsgUseremail(finalMsgid);
                    }
                        if (emailList.size() != 0 && emailList != null) {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                            String time = df.format(new Date());
                            MessageEmail messageEmail = new MessageEmail();
                            messageEmail.setFrom("1632029393@qq.com");
                            try {
                                messageEmail.setMsg(SendEmail.SendDeleteMsgmail3("http://www.lidiwen.club/muke_Web/message.jsp?msgid="+ finalMsgid, messageInfo.getMsgtopic(), time));
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                            List<String> str = new ArrayList<String>();
                            for (int i = 0; i < emailList.size(); i++) {
                                String email = StrUtil.getMapToString2((Map<String, String>) emailList.get(i));
                                str.add(email.replace("email=", ""));
                            }
                            messageEmail.setTo(str);
                            try {
                                SendEmail.sslSend(messageEmail);//发送邮件
                                return;
                            } catch (MessagingException | IOException e) {
                                System.out.println("管理员删除帖子时发送邮件失败");
                                return;
                            }
                        }else {
                            return;
                        }
                    }
            });
            t2.start();
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"删除失败\"}");
        }
    }

    private void getReplyCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();

        // 本日回帖量
        long today = iReplyService.queryReplyCountByDate(DateUtil.getToday(date), DateUtil.getTomorrow(date));
        long week = iReplyService.queryReplyCountByDate(DateUtil.getWeekAgo(date), DateUtil.getTomorrow(date));
        long month = iReplyService.queryReplyCountByDate(DateUtil.getMonthAgo(date), DateUtil.getTomorrow(date));
        long replytotal = iReplyService.queryReplyCountBy();
        long visits = iReplyService.queryVisit();//网站访问总量

        //
        response.getWriter().print("{\"res\": 1, \"data\": {\"today\": " + today + ", \"week\": " + week + ", \"month\": " + month + ", \"replytotal\": " + replytotal + ", \"visits\": " + visits + "}}");
    }

    private void getMsgCount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Date date = new Date();

        // 本日发贴量
        long today = iMessageService.queryMsgCountByDate(DateUtil.getToday(date), DateUtil.getTomorrow(date));
        long week = iMessageService.queryMsgCountByDate(DateUtil.getWeekAgo(date), DateUtil.getTomorrow(date));
        long month = iMessageService.queryMsgCountByDate(DateUtil.getMonthAgo(date), DateUtil.getTomorrow(date));
        long msgtotal = iMessageService.queryMsgCountBy();


        //
        response.getWriter().print("{\"res\": 1, \"data\": {\"today\": " + today + ", \"week\": " + week + ", \"month\": " + month + ", \"msgtotal\": " + msgtotal + "}}");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
