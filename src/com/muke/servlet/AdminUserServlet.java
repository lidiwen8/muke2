package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.muke.util.StrUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/admin/adminUserServlet")
public class AdminUserServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 4122206234828080374L;
    private IUserService iUserService = new UserServiceImpl();

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
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class,
                    HttpServletResponse.class);
            //调用方法
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userid = request.getParameter("userid");
        if (userid == null || userid.equals("")) {
            userid = "-1";
        }

        int res = iUserService.restoreUser(Integer.parseInt(userid));

        if (res == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"恢复失败\"}");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userid = request.getParameter("userid");
        if (userid == null || userid.equals("")) {
            userid = "-1";
        }

        int res = iUserService.deleteUser(Integer.parseInt(userid));

        if (res == 1) {
            //发送更新信号
            UserMessageServlet.sendMessage("deleteUser"+userid);
            response.getWriter().print("{\"res\": 1, \"info\":\"禁用成功\"}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"禁用失败\"}");
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userid = request.getParameter("userid");
        if (userid == null || userid.equals("")) {
            userid = "-1";
        }
        boolean flag1 = iUserService.queryMessageByid(Integer.parseInt(userid));
        boolean flag2 = iUserService.queryReplyByid(Integer.parseInt(userid));
//		boolean flag3=iUserService.queryReplyByMsgid(Integer.parseInt(userid));
        //该用户没有发表过帖子也没有回复过帖子可以直接删除
        if (flag1 == false && flag2 == false) {
            int res = iUserService.delete(Integer.parseInt(userid));
            if (res == 1) {
                response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
                //发送更新信号
                UserMessageServlet.sendMessage("add");
                return;
            } else {
                response.getWriter().print("{\"res\": " + res + ", \"info\":\"删除失败\"}");
                return;
            }
            //该用户发表过帖子，没有回复过帖子，只需级联删除message表和count表
        } else if (flag1 == true && flag2 == false) {
            response.getWriter().print("{\"res\": 2, \"info\":\"该用户发表过帖子，没有回复过帖子，是否需级联删除帖子表!\"}");
            return;
        }
        //该用户回复过帖子，没有发表过帖子，只需级联删除reply表
        else if (flag1 == false && flag2 == true) {
            response.getWriter().print("{\"res\": 3, \"info\":\"该用户回复过帖子，没有发表过帖子，是否需级联删除回复表!\"}");
            return;
        }
        //该用户回复过帖子，也发表过帖子，需级联删除reply表和message表
        else {
            response.getWriter().print("{\"res\": 4, \"info\":\"该用户回复过帖子，也发表过帖子，是否需级联删除回复表和帖子表!\"}");
            return;
        }


    }

    //级联删除
    public void cascadelete2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userid = request.getParameter("userid");
        if (iUserService.cascadeleteUserAndMessage(Integer.parseInt(userid)) == 1) {
            //发送更新信号
            UserMessageServlet.sendMessage("add");
            response.getWriter().print("{\"res\": 1, \"info\":\"已成功级联删除！\"}");
            return;
        } else {
            response.getWriter().print("{\"res\": -1, \"info\":\"级联删除失败！\"}");
            return;
        }
    }

    public void cascadelete3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userid = request.getParameter("userid");
        //更新count表的replycount字段
        List msg = new ArrayList();
        msg = iUserService.queryMsgid(Integer.parseInt(userid));
        for (int i = 0; i < msg.size(); i++) {
            String passBackParams = StrUtil.getMapToString((Map<String, Integer>) msg.get(i));
            int msgid = Integer.parseInt(passBackParams.substring(6, passBackParams.length()));
            int replyCount = iUserService.queryReplyCountByMsgid(msgid, Integer.parseInt(userid));
            iUserService.updateCountReplyCount(msgid, replyCount);
        }
        if (iUserService.cascadeleteUserAndReply(Integer.parseInt(userid)) == 1) {
            //发送更新信号
            UserMessageServlet.sendMessage("add");
            response.getWriter().print("{\"res\": 1, \"info\":\"已成功级联删除！\"}");
            return;
        } else {
            response.getWriter().print("{\"res\": -1, \"info\":\"级联删除失败！\"}");
            return;
        }
    }

    public void cascadelete4(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userid = request.getParameter("userid");
        //更新count表的replycount字段
        List msg = new ArrayList();
        try {
            msg = iUserService.queryMsgid(Integer.parseInt(userid));
            for (int i = 0; i < msg.size(); i++) {
                String passBackParams = StrUtil.getMapToString((Map<String, Integer>) msg.get(i));
                int msgid = Integer.parseInt(passBackParams.substring(6, passBackParams.length()));
                int replyCount = iUserService.queryReplyCountByMsgid(msgid, Integer.parseInt(userid));
                iUserService.updateCountReplyCount(msgid, replyCount);
            }
            iUserService.cascadeleteUserAndReplyAndMessage(Integer.parseInt(userid));
            iUserService.cascadeleteUserAndReply(Integer.parseInt(userid));
            iUserService.deleteReply(Integer.parseInt(userid));
            //发送更新信号
            UserMessageServlet.sendMessage("add");
            response.getWriter().print("{\"res\": 1, \"info\":\"已成功级联删除！\"}");
            return;
        } catch (Exception e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"级联删除失败！\"}");
            return;
        }
