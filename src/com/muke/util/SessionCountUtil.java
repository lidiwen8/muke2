package com.muke.util;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @Title: SessionCount
 * @Description: 该类用于获取当前在线人数
 * @author 李弟文
 * @data 2018年8月15日 上午9:04:40
 * @version V1.0
 */
public class SessionCountUtil implements HttpSessionListener {

	private static int activeSessions = 0;

	// session创建时执行
	public void sessionCreated(HttpSessionEvent se) {
		activeSessions++;
	}

	// session销毁时执行
	public void sessionDestroyed(HttpSessionEvent se) {
		if (activeSessions > 0)
			activeSessions--;
	}

	// 获取活动的session个数(在线人数)
	public static int getActiveSessions() {
		return activeSessions;
	}

}