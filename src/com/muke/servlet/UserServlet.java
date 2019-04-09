package com.muke.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.config.GeetestConfig;
import com.muke.pojo.*;
import com.muke.sdk.GeetestLib;
import com.muke.service.IMessageService;
import com.muke.service.IUserService;
import com.muke.service.impl.MessageServiceImpl;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.*;
import com.sun.mail.smtp.SMTPAddressFailedException;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 4122206234828080374L;
    private IUserService userService = new UserServiceImpl();
    private IMessageService messageservice = new MessageServiceImpl();
    private static String validate = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    //验证用户名是否存在
    private void isExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        boolean result = userService.isExist(username);
        response.getWriter().print("{\"valid\":" + result + "}");
    }

    //网站建议
    private void help(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String advise = request.getParameter("advise");
        String number = request.getParameter("number");
        int result = userService.saveAdvise(advise, number);
        if (result == 1) {
            response.getWriter().print("{\"res\": 1, \"info\":\"提交成功！你的建议我们已经收到，我们将认真参考你宝贵的意见，谢谢\"}");
            return;
        } else {
            response.getWriter().print("{\"res\": -1, \"info\":\"提交失败！\"}");
            return;
        }

    }

    //检验用户账号是不是可用
    private void checkUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        if (userService.isExist(username) == false) {
            response.getWriter().print("{\"res\": -1, \"info\":\"账号已存在\"}");
        } else {
            response.getWriter().print("{\"res\": 1, \"info\":\"该账号可用！\"}");
        }

    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user") != null) {
            ServletContext application = this.getServletContext();
            User user = (User) request.getSession().getAttribute("user");
            // 获取application对象中user
            ArrayList<String> users = (ArrayList<String>) application.getAttribute("user");
            if (application.getAttribute("user") != null) {
                for (int i = 0; i < users.size(); i++) { //循环application对象
                    //如果当前登录用户对象与保存在application中的对象相等
                    if (user.getUsername().equals(users.get(i))) {
                        users.remove(i); //从保存的LIST中清除掉登录的用户
                    }
                }
            }
            request.getSession().invalidate();//使session无效
//            application.removeAttribute("users");//从application中移除user使session无效
        }
        response.getWriter().print("{\"res\": 1, \"info\":\"欢迎下次登录！\"}");
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String realname = request.getParameter("realname");
        String sex = request.getParameter("sex");
        boolean flag3 = true;

