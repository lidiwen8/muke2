package com.muke.servlet;

import com.muke.service.IAdminNoticeService;
import com.muke.service.impl.AdminNoticeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Auther: lidiwen
 * @Date: 2019/8/9 23:19
 * @Description:
 */
public class AdminNoticeServlet {
    @WebServlet("/admin/adminNoticeServlet")
    public class UserReportServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private IAdminNoticeService iAdminNoticeService = new AdminNoticeServiceImpl();

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

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request, response);
        }

    }

}
