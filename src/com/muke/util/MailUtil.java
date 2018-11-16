package com.muke.util;

import java.util.Properties;

/**
 * 
 * @author TH
 *
 */
public class MailUtil {
	
	/***
	 * 获取对应的Properties对象
	 * @return
	 */
	public static Properties getProps(String mailAddr,String keyword){ 
		
	   Properties props = null;
	   
	   if(mailAddr.split("@")[1].startsWith("163")&&keyword.equals("SMTP")){
		   
		  props = new Properties();
		  props.setProperty("mail.smtp.auth","true");
		  props.setProperty("mail.transport.protocol","smtp");
		  props.setProperty("mail.host","smtp.163.com"); 
		  
	   }else if(mailAddr.split("@")[1].startsWith("163")&&keyword.equals("POP3")){
		   
		   props = new Properties();
		   props.setProperty("mail.store.protocol", "pop3");      
	       props.setProperty("mail.pop3.host", "pop3.163.com"); 
	       props.setProperty("mail.pop3.auth", "true");   
	       
	   }else if(mailAddr.split("@")[1].startsWith("qq")&&keyword.equals("SMTP")){
		   
		   props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.setProperty("mail.transport.protocol","smtp");
		   props.put("mail.smtp.host", "smtp.qq.com");
		   props.put("mail.smtp.port", "587");
		   
	   }else if(mailAddr.split("@")[1].startsWith("qq")&&keyword.equals("POP3")){
		   
		   props = new Properties();
		   props.setProperty("mail.store.protocol","pop3");
	       props.setProperty("mail.pop3.host", "pop.qq.com"); // 按需要更改
	       props.setProperty("mail.pop3.port", "995");
	       
	       props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	       props.setProperty("mail.pop3.socketFactory.fallback", "true");
	       props.setProperty("mail.pop3.socketFactory.port", "995");
	   }
	   return props;
		
   }
	
    /**
     * 判断字符串不为空
     * @param str
     * @return
     */
	public static boolean isNotNull(String str){
		if(str!=null && !"".equals(str)){
			return true;
		}
		return false;
	}
}