//		String hobbys = request.getParameter("hobbys");
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

        String birthday = request.getParameter("birthday");
        String city = request.getParameter("city");
        String email = request.getParameter("email");
        String qq = request.getParameter("qq");

        if (username.equals(null) || username.equals("")) {
            response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:用户账号不能为空，请重新输入！\"}");
            return;
        }
        if (username != null && username != "") {
            boolean flag = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("^[A-Za-z0-9]+$");
            m1 = p3.matcher(username);
            flag = m1.matches();
            if (flag == false || username.length() < 6 || username.length() > 30) {
                flag3 = false;
                response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！\"}");
                return;
            }
        }
        if (password.equals(null) || password.equals("") || password.length() < 6 || password.length() > 30) {
            response.getWriter().print("{\"res\": 4, \"info\":\"尊敬的用户:密码长度必须在6到30之间，请重新输入！\"}");
            return;
        }
        if (password != null && password != "") {
            boolean flag9 = false;
            Pattern p4 = null;
            Matcher m2 = null;
            p4 = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
            m2 = p4.matcher(password);
            flag9 = m2.matches();
            if (flag9 == false) {
                response.getWriter().print("{\"res\": 28, \"info\":\"尊敬的用户:你输入的密码不合法，不能包含特殊字符和空格，请重新输入！\"}");
                return;
            }
        }
        if (password2.equals(null) || password2.equals("") || password2.length() < 6 || password2.length() > 30) {
            response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入！\"}");
            return;
        }
        if (!password.equals(password2)) {
            response.getWriter().print("{\"res\": 5, \"info\":\"尊敬的用户:确认密码跟新密码不匹配，请重新输入确认密码！\"}");
            return;
        }
        if (realname.equals(null) || realname.equals("")) {
            response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的用户:为了保证你的合法性，真实姓名不能为空，请如实填写！\"}");
            return;
        }
        if (realname.length() > 30) {
            response.getWriter().print("{\"res\": 30, \"info\":\"尊敬的用户:真实姓名的长度必须小于30，请重新填写！\"}");
            return;
        }
        try {
            if (item.length == 0 || item.length < 0 || item.equals(null)) {
                response.getWriter().print("{\"res\": 13, \"info\":\"尊敬的用户:爱好不能为空，请至少勾选一个爱好！\"}");
                return;
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\": 13, \"info\":\"尊敬的用户:爱好不能为空，请至少勾选一个爱好！\"}");
            return;
        }
        if (city.equals(null) || city.equals("")) {
            response.getWriter().print("{\"res\": 7, \"info\":\"尊敬的用户:所在城市不能为空，请点击城市下拉列表选择你所在的城市！\"}");
            return;
        }
        if (email.equals(null) || email.equals("")) {
            response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
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
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的邮箱格式不正确，请重新输入！\"}");
                return;
            }
        }
        if (qq.equals(null) || qq.equals("")) {
            response.getWriter().print("{\"res\": 11, \"info\":\"尊敬的用户:QQ号输入不能为空，请重新输入！\"}");
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
                response.getWriter().print("{\"res\": 12, \"info\":\"尊敬的用户:你输入的QQ格式不正确，请重新输入！\"}");
                return;
            }
        }

        if (userService.isExist(username) == false) {
            response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的账号已经存在!注册失败，请换一个其它账号呗！\"}");
            return;
        }
        //被其它用户绑定的邮箱不能继续被绑定，也就是数据库存在该邮箱且已经被激活
        User user1 = userService.useremail(email);
        if (user1 != null && user1.getMailstate() == 1) {
            response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的邮箱已经被注册了!注册失败，请换一个邮箱呗！\"}");
            return;
        }
        User user = new User();
        user.setUsername(username);
        Md5Encrypt md5 = new Md5Encrypt();
        try {
            password = md5.Encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setPassword(password);
        user.setRealname(realname);
        if (sex.equals("男")) {
            user.setUser_img("image/nan.png");
        } else {
            user.setUser_img("image/nu.png");
        }
        user.setSex(sex);

        user.setHobbys(delStr);
        user.setBirthday(birthday);
        user.setCity(city);
        user.setEmail(email);
        user.setQq(qq);
        user.setLoginNum(1);
        Date day = new Date();
        SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//登录时间
        user.setLogintime(da.format(day));
        try {

            int res = userService.userRegister(user);
            if (res == 1) {
                // 自动登录
                user = userService.userLogin(username, password);
                // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("username", username);
                response.getWriter().print("{\"res\": 1, \"info\":\"注册成功\"}");
                Userlog userlog = new Userlog();
                userlog.setLogintime(da.format(day));
                userlog.setUserid(user.getUserid());
                String ip = IPUtil.getIpAdrress(request);
                userlog.setIp(ip);
                userlog.setPlace(PlaceUtil.baiduGetCityCode(ip));
                userService.insertloginLog(userlog);//注册成功后自动登录也要插入登陆日志
                return;
            } else {
                response.getWriter().print("{\"res\": " + res + ", \"info\":\"注册失败，尊敬的用户:你输入的账号已经存在，请换一个其它账号呗！\"}");
                return;
            }
        } catch (Exception e) {
            response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的账号已经存在!注册失败，请换一个其它账号呗！\"}");
            return;
        }

    }

    private void emailpass(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String email = request.getParameter("mail");
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        String verifyCode = request.getParameter("verifyCode");
        String key;
        if (email.equals(null) || email.equals("")) {
            response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
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
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的邮箱格式不正确，请重新输入！\"}");
                return;
            }
        }
        // VerifyCodeServlet会把真正的验证码保存到session中
        String vcode = (String) request.getSession().getAttribute("vCode");
        if (!verifyCode.equalsIgnoreCase(vcode)) {
            response.getWriter().print("{\"res\": 7, \"info\":\"验证码输入错误，请重新输入！\"}");
            return;
        }
        if (userService.isExistmail(email) == true) {
            response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的邮箱号有误!用户认证失败，请重新输入！\"}");
            return;
        }
        User user = userService.useremail(email);
        String username = user.getUsername();
        if (user.getMailstate() == 1) {
            Md5Encrypt md5 = new Md5Encrypt();
            MessageEmail messageEmail = new MessageEmail();
            List<String> strs = new ArrayList<String>();
            strs.add(user.getEmail());
            messageEmail.setFrom("1632029393@qq.com");
            messageEmail.setTo(strs);
            messageEmail.setSubject("[爱之家科技有限公司]重置密码通知");
            key = UUIDUtils.getUUID();
            HttpSession session = request.getSession();
            session.setAttribute(username + email, key);
            session.setAttribute(username + "token", username + email);
            session.setMaxInactiveInterval(60 * 5);
            messageEmail.setMsg(SendEmail.sendMsg("http://www.lidiwen.club/muke_Web/userServlet/passwordResetByemail", random, username, key, email));
            try {
                random = md5.Encrypt(random);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (SendEmail.sslSend(messageEmail)) {
                    session.setAttribute(username + "random", random);
                    response.getWriter().print("{\"res\": 1, \"info\":\"尊敬的用户：用户验证成功，发送邮件成功，请你及时登录邮箱查看\"}");
                    return;
                } else {
                    response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户：用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员\"}");
                    return;
                }
            } catch (SendFailedException e) {
                response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户：用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员\"}");
                return;
            }
        } else {
            response.getWriter().print("{\"res\": 27, \"info\":\"尊敬的用户：你的该邮箱账号并没有被激活，不能通过此方式来找回密码，请激活后再通过邮箱账号重置密码！\"}");
            return;
        }
    }

    private void passwordResetByemail(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String key = request.getParameter("key");
        User user = userService.username(username);
        HttpSession session = request.getSession();
        if (email == null || username == null || key == null || email == "" || username == "" || key == "") {
            response.getWriter().print("{\"res\": -1, \"info\":\"该链接是无效的！\"}");
            return;
        }
        if (user != null) {
            if (user.getEmail().equals(email) && user.getMailstate() == 1) {
                try {
                    if (session.getAttribute(username + "token").equals(username + email)) {
                        if (session.getAttribute(username + email).equals(key)) {
                            if (session.getAttribute(username + "check") == null) {
                                user.setPassword((String) session.getAttribute(username + "random"));
                                userService.updatePw(user);
                                session.setAttribute(username + "check", true);
                                session.setMaxInactiveInterval(60 * 5);
                                response.getWriter().print("{\"res\": 1, \"info\":\"重置的密码已经生效，请用新密码登录！\"}");
                                return;
                            } else {
                                response.getWriter().print("{\"res\": -1, \"info\":\"该链接是一次性的，重复点击无效！\"}");
                                return;
                            }
                        }
                    } else {
                        response.getWriter().print("{\"res\": -1, \"info\":\"没有找回密码，或已经找回密码且新密码已经生效！\"}");
                        return;
                    }

                } catch (NullPointerException e) {
                    response.getWriter().print("{\"res\": -2, \"info\":\"该链接已过期，请重新通过系统向你绑定的邮箱发送找回密码的链接！\"}");
                    return;
                }
            } else {
                response.getWriter().print("{\"res\": -1, \"info\":\"该链接是无效的！\"}");
                return;
            }
        } else {
            response.getWriter().print("{\"res\": -1, \"info\":\"该链接是无效的！\"}");
            return;
        }

    }

    private void bindingmail(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String email = request.getParameter("mail");
        String username = request.getParameter("username");
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        validate = random;
        HttpSession session = request.getSession();
        if (username.equals(null) || username.equals("")) {
            response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:用户账号不能为空，请重新输入！\"}");
            return;
        }
        if (username != null && username != "") {
            boolean flag = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("^[A-Za-z0-9]+$");
            m1 = p3.matcher(username);
            flag = m1.matches();
            if (flag == false || username.length() < 6 || username.length() > 30) {
                response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！\"}");
                return;
            }
        }
        if (email.equals(null) || email.equals("")) {
            response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
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
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的邮箱格式不正确，请重新输入！\"}");
                return;
            }
        }
        User user = userService.username(username);
        if (user == null) {
            response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的账号并没有注册过!请你注册后，再来绑定你的邮箱！\"}");
            return;
        }
        if (user.getMailstate() == 1) {
            response.getWriter().print("{\"res\": 21, \"info\":\"尊敬的" + user.getUsername() + "用户:你的邮箱已经激活了，请不要重复激活！\"}");
            return;
        }
        User user2 = (User) session.getAttribute("user");
        if (user2 == null && (email != user.getEmail())) {
            response.getWriter().print("{\"res\": 17, \"info\":\"尊敬的" + user.getUsername() + "用户:你输入的邮箱跟你注册时的邮箱不匹配，请重新输入！\"}");
            return;
        }
        //被其它用户绑定的邮箱不能继续被绑定，也就是数据库存在该邮箱且已经被激活
        User user1 = userService.useremail(email);
        if (user1 != null && user1.getMailstate() == 1) {
            response.getWriter().print("{\"res\": 23, \"info\":\"尊敬的" + user.getUsername() + "用户:你输入的邮箱已经抢先被其它用户绑定了!绑定邮箱失败，请换一个邮箱呗！\"}");
            return;
        }
        MessageEmail messageEmail = new MessageEmail();
        List<String> strs = new ArrayList<String>();
        strs.add(email);
        messageEmail.setFrom("1632029393@qq.com");
        messageEmail.setTo(strs);
        messageEmail.setSubject("[爱之家科技有限公司]绑定邮箱通知");
        messageEmail.setMsg(SendEmail.sendbindingmail(random, IPUtil.getIP(request), user.getUsername()));
        try {
            try {
                if (SendEmail.sslSend(messageEmail)) {
                    response.getWriter().print("{\"res\": 1, \"info\":\"尊敬的用户：用户验证成功，发送邮件成功，请你及时登录邮箱查看\"}");
                    session.setAttribute("email", email);
                    session.setAttribute("username", username);
                    return;
                } else {
                    response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的" + user.getUsername() + "用户：用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员\"}");
                    return;
                }
            } catch (SendFailedException e) {
                response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的用户：发送邮件失败，你输入的邮件地址是无效的，请重新输入\"}");
                return;
            }
        } catch (SMTPAddressFailedException e) {
            response.getWriter().print("{\"res\": 1, \"info\":\"尊敬的用户：用户验证成功，发送邮件成功！请你及时登录邮箱查看。若您没有收到邮件，请一分钟后核对邮箱重新验证。\"}");
            session.setAttribute("email", email);
            session.setAttribute("username", username);
            return;
        }
    }

    private void binding(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String email = request.getParameter("mail");
        String username = request.getParameter("username");
        String code = request.getParameter("input1");//验证码
        HttpSession session = request.getSession();
        if (username.equals(null) || username.equals("")) {
            response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:用户账号不能为空，请重新输入！\"}");
            return;
        }
        if (username != null && username != "") {
            boolean flag = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("^[A-Za-z0-9]+$");
            m1 = p3.matcher(username);
            flag = m1.matches();
            if (flag == false || username.length() < 6 || username.length() > 30) {
                response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！\"}");
                return;
            }
        }
        if (email.equals(null) || email.equals("")) {
            response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
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
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的邮箱格式不正确，请重新输入！\"}");
                return;
            }
        }
        try {
            if (!email.equals((String) session.getAttribute("email"))) {
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的邮箱或验证码不正确，请重新输入！\"}");
                return;
            }
            if (!username.equals((String) session.getAttribute("username"))) {
                response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的账户是无效的，请重新输入！\"}");
                return;
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"尊敬的用户:你输入的邮箱或验证码不正确，请重新输入！\"}");
            return;
        }
        User user = userService.username(username);
        if (user == null) {
            response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的账号并没有注册过!请你注册后，再来绑定你的邮箱！\"}");
            return;
        }
        if (user.getMailstate() == 1) {
            response.getWriter().print("{\"res\": 21, \"info\":\"尊敬的" + user.getUsername() + "用户:你的邮箱已经激活了，请不要重复激活！\"}");
            return;
        }
        //被其它用户绑定的邮箱不能继续被绑定，也就是数据库存在该邮箱且已经被激活
        User user2 = userService.useremail(email);
        if (user2 != null && user2.getMailstate() == 1) {
            response.getWriter().print("{\"res\": 23, \"info\":\"尊敬的" + user.getUsername() + "用户:你输入的邮箱已经抢先被其它用户绑定了!绑定邮箱失败，请换一个邮箱呗！\"}");
            return;
        }
        if (code == null || code == "") {
            response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:验证码不能为空，请重新输入！\"}");
            return;
        }
        if (validate.equals(code)) {
            user.setMailstate(1);//激活邮箱
            user.setEmail(email);//绑定新邮箱
            int res = userService.updatemail(user.getUsername(), user.getEmail(), user.getMailstate());//更新数据插入数据库
            if (res == 1) {
                User user1 = (User) request.getSession().getAttribute("user");
                if (user1 != null) {
                    user1.setEmail(user.getEmail());
                    user1.setMailstate(1);
                    request.getSession().setAttribute("user", user1);
                }
                response.getWriter().print("{\"res\": 1, \"info\":\"绑定邮箱成功\"}");
                validate = "";
                return;
            } else {
                response.getWriter().print("{\"res\": 17, \"info\":\"服务器发生错误，请及时联系网站管理员\"}");
                return;
            }

        } else {
            response.getWriter().print("{\"res\": 8, \"info\":\"验证码错误，绑定邮箱失败\"}");
            return;
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, MessagingException, ParseException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String verifyCode = request.getParameter("verifyCode");
        String password1 = password;
        // VerifyCodeServlet会把真正的验证码保存到session中
        String rememberme = request.getParameter("rememberme");//记住登陆
        String vcode = (String) request.getSession().getAttribute("vCode");
        if (!verifyCode.equalsIgnoreCase(vcode)) {
            response.getWriter().print("{\"res\": 7, \"info\":\"验证码输入错误，请重新输入！\"}");
            return;
        }
        if (username == null || username.trim().length() < 6 || username.trim().length() > 16 || password == null
                || password.trim().length() < 6 || password.trim().length() > 16) {
            // 信息有问题重新登录
            response.getWriter().print("{\"res\": -1, \"info\":\"登录信息填写有误，请不要带有非法字符！\"}");
        }
        Md5Encrypt md5 = new Md5Encrypt();
        try {
            password = md5.Encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = userService.userLogin(username, password);

        if (user == null) {
            // 登录失败 用户名或密码错误
            response.getWriter().print("{\"res\": -1, \"info\":\"用户名或密码错误，请重新输入！\"}");
        } else if (user.getState() == -1) {
            // 登录失败 帐号被封
            response.getWriter().print("{\"res\": -1, \"info\":\"你的账号已被禁用！\"}");
        } else {
            // 用户登录重复判断

            // 登录成功
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", username);
            ServletContext application = this.getServletContext();
            // 获取application对象中user
            ArrayList<String> users = (ArrayList<String>) application.getAttribute("user");
            if (users != null && users.contains(username)) {
                SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time_2 = dfs.format(new Date());
                Date begin = dfs.parse(user.getLogintime());
                Date end = dfs.parse(time_2);
                long miao = (end.getTime() - begin.getTime()) / 1000;
                //除以1000是为了转换成秒
                long fen = miao / 60;
                if (fen > 30) {
                    ServletContext application1 = this.getServletContext();
                    application1.removeAttribute("user");
                    if (request.getSession().getAttribute("user") != null) {
                        request.getSession().invalidate();//使session无效
                    }
                    login(request, response);
//					response.getWriter().print("{\"res\": 6, \"info\":\"系统开了点小差，请再尝试一次登录！\"}");
//					return;
                } else {
                    if (request.getSession().getAttribute("user") != null) {
                        request.getSession().invalidate();//使session无效
                    }
                    response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的" + user.getUsername() + "用户：你的账号已在其它地方登陆,你被迫下线,你暂时无法登录！\"}");
                    return;
                }
            } else {
                if (users == null)// 如果当前application中没有user，初始化user对象
                {
                    users = new ArrayList<String>();
                }
                users.add(username);
                application.setAttribute("user", users);
            }
//			if(SingleLogin.isAlreadyEnter(session,user.getUsername())){
//				response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的"+user.getUsername()+"用户：你的账号已在其它地方登陆,被迫下线,你暂时无法登录！\"}");
//                request.getSession().invalidate();//使session无效
//				return;
//			}else {
            Date day = new Date();
            SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//登录时间
            userService.insertlogintime(username, da.format(day));
            userService.insertLoginNum(username);//增加登录次数
            Gson gson = new Gson();
            String dataJSON = gson.toJson(user);
            //存入cookie
            if (rememberme != null) {
                //创建两个Cookie对象
                Cookie nameCookie = new Cookie("username", username);
                //设置Cookie的有效期为3天
                nameCookie.setMaxAge(60 * 60 * 24 * 3);
                Cookie pwdCookie = new Cookie("password", password1);
                pwdCookie.setMaxAge(60 * 60 * 24 * 3);
                response.addCookie(nameCookie);
                response.addCookie(pwdCookie);
            }
            if (user.getMailstate() == 0) {
                response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的" + user.getUsername() + "用户:登录成功，但是你的邮箱账号还没被激活，是否前去激活！\"}");
            } else {
                response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
            }//已经被激活的邮箱才在登录时发送到激活邮箱账号提醒用户登录
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
                    messageEmail.setSubject("[爱之家科技有限公司]登录提醒");
                    messageEmail.setMsg(SendEmail.sendlogin("http://www.lidiwen.club/muke_Web", IPUtil.getIP(request), user.getUsername(), time));
                    try {
                        SendEmail.sslSend(messageEmail);//发送邮件
                    } catch (SMTPAddressFailedException e) {
                        System.out.println("登录时发送邮件异常");
                    } catch (SendFailedException e) {
                        System.out.println("登录时发送邮件失败");
                    }
                }
            }
            Userlog userlog = new Userlog();
            userlog.setLogintime(da.format(day));
            userlog.setUserid(user.getUserid());
            String ip = IPUtil.getIpAdrress(request);
            userlog.setIp(ip);
            userlog.setPlace(PlaceUtil.baiduGetCityCode(ip));
            userService.insertloginLog(userlog);//插入登陆日志
        }
    }

    //忘记密码通过绑定的邮箱重置密码
    private void updatePw2(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String code = request.getParameter("code");
        String mail = request.getParameter("mail");
        String password = request.getParameter("newpassword");
        String password2 = request.getParameter("newpassword2");

        if (code == null || code == "") {
            response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:验证码不能为空，请重新输入！\"}");
            return;
        }
        if (mail == null || mail.equals("")) {
            response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
            return;
        }
        if (mail != null && mail != "") {
            boolean flag1 = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            m1 = p3.matcher(mail);
            flag1 = m1.matches();
            if (flag1 == false) {
                response.getWriter().print("{\"res\": 7, \"info\":\"尊敬的用户:你输入的邮箱格式不正确，请重新输入！\"}");
                return;
            }
        }
        if (password == null || password.equals("") || password.length() < 6 || password.length() > 30) {
            response.getWriter().print("{\"res\": 4, \"info\":\"尊敬的用户:新密码长度必须在6到30之间，请重新输入！\"}");
            return;
        }
        if (password != null && password != "") {
            boolean flag8 = false;
            Pattern p4 = null;
            Matcher m2 = null;
            p4 = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
            m2 = p4.matcher(password);
            flag8 = m2.matches();
            if (flag8 == false) {
                response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的新密码不合法，不能包含特殊字符和空格，请重新输入！\"}");
                return;
            }
        }
        if (password2 == null || password2.equals("") || password2.length() < 6 || password2.length() > 30) {
            response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入确认密码！\"}");
            return;
        }
        if (!password.equals(password2)) {
            response.getWriter().print("{\"res\": 5, \"info\":\"尊敬的用户:你输入的确认密码跟新密码不匹配，请仔细核对确认密码后再输入！\"}");
            return;
        }
        if (!userService.isExistmailBind(mail)) {
            response.getWriter().print("{\"res\": -1, \"info\":\"邮箱或验证码错误！\"}");
            return;
        }
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute(mail + "resetPassCode").equals(code + mail)) {
                Md5Encrypt md5 = new Md5Encrypt();
                try {
                    password = md5.Encrypt(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userService.updatePwBynewPass(password, mail) > 0) {
                    response.getWriter().print("{\"res\": 1, \"info\":\"重置密码成功！请用修改后的密码登录\"}");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                    String time = df.format(new Date());
                    MessageEmail messageEmail = new MessageEmail();
                    List<String> str = new ArrayList<String>();
                    str.add(mail);
                    messageEmail.setFrom("1632029393@qq.com");
                    messageEmail.setSubject("[爱之家科技有限公司]登录密码修改成功");
                    messageEmail.setTo(str);
                    messageEmail.setMsg(SendEmail.sendupdatepass2("http://www.lidiwen.club/muke_Web", IPUtil.getIP(request), userService.useremail(mail).getUsername(), time));
                    try {
                        SendEmail.sslSend(messageEmail);//发送邮件
                        return;
                    } catch (SendFailedException e) {
                        System.out.println("修改密码时发送邮件失败");
                        return;
                    }
                } else {
                    response.getWriter().print("{\"res\": -1, \"info\":\"系统出了点小问题，重置密码失败！请稍后重试\"}");
                    return;
                }
            } else {
                response.getWriter().print("{\"res\": -1, \"info\":\"邮箱或验证码错误！\"}");
                return;
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"验证码错误！\"}");
            return;
        }
    }

    private void sendResetPassCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, MessagingException {
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        String mail = request.getParameter("mail");
        if (mail == null || mail.equals("")) {
            response.getWriter().print("{\"res\": -1, \"info\":\"无效邮箱！\"}");
            return;
        }
        if (mail != null && mail != "") {
            boolean flag1 = false;
            Pattern p3 = null;
            Matcher m1 = null;
            p3 = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            m1 = p3.matcher(mail);
            flag1 = m1.matches();
            if (flag1 == false) {
                response.getWriter().print("{\"res\": -1, \"info\":\"无效邮箱！\"}");
                return;
            }
        }
        if (userService.isExistmail(mail)) {
            response.getWriter().print("{\"res\": -1, \"info\":\"邮箱未注册过，请重填\"}");
            return;
        }
        if (userService.isExistmailBind(mail)) {
            MessageEmail messageEmail = new MessageEmail();
            List<String> str = new ArrayList<String>();
            str.add(mail);
            messageEmail.setFrom("1632029393@qq.com");
            messageEmail.setTo(str);
            messageEmail.setSubject("[爱之家科技有限公司]重置密码通知");
            messageEmail.setMsg(SendEmail.sendforgetpass(random, IPUtil.getIP(request), "http://www.lidiwen.club/muke_Web"));
            try {
                SendEmail.sslSend(messageEmail);//发送邮件
                HttpSession session = request.getSession();
                session.setAttribute(mail + "resetPassCode", random + mail);
                session.setMaxInactiveInterval(60 * 30);//30分钟内有效
                response.getWriter().print("{\"res\": 1, \"info\":\"用户验证成功，发送邮件成功！请你及时登录邮箱查看。若您没有收到邮件，请一分钟后核对邮箱重新验证！\"}");
                return;
            } catch (SendFailedException e) {
                System.out.println("重置密码时发送邮件失败");
                response.getWriter().print("{\"res\": -1, \"info\":\"邮件发送失败，请稍后重试！\"}");
                return;
            }

        } else {
            response.getWriter().print("{\"res\": -2, \"info\":\"邮箱已注册但未被绑定，请先去绑定后才能通过该方式重置密码\"}");
            return;
        }

    }


    // 查看用户个人中心信息
    private void getUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        String username = request.getParameter("username");
        User user = userService.username(username);
        if (username.equals(null) || username == "" || user == null) {
            response.getWriter().print("{\"res\":-1,\"message\":\"该用户不存在！\"}");
            return;
        }
        Gson gson = new Gson();
        user.setPassword("********");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createtime = df.format(user.getCreatetime());
        String json = gson.toJson(user);

        //查询用户发表的帖子
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        int userid = user.getUserid();
        //创建封装查询条件对象
        MessageCriteria messageCriteria = new MessageCriteria();
        messageCriteria.setUserid(userid);
        messageCriteria.setOrderRule(MessageCriteria.OrderRuleEnum.ORDER_BY_MSG_TIME);//排序条件
        messageCriteria.setState(0);//查询非禁用状态
        page = messageservice.searchUserCnterMsg(messageCriteria, page);
        gson = new GsonBuilder().setDateFormat("yy-MM-dd").create();
        String json2 = gson.toJson(page);
        long replycount = messageservice.queryReplyCount(userid);//回复总数
        long replydistinctcount = messageservice.queryDistinctReplyCount(userid);//回复帖子数量
        int flag = 0;
        if (user.getDescription() != null && user.getDescription().trim().length() > 0) {
            flag = 1;
        }
        response.getWriter().print("{\"res\":1,\"message\":" + json + ",\"message2\":" + json2 + ",\"replycount\":" + replycount + ",\"replydistinctcount\":" + replydistinctcount + ",\"flag\":" + flag + ",\"createtime\":\"" + createtime + "\"}");
    }

    //获取回复过的帖子信息
    private void getReply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
        String username = request.getParameter("username");
        User user = userService.username(username);
        if (username.equals(null) || username == "" || user == null) {
            response.getWriter().print("{\"res\":-1,\"message\":\"该用户不存在！\"}");
            return;
        }
        //查询用户回复过的帖子
        Gson gson;
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        int userid = user.getUserid();
        //创建封装查询条件对象
        MessageCriteria messageCriteria = new MessageCriteria();
        messageCriteria.setUserid(userid);
        messageCriteria.setOrderRule(MessageCriteria.OrderRuleEnum.ORDER_BY_MSG_TIME);//排序条件
        messageCriteria.setState(0);//查询非禁用状态
        page = messageservice.queryUserCenterReply(messageCriteria, page);
        gson = new GsonBuilder().setDateFormat("yy-MM-dd").create();
        String json = gson.toJson(page);

        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_2 = dfs.format(new Date());
        Date begin = dfs.parse(dfs.format(user.getCreatetime()));
        Date end = dfs.parse(time_2);
        long mss = (end.getTime() - begin.getTime());
        long day = mss / (1000 * 60 * 60 * 24);
        response.getWriter().print("{\"res\":1,\"message\":" + json + ",\"day\":" + day + "}");

    }

    private void StartCaptcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String resStr = "{}";

        String userid = request.getParameter("username");
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userid); //网站用户id
        if (JudgeIsMoblieUtil.JudgeIsMoblie(request)) {
            param.put("client_type", "h5");
        } else {
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        }
        param.put("ip_address", IPUtil.getIpAdrress(request)); //传输用户请求验证时所携带的IP

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        //将userid设置到session中
        request.getSession().setAttribute("userid", userid);

        resStr = gtSdk.getResponseStr();

        PrintWriter out = response.getWriter();
        out.println(resStr);
    }

    private void VerifyLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException, MessagingException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey) == null) {
            response.getWriter().print("{\"res\": -2, \"info\":\"抱歉,验证失败！\"}");
            return;
        }
        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
