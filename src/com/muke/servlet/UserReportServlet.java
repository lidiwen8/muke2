package com.muke.servlet;

import com.muke.pojo.Report;
import com.muke.pojo.User;
import com.muke.service.IMessageService;
import com.muke.service.IReplyService;
import com.muke.service.IUserRportService;
import com.muke.service.impl.IReplyServiceImpl;
import com.muke.service.impl.IUserRportServiceImpl;
import com.muke.service.impl.MessageServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:21
 * @Description:
 */
@WebServlet("/user/userReportServlet")
public class UserReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserRportService iUserRportService = new IUserRportServiceImpl();
    private IReplyService iReplyService = new IReplyServiceImpl();
    private IMessageService messageservice = new MessageServiceImpl();
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //举报帖子，举报回复
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
    private void report(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String reportDetail = request.getParameter("reportDetail");
        String msgid = request.getParameter("msgid");
        String replyid = request.getParameter("replyid");
        Report report = new Report();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if(Integer.parseInt(replyid)==-1){
            if(messageservice.getMsgNoincreaseCount(Integer.parseInt(msgid)).getUserid()==user.getUserid()){
                response.getWriter().print("{\"res\": -1, \"info\":\"举报失败,你不可以举报自己发布的帖子！\"}");
                return;
            }
            report.setReportType("帖子举报");
        }else {
            if(iReplyService.queryid(Integer.parseInt(replyid)).getUserid()==user.getUserid()){
                response.getWriter().print("{\"res\": -1, \"info\":\"举报失败,你不可以举报自己回复的内容！\"}");
                return;
            }
            report.setReportType("回复举报");
        }
        String reportDetailType = request.getParameter("report_select");


        report.setUid(user.getUserid());
        report.setReportDetail(reportDetail);
        report.setReportDetailType(reportDetailType);
        report.setUsername(user.getUsername());
        report.setTime(new Date());
        report.setMsgid(Integer.parseInt(msgid));
        report.setReplyid(Integer.parseInt(replyid));
        int result = iUserRportService.insertRport(report);
        if (result == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"举报成功，我们会尽快处理！\"}");
            return;
        } else {
            response.getWriter().print("{\"res\": -1, \"info\":\"举报失败！\"}");
            return;
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
