package com.muke.util;




import com.muke.pojo.EmailAccout;
import com.muke.pojo.MessageEmail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class SendEmail {

	public static void main(String [] args) throws MessagingException

	{

		Properties props = new Properties();

		// 开启debug调试

		props.setProperty("mail.debug", "true");

		// 发送服务器需要身份验证

		props.setProperty("mail.smtp.auth", "true");

		// 设置邮件服务器主机名
//		props.setProperty("mail.host", "smtp.163.com");
		props.setProperty("mail.host", "smtp.163.com");

		// 发送邮件协议名称

		props.setProperty("mail.transport.protocol", "smtp");

		// 设置环境信息

		Session session = Session.getInstance(props);



		// 创建邮件对象

		Message msg = new MimeMessage(session);

		try {

			msg.setSubject("爱之家科技有限公司");

		} catch (MessagingException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		// 设置邮件内容

		msg.setText("找回密码：                                           "
				+"              " +
				"        尊敬的"+"admins"+"用户:已经将你的密码重置为初始密码<"+"123456"+">，请妥善保管；为了确保你的账户安全，登陆后请在个人中心尽快修改当前密码。");


		// 设置发件人

		msg.setFrom(new InternetAddress("18189530509@163.com"));



		Transport transport = session.getTransport();

		// 连接邮件服务器     xzbbrvnlsjpdbfei
//		transport.connect("cn_edu_njust@163.com", "lty123456789");
//		transport.connect("18189530509@163.com", "126137mp");

		transport.connect("18189530509@163.com", "126137mp");

		// 发送邮件

		transport.sendMessage(msg, new Address[] {new InternetAddress("1632029393@qq.com")});

		// 关闭连接

		transport.close();

	}
	public  void sendMail(String adress,String password,String username) throws MessagingException

	{

		Properties props = new Properties();

		// 开启debug调试

		props.setProperty("mail.debug", "true");

		// 发送服务器需要身份验证

		props.setProperty("mail.smtp.auth", "true");

		// 设置邮件服务器主机名
//		props.setProperty("mail.host", "smtp.163.com");
		props.setProperty("mail.host", "smtp.163.com");

		// 发送邮件协议名称

		props.setProperty("mail.transport.protocol", "smtp");

		// 设置环境信息

		Session session = Session.getInstance(props);



		// 创建邮件对象

		Message msg = new MimeMessage(session);

		try {

			msg.setSubject("爱之家科技有限公司");

		} catch (MessagingException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		// 设置邮件内容

		msg.setText("         尊敬的"+username+"用户:已经将你的密码重置为初始密码<"+password+">，请妥善保管；为了确保你的账户安全，登陆后请在个人中心尽快修改当前密码。");


		// 设置发件人

		msg.setFrom(new InternetAddress("cn_edu_njust@163.com"));



		Transport transport = session.getTransport();

		// 连接邮件服务器     xzbbrvnlsjpdbfei
		transport.connect("cn_edu_njust@163.com", "lty123456789");
//		transport.connect("18189530509@163.com", "126137mp");

//		transport.connect("1632029393@qq.com", "jqrvtnbapfzzcaec");

		// 发送邮件

		transport.sendMessage(msg, new Address[] {new InternetAddress(adress)});

		// 关闭连接

		transport.close();

	}
	public  String sendMsg(String adress,String password,String username) throws MessagingException

	{
       String msg="         【爱之家科技有限公司】尊敬的"+username+"用户:已经将你的密码重置为初始密码<"+password+">，请妥善保管；为了确保你的账户安全，登陆后请在个人中心尽快修改当前密码。";
		return  msg;
	}
	public  String sendlogin(String adress,String ip,String username,String time) throws MessagingException

	{
		String msg="         【爱之家科技有限公司】尊敬的"+username+"用户:您于"+time+"登录爱之家网站答疑平台"+adress+",登录地址:"+ip+"。如非本人操作,请立即登录修改密码。";
		return  msg;
	}

	public  String sendupdatepass(String adress,String ip,String username,String time) throws MessagingException

	{
		String msg="         【爱之家科技有限公司】尊敬的"+username+"用户:您于"+time+"登录爱之家网站答疑平台"+adress+"修改了你的密码,登录地址:"+ip+"。如非本人操作,请立即使用你注册时绑定的邮箱进行密码找回。";
		return  msg;
	}

	public  String sendupdatepass2(String adress,String ip,String username,String time) throws MessagingException

	{
		String msg="         【爱之家科技有限公司】尊敬的"+username+"用户:您于"+time+"在爱之家网站答疑平台"+adress+"修改了你的密码,操作地址:"+ip+"。如非本人操作,请立即使用你注册时绑定的邮箱进行密码找回。";
		return  msg;
	}

	public  String sendbindingmail(String code,String ip,String username) throws MessagingException

	{
		String msg="         【爱之家科技有限公司】尊敬的"+username+"用户:欢迎您绑定你的邮箱账号，请将此验证码：《"+code+"》填写到绑定邮箱页面。操作地址:"+ip+"。如非本人操作,请忽略该邮件。";
		return  msg;
	}
	public boolean sslSend(MessageEmail msg1, EmailAccout emailAccount)
			throws MessagingException, IOException {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", emailAccount.getPlace());
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");

		final String username = emailAccount.getUsername();
		final String password = emailAccount.getPassword();
		Session session = Session.getDefaultInstance(props, new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}});
		Message msg = new MimeMessage(session);

		// 设置发件人和收件人
		msg.setFrom(new InternetAddress(emailAccount.getUsername()));
		List<String> tos = msg1.getTo();
		Address to[] = new InternetAddress[tos.size()];
		for(int i=0;i<tos.size();i++){
			to[i] = new InternetAddress(tos.get(i));
		}
		// 多个收件人地址
		msg.setRecipients(Message.RecipientType.TO, to);
		msg.setSubject(msg1.getSubject()); // 标题
		msg.setText(msg1.getMsg());// 内容
		msg.setSentDate(new Date());
		Transport.send(msg);
		return true;
	}

}