//        //从session中获取userid
//        String userid = (String) request.getSession().getAttribute("userid");

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", username); //网站用户id
        if (JudgeIsMoblieUtil.JudgeIsMoblie(request)) {
            param.put("client_type", "h5");
        } else {
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        }
        param.put("ip_address", IPUtil.getIpAdrress(request)); //传输用户请求验证时所携带的IP

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        if (gtResult == 1) {
            // 验证成功
            String password1 = password;
            String rememberme = request.getParameter("rememberme");//记住登陆
            if (username == null || username.trim().length() < 6 || username.trim().length() > 30 || password == null
                    || password.trim().length() < 6 || password.trim().length() > 30) {
                // 信息有问题重新登录
                response.getWriter().print("{\"res\": -1, \"info\":\"登录信息填写有误，请不要带有非法字符！\"}");
                return;
            }
            Md5Encrypt md5 = new Md5Encrypt();
            try {
                password = md5.Encrypt(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            User user = userService.userLogin(username, password);
            if (user == null) {
                // 登录失败 用户名或密码错误
                response.getWriter().print("{\"res\": -1, \"info\":\"用户名或密码错误，请重新输入！\"}");
                return;
            } else if (user.getState() == -1) {
                // 登录失败 帐号被封
                response.getWriter().print("{\"res\": -1, \"info\":\"你的账号已被禁用！\"}");
                return;
            } else {
                // 用户登录重复判断

                // 登录成功
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("username", username);
                ServletContext application = this.getServletContext();
                // 获取application对象中user
                ArrayList<String> users = (ArrayList<String>) application.getAttribute("user");
                if (users != null && users.contains(username)) {
                    SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time_2 = dfs.format(new Date());
                    Date begin = dfs.parse(user.getLogintime());
                    Date end = dfs.parse(time_2);
                    long miao = (end.getTime() - begin.getTime()) / 1000;
                    //除以1000是为了转换成秒
                    long fen = miao / 60;
                    if (fen > 30) {
                        for (int i = 0; i < users.size(); i++) { //循环application对象
                            //如果当前登录用户对象与保存在application中的对象相等
                            if (user.getUsername().equals(users.get(i))) {
                                users.remove(i); //从保存的LIST中清除掉登录的用户
                            }
                        }
                        try {
                            VerifyLogin(request, response);
                        } catch (NullPointerException e) {
                            response.getWriter().print("{\"res\": -2, \"info\":\"抱歉,验证失败！\"}");
                            return;
                        }
                    } else {
                        if (request.getSession().getAttribute("user") != null) {
                            request.getSession().invalidate();//使session无效
                        }
                        response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的" + user.getUsername() + "用户：你的账号已在其它地方登陆,你被迫下线,你暂时无法登录！\"}");
                        return;
                    }
                } else {
                    if (users == null)// 如果当前application中没有user，初始化user对象
                    {
                        users = new ArrayList<String>();
                    }
                    users.add(username);
                    application.setAttribute("user", users);
                }
                Date day = new Date();
                SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//登录时间
                userService.insertlogintime(username, da.format(day));
                userService.insertLoginNum(username);//增加登录次数
                Gson gson = new Gson();
                String dataJSON = gson.toJson(user);
                //存入cookie
                if (Integer.parseInt(rememberme) == 1) {
                    //创建两个Cookie对象
                    Cookie nameCookie = new Cookie("username", username);
                    //设置Cookie的有效期为3天
                    nameCookie.setMaxAge(60 * 60 * 24 * 3);
                    Cookie pwdCookie = new Cookie("password", password1);
                    pwdCookie.setMaxAge(60 * 60 * 24 * 3);
                    response.addCookie(nameCookie);
                    response.addCookie(pwdCookie);
                }
                if (user.getMailstate() == 0) {
                    response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的" + user.getUsername() + "用户:登录成功，但是你的邮箱账号还没被激活，是否前去激活！\"}");
                    return;
                } else {
                    response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
                }//已经被激活的邮箱才在登录时发送到激活邮箱账号提醒用户登录
                if (user.getEmail() != null && user.getMailstate() == 1) {
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
                        messageEmail.setSubject("[爱之家科技有限公司]登录提醒");
                        messageEmail.setMsg(SendEmail.sendlogin("http://www.lidiwen.club/muke_Web", IPUtil.getIP(request), user.getUsername(), time));
                        try {
                            SendEmail.sslSend(messageEmail);//发送邮件
                        } catch (SMTPAddressFailedException e) {
                            System.out.println("登录时发送邮件异常");
                        } catch (SendFailedException e) {
                            System.out.println("登录时发送邮件失败");
                        }
                    }
                }
                Userlog userlog = new Userlog();
                userlog.setLogintime(da.format(day));
                userlog.setUserid(user.getUserid());
                String ip = IPUtil.getIpAdrress(request);
                userlog.setIp(ip);
                userlog.setPlace(PlaceUtil.baiduGetCityCode(ip));
                userService.insertloginLog(userlog);//插入登陆日志
            }
        } else {
            // 验证失败
            response.getWriter().print("{\"res\": -2, \"info\":\"抱歉,验证失败！\"}");
            return;
        }

    }

    private void StartCaptchaRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
