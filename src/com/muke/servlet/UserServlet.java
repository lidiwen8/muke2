package com.muke.servlet;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.muke.pojo.EmailAccout;
import com.muke.pojo.MessageEmail;
import com.muke.pojo.User;
import com.muke.service.IUserService;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.IPUtil;
import com.muke.util.Md5Encrypt;
import com.muke.util.SendEmail;
import com.muke.util.SingleLogin;
import com.sun.mail.smtp.SMTPAddressFailedException;

@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {
    IUserService userService = new UserServiceImpl();
	private static final long serialVersionUID = 4122206234828080374L;
	private  static  String validate="";

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

	//验证用户名是否存在
	private void isExist(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username=request.getParameter("username");
		boolean result=userService.isExist(username);
		response.getWriter().print("{\"valid\":"+result+"}");
	}

	//网站建议
	private void help(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String advise=request.getParameter("advise");
		String number=request.getParameter("number");
		int result=userService.saveAdvise(advise,number);
		if(result==1){
			response.getWriter().print("{\"res\": 1, \"info\":\"提交成功！你的建议我们已经收到，我们将认真参考你宝贵的意见，谢谢\"}");
			return;
		}else{
			response.getWriter().print("{\"res\": -1, \"info\":\"提交失败！\"}");
			return;
		}

	}
	//检验用户账号是不是可用
	private void checkUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username=request.getParameter("username");
		if(userService.isExist(username)==false){
			response.getWriter().print("{\"res\": -1, \"info\":\"账号已存在\"}");
		}else{
			response.getWriter().print("{\"res\": 1, \"info\":\"该账号可用！\"}");
		}

	}
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(request.getSession().getAttribute("user") != null) {
			ServletContext application = this.getServletContext();
			request.getSession().invalidate();//使session无效
			application.removeAttribute("user");//从application中移除user使session无效
		}
		response.getWriter().print("{\"res\": 1, \"info\":\"欢迎下次登录！\"}");
	}

	private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String realname = request.getParameter("realname");
		String sex = request.getParameter("sex");
		boolean flag3=true;

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

		if(username.equals(null)||username.equals("")){
			response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:用户账号不能为空，请重新输入！\"}");
			return;
		}
		if(username!=null&&username!=""){
			boolean flag = false;
			Pattern p3 = null;
			Matcher m1 = null;
			p3 = Pattern.compile("^[A-Za-z0-9]+$");
			m1 = p3.matcher(username);
			flag = m1.matches();
			if (flag== false||username.length()<6||username.length()>30) {
				flag3=false;
				response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！\"}");
				return;
			}
		}
		if(password.equals(null)||password.equals("")||password.length()<6||password.length()>30){
			response.getWriter().print("{\"res\": 4, \"info\":\"尊敬的用户:密码长度必须在6到30之间，请重新输入！\"}");
			return;
		}
		if(password!=null&&password!="") {
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
		if(password2.equals(null)||password2.equals("")||password2.length()<6||password2.length()>30){
			response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入！\"}");
			return;
		}
		if(!password.equals(password2)){
			response.getWriter().print("{\"res\": 5, \"info\":\"尊敬的用户:确认密码跟新密码不匹配，请重新输入确认密码！\"}");
			return;
		}
		if(realname.equals(null)||realname.equals("")){
			response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的用户:为了保证你的合法性，真实姓名不能为空，请如实填写！\"}");
			return;
		}
		if(realname.length()>30){
			response.getWriter().print("{\"res\": 30, \"info\":\"尊敬的用户:真实姓名的长度必须小于30，请重新填写！\"}");
			return;
		}
		try {
			if (item.length == 0 || item.length < 0 || item.equals(null)) {
				response.getWriter().print("{\"res\": 13, \"info\":\"尊敬的用户:爱好不能为空，请至少勾选一个爱好！\"}");
				return;
			}
		}catch (NullPointerException e){
			response.getWriter().print("{\"res\": 13, \"info\":\"尊敬的用户:爱好不能为空，请至少勾选一个爱好！\"}");
			return;
		}
		if(city.equals(null)||city.equals("")){
			response.getWriter().print("{\"res\": 7, \"info\":\"尊敬的用户:所在城市不能为空，请点击城市下拉列表选择你所在的城市！\"}");
			return;
		}
		if(email.equals(null)||email.equals("")){
			response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
			return;
		}
		if(email!=null&&email!="") {
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
		if(qq.equals(null)||qq.equals("")){
			response.getWriter().print("{\"res\": 11, \"info\":\"尊敬的用户:QQ号输入不能为空，请重新输入！\"}");
			return;
		}
		if(qq!=null&&qq!="") {
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

		if(userService.isExist(username)==false){
			response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的账号已经存在!注册失败，请换一个其它账号呗！\"}");
			return;
		}
		if(userService.isExistmail(email)==false){
			response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的邮箱已经被注册了!注册失败，请换一个邮箱呗！\"}");
			return;
		}
		User user = new User();
		user.setUsername(username);
		Md5Encrypt md5=new Md5Encrypt();
		try {
			password=md5.Encrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setPassword(password);
		user.setRealname(realname);
		user.setSex(sex);
		
		user.setHobbys(delStr);
		user.setBirthday(birthday);
		user.setCity(city);
		user.setEmail(email);
		user.setQq(qq);
		if(user.getSex().equals("男")){
			String virtualPath="image/nan.png";
			userService.saveUserimg(username, virtualPath);
		}else{
			String virtualPath="image/nu.png";
			userService.saveUserimg(username, virtualPath);
		}
		try {

				int res = userService.userRegister(user);
				if (res == 1) {
					// 自动登录
					user = userService.userLogin(username, password);
					// 登录成功
					HttpSession session = request.getSession();
					session.setAttribute("user", user);

					Gson gson = new Gson();
					String dataJSON = gson.toJson(user);
					response.getWriter().print("{\"res\": 1, \"info\":\"注册成功\"}");
					return;
				} else {
					response.getWriter().print("{\"res\": " + res + ", \"info\":\"注册失败，尊敬的用户:你输入的账号已经存在，请换一个其它账号呗！\"}");
					return;
				}
		}catch (Exception e){
			response.getWriter().print("{\"res\": 19, \"info\":\"尊敬的用户:你输入的账号已经存在!注册失败，请换一个其它账号呗！\"}");
			return;
		}

	}
	private void emailpass(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
		String email = request.getParameter("mail");
		String random=(int)((Math.random()*9+1)*100000)+"";
		String verifyCode = request.getParameter("verifyCode");
		if(email.equals(null)||email.equals("")){
			response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
			return;
		}
		if(email!=null&&email!="") {
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
		if(!verifyCode.equalsIgnoreCase(vcode)){
			response.getWriter().print("{\"res\": 7, \"info\":\"验证码输入错误，请重新输入！\"}");
			return;
		}
		if(userService.isExistmail(email)==true){
			response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的邮箱号有误!用户认证失败，请重新输入！\"}");
			return;
		}
		User user=userService.useremail(email);
		if(user.getMailstate()==1) {
			Md5Encrypt md5 = new Md5Encrypt();
			MessageEmail messageEmail = new MessageEmail();
			EmailAccout emailAccout = new EmailAccout();
			SendEmail sendEmail = new SendEmail();
			List<String> strs = new ArrayList<String>();
			strs.add(user.getEmail());
			emailAccout.setUsername("1632029393@qq.com");
			emailAccout.setPassword("jqrvtnbapfzzcaec");
			emailAccout.setPlace("smtp.qq.com");
			messageEmail.setFrom("1632029393@qq.com");
			messageEmail.setTo(strs);
			messageEmail.setMsg(sendEmail.sendMsg(user.getEmail(), random, user.getUsername()));
			try {
				random = md5.Encrypt(random);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (sendEmail.sslSend(messageEmail, emailAccout)) {
					user.setPassword(random);
					userService.updatePw(user);
					response.getWriter().print("{\"res\": 1, \"info\":\"尊敬的用户：用户验证成功，发送邮件成功，请你及时登录邮箱查看\"}");
					return;
				} else {
					response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户：用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员\"}");
					return;
				}
			}catch (SendFailedException e){
				response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的用户：用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员\"}");
				return;
			}
		}else{
			response.getWriter().print("{\"res\": 27, \"info\":\"尊敬的用户：你的该邮箱账号并没有被激活，不能通过此方式来找回密码，请激活后再通过邮箱账号重置密码！\"}");
			return;
		}
