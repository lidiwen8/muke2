package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.MessageEmail;
import com.muke.pojo.User;
import com.muke.service.IUserService;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.IPUtil;
import com.muke.util.Md5Encrypt;
import com.muke.util.Page;
import com.muke.util.SendEmail;

/**
 * Servlet implementation class UserCenterServlet
 */
@WebServlet("/user/userCenterServlet")
public class UserCenterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        try {
            //使用反射定义方法
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            //调用方法
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 登录成功
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Gson gson = new Gson();
        User user1 = userService.queryuserbyid(user.getUserid());
        request.setAttribute("user", user1);
        String dataJSON = gson.toJson(user1);
        response.getWriter().print("{\"res\": 1, \"info\":\"成功上传\",\"src\": \""+user1.getUser_img()+"\"}");
//        response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "\"}");
//       response.getWriter().print("{\"res\": " + user1.getUser_img() + ", \"data1\":" + dataJSON + "}");
    }


    private void updatePw(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        //获取session中的user用户名
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getUsername();
        String password = user.getPassword();
        String newpassword2 = request.getParameter("newpassword2");
        String newpassword = request.getParameter("newpassword");
        String oldpassword = request.getParameter("oldpassword");
        Md5Encrypt md5 = new Md5Encrypt();

        if (oldpassword.length() < 6 || oldpassword.length() > 30 || oldpassword.equals(null) || oldpassword.equals("")) {
            response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:旧密码的长度必须在6到30之间，请仔细核对后重新输入！\"}");
            return;
        }
        try {
            oldpassword = md5.Encrypt(oldpassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (request.getParameter("newpassword").equals(null) || request.getParameter("newpassword").equals("") || request.getParameter("newpassword").length() < 6 || request.getParameter("newpassword").length() > 30) {
            response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:新密码长度必须在6到30之间，请重新输入！\"}");
            return;
        }
        if (request.getParameter("newpassword") != null && request.getParameter("newpassword") != "") {
            boolean flag7 = false;
            Pattern p4 = null;
            Matcher m2 = null;
            p4 = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
            m2 = p4.matcher(request.getParameter("newpassword"));
            flag7 = m2.matches();
            if (flag7 == false) {
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的新密码不合法，不能包含特殊字符和空格，请重新输入！\"}");
                return;
            }
        }
        if (newpassword2.equals(null) || request.getParameter("newpassword2").equals("") || request.getParameter("newpassword2").length() < 6 || request.getParameter("newpassword2").length() > 30) {
            response.getWriter().print("{\"res\": 4, \"info\":\"尊敬的用户：确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入确认密码！\"}");
            return;
        }
        if (!request.getParameter("newpassword2").equals(request.getParameter("newpassword"))) {
            response.getWriter().print("{\"res\": 5, \"info\":\"尊敬的用户：你输入的确认密码跟新密码不匹配，请仔细核对确认密码后再输入！\"}");
            return;
        }
        //验证session中的密码是否与输入的旧密码相同
        if (password.equals(oldpassword)) {

            user.setUsername(username);
            try {
                newpassword = md5.Encrypt(newpassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setPassword(newpassword);
            userService.updatePw(user);
            user.setUsername(username);
            request.getSession().setAttribute("user", user);//修改session中的密码
            response.getWriter().print("{\"res\": 1, \"info\":\"修改成功！\"}");
            if (user.getMailstate() == 1 && user.getEmail() != null && user.getEmail() != "") {
                boolean flag1 = false;
                Pattern p3 = null;
                Matcher m1 = null;
                p3 = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
                m1 = p3.matcher(user.getEmail());
                flag1 = m1.matches();
                if (flag1 == true) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                    String time = df.format(new Date());
                    MessageEmail messageEmail = new MessageEmail();
                    List<String> strs = new ArrayList<String>();
                    strs.add(user.getEmail());
                    messageEmail.setFrom("1632029393@qq.com");
                    messageEmail.setTo(strs);
                    messageEmail.setMsg(SendEmail.sendupdatepass("http://www.lidiwen.club/muke_Web", IPUtil.getIP(request), user.getUsername(), time));
                    try {
                        SendEmail.sslSend(messageEmail);//发送邮件
                    } catch (SendFailedException e) {
                        System.out.println("修改密码时发送邮件失败");
                    }
                }
            }
            return;
        } else {
            response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的用户:你输入的旧密码跟你原始注册的密码不一致，请重新输入！\"}");
            return;
        }
    }

    //修改信息
    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取参数
        String realname = request.getParameter("realname");
        String sex = request.getParameter("sex");
        String user_img = request.getParameter("userAvatar");
        String birthday = request.getParameter("birthday");
        String city = request.getParameter("city");
        String email = request.getParameter("email");
        String qq = request.getParameter("qq");
        String description = request.getParameter("description");
        String[] item = request.getParameterValues("hobbys");
        String delStr = "";
        if (item != null) {
            for (int i = 0; i < item.length - 1; i++) {
                delStr += item[i] + ",";
            }
            for (int i = item.length - 1; i < item.length; i++) {
                delStr += item[i] + ";";
            }
        }
        // 获取当前用户对象
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        User user = sessionUser.clone();
        if (realname.equals(null) || realname.equals("")) {
            response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的" + user.getRealname() + "用户:为了保证你的合法性，修改个人资料时真实姓名不能为空，请如实填写！\"}");
            return;
        }
        // 重设其值

        if (realname != null && realname.trim().length() > 0) {
            user.setRealname(realname);
        }
        if (sex != null && sex.trim().length() > 0) {
            user.setSex(sex);
        }
        try {
            if (item.length == 0 || item.length < 0 || item.equals(null)) {
                response.getWriter().print("{\"res\": 17, \"info\":\"尊敬的" + user.getRealname() + "用户:修改个人资料时爱好不能为空，请至少勾选一个爱好！\"}");
                return;
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\": 17, \"info\":\"尊敬的" + user.getRealname() + "用户:修改个人资料时爱好不能为空，请至少勾选一个爱好！\"}");
            return;
        }
        user.setHobbys(delStr);
        if (birthday != null && birthday.trim().length() > 0) {
            user.setBirthday(birthday);
        }

        if (city.equals(null) || city.equals("")) {
            response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的" + user.getRealname() + "用户:你所在城市不能为空，请点击城市下拉列表选择你所在的城市！\"}");
            return;
        }
        if (city != null && city.trim().length() > 0) {
            user.setCity(city);
        }
        if (email.equals(null) || email.equals("")) {
            response.getWriter().print("{\"res\": 14, \"info\":\"尊敬的" + user.getRealname() + "用户:修改个人资料时邮箱号输入不能为空，请重新输入！\"}");
            return;
        }
        if (email != null && email != "") {
            boolean flag1 = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            m1 = p3.matcher(email);
            flag1 = m1.matches();
            if (flag1 == false) {
                request.setAttribute("username", user.getUsername());
                response.getWriter().print("{\"res\": 5, \"info\":\"尊敬的" + user.getRealname() + "用户:你输入的邮箱格式不正确，请重新输入！\"}");
                return;
            }
        }
        //修改的邮箱跟初始邮箱不同，此时需要激活修改的邮箱
        if (!email.equals(user.getEmail())) {
            //在修改的邮箱跟初始邮箱不同条件下，修改的邮箱跟数据表里面的邮箱（除去自己）有相同的，此时不允许修改当前邮箱
            if (userService.isExistmail(email) == false) {
                response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的" + user.getRealname() + "用户:你输入的邮箱号已经被注册了!修改失败，请换一个邮箱呗！\"}");
                return;
            }
            user.setMailstate(0);//未激活
        }


        if (email != null && email.trim().length() > 0) {
            user.setEmail(email);
        }
        if (qq.equals(null) || qq.equals("")) {
            response.getWriter().print("{\"res\": 16, \"info\":\"尊敬的" + user.getRealname() + "用户:修改个人资料时QQ号输入不能为空，请重新输入！\"}");
            return;
        }
        if (qq != null && qq != "") {
            boolean flag1 = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("[1-9][0-9]{4,14}");
            m1 = p3.matcher(qq);
            flag1 = m1.matches();
            if (flag1 == false) {
                request.setAttribute("username", user.getUsername());
                response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的" + user.getRealname() + "用户:你输入的QQ格式不正确，请重新输入！\"}");
                return;
            }
        }
        if (qq != null && qq.trim().length() > 0) {
            user.setQq(qq);
        }
        if (description.length()<=50) {
            user.setDescription(description);
        }else {
            response.getWriter().print("{\"res\": 7, \"info\":\"尊敬的" + user.getRealname() + "用户:个人简介总长度必须小于50！\"}");
            return;
        }
         if(user_img!=null){
             user.setUser_img(user_img);
         }
        // 更新
        int res = userService.update(user);

        if (res == 1) {    // 更新成功
            // 获取更新后的数据
            user = userService.userLogin(user.getUsername(), user.getPassword());

            // 更新Session
            session.setAttribute("user", user);

            Gson gson = new Gson();
            String dataJSON = gson.toJson(user);

            response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
        } else {
            response.getWriter().print("{\"res\": " + res + ", \"info\":\"修改失败!\"}");
        }
    }

    private void getUserlog(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        Page resPage = userService.queryUserlogbyUserid(user.getUserid(),page);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
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
