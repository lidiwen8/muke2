package com.muke.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.service.IUserRportService;
import com.muke.service.impl.IUserRportServiceImpl;
import com.muke.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Auther: lidiwen
 * @Date: 2019/6/15 14:42
 * @Description:
 */
@WebServlet("/admin/adminReportServlet")
public class AdminReportServlet extends HttpServlet {
    private IUserRportService iUserRportService = new IUserRportServiceImpl();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
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

    private void getReport(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        Page resPage = iUserRportService.queryRport(page);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String dataJSON = gson.toJson(resPage);
        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
    }

    private void changeState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reid = request.getParameter("reid");
        if (reid == null || reid.equals("")) {
            reid = "-1";
        }

        int res = iUserRportService.changeState(Integer.parseInt(reid),1);

        if (res == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"成功\"}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"失败\"}");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }



}