//        if (iUserService.cascadeleteUserAndReplyAndMessage(Integer.parseInt(userid)) == 1 &&iUserService.cascadeleteUserAndReply(Integer.parseInt(userid)) == 1&&iUserService.deleteReply(Integer.parseInt(userid))==1) {
//            response.getWriter().print("{\"res\": 1, \"info\":\"已成功级联删除！\"}");
//            return;
//        } else {
//            response.getWriter().print("{\"res\": -1, \"info\":\"级联删除失败！\"}");
//            return;
//        }
    }

    private void getAdvise(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        Page resPage = iUserService.searchAdvice(page);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String dataJSON = gson.toJson(resPage);
        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
    }

    private void getUserlog(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pageNum = request.getParameter("pageNum");
        String username=request.getParameter("username");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        Page resPage = iUserService.queryUserLogByName(username,page);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        String dataJSON = gson.toJson(resPage);
        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
    }

//    private void queryUserLogByName(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        String pageNum = request.getParameter("page2");
//        String username=request.getParameter("username");
//        if (pageNum == null || pageNum.equals("")) {
//            pageNum = "1";
//        }
//        Page page = new Page();
//        page.setCurPage(Integer.parseInt(pageNum));
//        Page resPage = iUserService.queryUserLogByName(username,page);
//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
//        String dataJSON = gson.toJson(resPage);
//        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
//    }

    private void getAdvisedetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");//帖子Id
        Advise advise = new Advise();
        advise = iUserService.getAdvisedetails(Integer.parseInt(id));
        if (advise.getStates() == 0) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            String json = gson.toJson(advise);
            response.getWriter().print("{\"res\":1,\"advise\":" + json + "}");
        } else {
            response.getWriter().print("{\"res\": -1, \"info\":\"已删除！\"}");
            return;
        }
    }

    private void deleteAdvise(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            id = "-1";
        }

        int res = iUserService.deleteAdvise(Integer.parseInt(id));

        if (res == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"删除失败\"}");
        }
    }

    private void restoreAdvise(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id == null || id.equals("")) {
            id = "-1";
        }

        int res = iUserService.restoreAdvise(Integer.parseInt(id));
        if (res == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"恢复失败\"}");
        }
    }

    //搜索建议信息
    private void searchAdvise(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");//关键字
        String pageNum = request.getParameter("pageNum");//当前页
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));//设置当前页
        page = iUserService.queryAdvise(key, page);//根据关键字和分页信息查询反馈信息
        Gson gson = new Gson();
        String json = gson.toJson(page);//将page转化为json
        response.getWriter().println("{\"advise\":" + json + "}");
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String pageNum = request.getParameter("pageNum");

        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }

        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));

        Page resPage = iUserService.searchByName(username, page);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String dataJSON = gson.toJson(resPage);

        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");

    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