//        param.put("user_id", userid); //网站用户id
        if (JudgeIsMoblieUtil.JudgeIsMoblie(request)) {
            param.put("client_type", "h5");
        } else {
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        }
        param.put("ip_address", IPUtil.getIpAdrress(request)); //传输用户请求验证时所携带的IP

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        //将userid设置到session中
//        request.getSession().setAttribute("userid", userid);

        resStr = gtSdk.getResponseStr();

        PrintWriter out = response.getWriter();
        out.println(resStr);
    }

    private void VerifyRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String realname = request.getParameter("realname");
        String sex = request.getParameter("sex");
        String birthday = request.getParameter("birthday");
        String city = request.getParameter("city");
        String email = request.getParameter("email");
        String qq = request.getParameter("qq");
        String[] item = request.getParameter("hobbys").split(",");
        if (request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey) == null) {
            response.getWriter().print("{\"res\": -2, \"info\":\"抱歉,验证失败！\"}");
            return;
        }
        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
//        //从session中获取userid
//        String userid = (String) request.getSession().getAttribute("userid");

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
//        param.put("user_id", username); //网站用户id
        if (JudgeIsMoblieUtil.JudgeIsMoblie(request)) {
            param.put("client_type", "h5");
        } else {
            param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        }
        param.put("ip_address", IPUtil.getIpAdrress(request)); //传输用户请求验证时所携带的IP

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        if (gtResult == 1) {
            // 验证成功
            boolean flag3 = true;
            String delStr = "";
            if (item != null) {
                for (int i = 0; i < item.length - 1; i++) {
                    delStr += item[i] + ",";
                }
                for (int i = item.length - 1; i < item.length; i++) {
                    delStr += item[i] + ";";
                }
            }
            if (username == null || username.equals("")) {
                response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:用户账号不能为空，请重新输入！\"}");
                return;
            }
            if (username != null && username != "") {
                boolean flag = false;
                Pattern p3 = null;
                Matcher m1 = null;
                p3 = Pattern.compile("^[A-Za-z0-9]+$");
                m1 = p3.matcher(username);
                flag = m1.matches();
                if (flag == false || username.length() < 6 || username.length() > 30) {
                    flag3 = false;
                    response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！\"}");
                    return;
                }
            }
            if (password == null || password.equals("") || password.length() < 6 || password.length() > 30) {
                response.getWriter().print("{\"res\": 4, \"info\":\"尊敬的用户:密码长度必须在6到30之间，请重新输入！\"}");
                return;
            }
            if (password != null && password != "") {
                boolean flag9 = false;
                Pattern p4 = null;
                Matcher m2 = null;
                p4 = Pattern.compile("^[a-zA-Z0-9_\\.]+$");
                m2 = p4.matcher(password);
                flag9 = m2.matches();
                if (flag9 == false) {
                    response.getWriter().print("{\"res\": 28, \"info\":\"尊敬的用户:你输入的密码不合法，不能包含特殊字符和空格，请重新输入！\"}");
                    return;
                }
            }
            if (password2 == null || password2.equals("") || password2.length() < 6 || password2.length() > 30) {
                response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入！\"}");
                return;
            }
            if (!password.equals(password2)) {
                response.getWriter().print("{\"res\": 5, \"info\":\"尊敬的用户:确认密码跟新密码不匹配，请重新输入确认密码！\"}");
                return;
            }
            if (realname == null || realname.equals("")) {
                response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的用户:为了保证你的合法性，真实姓名不能为空，请如实填写！\"}");
                return;
            }
            if (realname.length() > 30) {
                response.getWriter().print("{\"res\": 30, \"info\":\"尊敬的用户:真实姓名的长度必须小于30，请重新填写！\"}");
                return;
            }
            if (item == null || item.length < 0 || item.length == 0) {
                response.getWriter().print("{\"res\": 13, \"info\":\"尊敬的用户:爱好不能为空，请至少勾选一个爱好！\"}");
                return;
            }
            if (city == null || city.equals("")) {
                response.getWriter().print("{\"res\": 7, \"info\":\"尊敬的用户:所在城市不能为空，请点击城市下拉列表选择你所在的城市！\"}");
                return;
            }
            if (email == null || email.equals("")) {
                response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
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
                    response.getWriter().print("{\"res\": 10, \"info\":\"尊敬的用户:你输入的邮箱格式不正确，请重新输入！\"}");
                    return;
                }
            }
            if (qq == null || qq.equals("")) {
                response.getWriter().print("{\"res\": 11, \"info\":\"尊敬的用户:QQ号输入不能为空，请重新输入！\"}");
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
                    response.getWriter().print("{\"res\": 12, \"info\":\"尊敬的用户:你输入的QQ格式不正确，请重新输入！\"}");
                    return;
                }
            }

            if (userService.isExist(username) == false) {
                response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的账号已经存在!注册失败，请换一个其它账号呗！\"}");
                return;
            }
            //被其它用户绑定的邮箱不能继续被绑定，也就是数据库存在该邮箱且已经被激活
            User user1 = userService.useremail(email);
            if (user1 != null && user1.getMailstate() == 1) {
                response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的邮箱已经被注册了!注册失败，请换一个邮箱呗！\"}");
                return;
            }
            User user = new User();
            user.setUsername(username);
            Md5Encrypt md5 = new Md5Encrypt();
            try {
                password = md5.Encrypt(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            user.setPassword(password);
            user.setRealname(realname);
            if (sex.equals("男")) {
                user.setUser_img("image/nan.png");
            } else {
                user.setUser_img("image/nu.png");
            }
            user.setSex(sex);

            user.setHobbys(delStr);
            user.setBirthday(birthday);
            user.setCity(city);
            user.setEmail(email);
            user.setQq(qq);
            user.setLoginNum(1);
            Date day = new Date();
            SimpleDateFormat da = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//登录时间
            user.setLogintime(da.format(day));
            try {

                int res = userService.userRegister(user);
                if (res == 1) {
                    // 自动登录
                    user = userService.userLogin(username, password);
                    // 登录成功
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("username", username);
                    response.getWriter().print("{\"res\": 1, \"info\":\"注册成功\"}");
                    Userlog userlog = new Userlog();
                    userlog.setLogintime(da.format(day));
                    userlog.setUserid(user.getUserid());
                    String ip = IPUtil.getIpAdrress(request);
                    userlog.setIp(ip);
                    userlog.setPlace(PlaceUtil.baiduGetCityCode(ip));
                    userService.insertloginLog(userlog);//注册成功后自动登录也要插入登陆日志
                    return;
                } else {
                    response.getWriter().print("{\"res\": " + res + ", \"info\":\"注册失败，尊敬的用户:你输入的账号已经存在，请换一个其它账号呗！\"}");
                    return;
                }
            } catch (Exception e) {
                response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的账号已经存在!注册失败，请换一个其它账号呗！\"}");
                return;
            }
        } else {
            // 验证失败
            response.getWriter().print("{\"res\": -2, \"info\":\"抱歉,验证失败！\"}");
        }

    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