//		sendEmail.sendMail(user.getEmail(),"123456",user.getUsername());
//		user.setPassword(password);
//		userService.updatePw(user);
//		response.getWriter().print("{\"res\": 1, \"info\":\"用户验证成功，发送邮件成功，请你及时登录邮箱查看\"}");
//		return;
	}

	private void bindingmail(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
		String email = request.getParameter("mail");
		String username = request.getParameter("username");
		String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
		validate = random;
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
		//被其它用户绑定的邮箱不能继续被绑定，也就是数据库存在该邮箱且已经被激活
		User user1=userService.useremail(email);
		if(user1!=null&&user1.getMailstate()==1){
			response.getWriter().print("{\"res\": 23, \"info\":\"尊敬的" + user.getUsername()+ "用户:你输入的邮箱已经抢先被其它用户绑定了!绑定邮箱失败，请换一个邮箱呗！\"}");
			return;
		}
		MessageEmail messageEmail = new MessageEmail();
		EmailAccout emailAccout = new EmailAccout();
		SendEmail sendEmail = new SendEmail();
		List<String> strs = new ArrayList<String>();
		strs.add(email);
		emailAccout.setUsername("1632029393@qq.com");
		emailAccout.setPassword("jqrvtnbapfzzcaec");
		emailAccout.setPlace("smtp.qq.com");
		messageEmail.setFrom("1632029393@qq.com");
		messageEmail.setTo(strs);
		messageEmail.setMsg(sendEmail.sendbindingmail(random, IPUtil.getIP(request), user.getUsername()));
		try {
			try {
				if (sendEmail.sslSend(messageEmail, emailAccout)) {
					response.getWriter().print("{\"res\": 1, \"info\":\"尊敬的用户：用户验证成功，发送邮件成功，请你及时登录邮箱查看\"}");
					return;
				} else {
					response.getWriter().print("{\"res\": 8, \"info\":\"尊敬的" + user.getUsername() + "用户：用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员\"}");
					return;
				}
			}catch (SendFailedException e){
				response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的用户：发送邮件失败，你输入的邮件地址是无效的，请重新输入\"}");
				return;
			}
		}catch (SMTPAddressFailedException e){
			response.getWriter().print("{\"res\": 1, \"info\":\"尊敬的用户：用户验证成功，发送邮件成功！请你及时登录邮箱查看。若您没有收到邮件，请一分钟后核对邮箱重新验证。\"}");
			return;
		}
	}
	private void binding(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
		String email = request.getParameter("mail");
		String username = request.getParameter("username");
		String code = request.getParameter("input1");//验证码
		if(username.equals(null)||username.equals("")){
			response.getWriter().print("{\"res\": 3, \"info\":\"尊敬的用户:用户账号不能为空，请重新输入！\"}");
			return;
		}
		if(username!=null&&username!=""){
			boolean flag = false;
			Pattern p3 = null;
			Matcher m1 = null;
			p3 = Pattern.compile("^[A-Za-z0-9]+$");
			m1 = p3.matcher(username);
			flag = m1.matches();
			if (flag== false||username.length()<6||username.length()>30) {
				response.getWriter().print("{\"res\": 9, \"info\":\"尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！\"}");
				return;
			}
		}
		if(email.equals(null)||email.equals("")){
			response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的用户:邮箱号输入不能为空，请重新输入！\"}");
			return;
		}
		if(email!=null&&email!="") {
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
		User user=userService.username(username);
		if(user==null){
			response.getWriter().print("{\"res\": 20, \"info\":\"尊敬的用户:你输入的账号并没有注册过!请你注册后，再来绑定你的邮箱！\"}");
			return;
		}
		if(user.getMailstate()==1){
			response.getWriter().print("{\"res\": 21, \"info\":\"尊敬的"+user.getUsername()+"用户:你的邮箱已经激活了，请不要重复激活！\"}");
			return;
		}

		if(validate.equals(code)){
			user.setMailstate(1);//激活邮箱
			user.setEmail(email);//绑定新邮箱
			int res =userService.updatemail(user.getUsername(),user.getEmail(),user.getMailstate());//更新数据插入数据库
			if(res==1){
				response.getWriter().print("{\"res\": 1, \"info\":\"绑定邮箱成功\"}");
				return;
			}else{
				response.getWriter().print("{\"res\": 17, \"info\":\"服务器发生错误，请及时联系网站管理员\"}");
				return;
			}

		}else
		{
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
      if(!verifyCode.equalsIgnoreCase(vcode)){
		  response.getWriter().print("{\"res\": 7, \"info\":\"验证码输入错误，请重新输入！\"}");
		  return;
	  }
		if (username == null || username.trim().length() < 6 || username.trim().length() > 16 || password == null
				|| password.trim().length() < 6 || password.trim().length() > 16) {
			// 信息有问题重新登录
			response.getWriter().print("{\"res\": -1, \"info\":\"登录信息填写有误，请不要带有非法字符！\"}");
		}
		Md5Encrypt md5=new Md5Encrypt();
		try {
			password=md5.Encrypt(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		User user = userService.userLogin(username, password);
		
		if (user == null){
			// 登录失败 用户名或密码错误
			response.getWriter().print("{\"res\": -1, \"info\":\"用户名或密码错误，请重新输入！\"}");
		}
		else if(user.getState() == -1){
			// 登录失败 帐号被封
			response.getWriter().print("{\"res\": -1, \"info\":\"你的账号已被禁用！\"}");
		}
		else{
			// 用户登录重复判断


			// 登录成功
			userService.insertlogintime(username);
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			session.setAttribute("username",username);
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
				long fen=miao/60;
				if(fen>30){
					ServletContext application1 = this.getServletContext();
					application1.removeAttribute("user");
					response.getWriter().print("{\"res\": 6, \"info\":\"系统开了点小差，请再尝试一次登录！\"}");
					return;
				}else{
					response.getWriter().print("{\"res\": 6, \"info\":\"尊敬的"+user.getUsername()+"用户：你的账号已在其它地方登陆,你被迫下线,你暂时无法登录！\"}");
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
                Gson gson = new Gson();
                String dataJSON = gson.toJson(user);
                //存入cookie
				if(rememberme!=null) {
					//创建两个Cookie对象
					Cookie nameCookie = new Cookie("username", username);
					//设置Cookie的有效期为3天
					nameCookie.setMaxAge(60 * 60 * 24 * 3);
					Cookie pwdCookie = new Cookie("password", password1);
					pwdCookie.setMaxAge(60 * 60 * 24 * 3);
					response.addCookie(nameCookie);
					response.addCookie(pwdCookie);
				}
				if(user.getMailstate()==0){
					response.getWriter().print("{\"res\": 2, \"info\":\"尊敬的"+user.getUsername()+"用户:登录成功，但是你的邮箱账号还没被激活，是否前去激活！\"}");
				}
				else {
					response.getWriter().print("{\"res\": 1, \"data\":" + dataJSON + "}");
				}//已经被激活的邮箱才在登录时发送到激活邮箱账号提醒用户登录
                if(user.getMailstate()==1&&user.getEmail()!=null&&user.getEmail()!=""){
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
						EmailAccout emailAccout = new EmailAccout();
						SendEmail sendEmail = new SendEmail();
						List<String> strs = new ArrayList<String>();
						strs.add(user.getEmail());
						emailAccout.setUsername("1632029393@qq.com");
						emailAccout.setPassword("jqrvtnbapfzzcaec");
						emailAccout.setPlace("smtp.qq.com");
						messageEmail.setFrom("1632029393@qq.com");
						messageEmail.setTo(strs);
						messageEmail.setMsg(sendEmail.sendlogin("http://www.lidiwen.club/muke_Web", IPUtil.getIP(request), user.getUsername(), time));
						try {
							sendEmail.sslSend(messageEmail, emailAccout);//发送邮件
						}catch (SMTPAddressFailedException e){
							System.out.println("登录时发送邮件异常");
						}catch (SendFailedException e){
							System.out.println("登录时发送邮件失败");
						}
					}
				}
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
