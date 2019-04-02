package com.muke.util;


import com.muke.config.MailConfig;
import com.muke.pojo.MessageEmail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Security;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SendEmail {

    public String sendMsg(String adress, String password, String username) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的" + username + "用户:已经将你的密码重置为初始密码<" + password + ">，请妥善保管；为了确保你的账户安全，登陆后请在个人中心尽快修改当前密码。";
        return msg;
    }

    public String sendlogin(String adress, String ip, String username, String time) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的" + username + "用户:您于" + time + "登录爱之家网站答疑平台" + adress + ",登录地址:" + ip + "。如非本人操作,请立即登录修改密码。";
        return msg;
    }

    public String sendupdatepass(String adress, String ip, String username, String time) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的" + username + "用户:您于" + time + "登录爱之家网站答疑平台" + adress + "修改了你的密码,登录地址:" + ip + "。如非本人操作,请立即使用你注册时绑定的邮箱进行密码找回。";
        return msg;
    }

    public String sendupdatepass2(String adress, String ip, String username, String time) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的" + username + "用户:您于" + time + "在爱之家网站答疑平台" + adress + "修改了你的密码,操作地址:" + ip + "。如非本人操作,请立即使用你注册时绑定的邮箱进行密码找回。";
        return msg;
    }

    public String sendbindingmail(String code, String ip, String username) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的" + username + "用户:欢迎您绑定你的邮箱账号，请将此验证码：《" + code + "》填写到绑定邮箱页面。操作地址:" + ip + "。如非本人操作,请忽略该邮件。";
        return msg;
    }

    public String SendDeleteMsgmail(String url, String title, String username,String email,String time) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的用户:你回复的帖子("+title+") :"+url+"，已经被该贴楼主<"+username+">于"+time+"暂时屏蔽了，无法查看，如有意见请联系该贴楼主邮箱：" + email + "。";
        return msg;
    }

    public String SendDeleteMsgmail2(String url, String title, String username,String email,String time) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的用户:你回复的帖子<"+title+">:"+url+"，已经被该贴楼主<"+username+">于"+time+"暂时屏蔽了，无法查看；由于该楼主邮箱未绑定，如对楼主屏蔽掉帖子有意见请联系管理员邮箱：" + email + "。";
        return msg;
    }

    public String SendDeleteMsgmail3(String url, String title,String time) throws MessagingException {
        String msg = "         【爱之家科技有限公司】尊敬的用户:你回复的帖子<"+title+">:"+url+"，已经被管理员于"+time+"暂时屏蔽了，无法查看；如对管理员屏蔽掉帖子有意见请联系管理员邮箱：163202933@qq.com。";
        return msg;
    }


    public boolean sslSend(MessageEmail msg1)
            throws MessagingException, IOException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = new Properties();
		props.setProperty("mail.smtp.host", MailConfig.getMail_smtp_host());
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", MailConfig.getSmtp_port());
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");

//        final String username = DESUtils.getDecryptString( MailConfig.getMail_user());
//        final String password = DESUtils.getDecryptString(MailConfig.getMail_password());
        final String username = MailConfig.getMail_user();
        final String password = MailConfig.getMail_password();
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        Message msg = new MimeMessage(session);

        // 设置发件人和收件人
        msg.setFrom(new InternetAddress(username));
        List<String> tos = msg1.getTo();
        Address to[] = new InternetAddress[tos.size()];
        for (int i = 0; i < tos.size(); i++) {
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