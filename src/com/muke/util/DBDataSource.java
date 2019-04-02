package com.muke.util;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库连接池
 * @author Administrator
 *
 */
public class DBDataSource {

	//修改 数据库连接参
	public static String DRIVER;
	public static String URL;
	public static String USER;
	public static String PWD;
	public static int maxPoolSize;
	public static int minPoolSize;
	public static int initialPoolSize;
	public static int checkoutTimeout;
	public static int maxIdleTime;
	public static int max_statements;
//	public static int acquireRetryAttempts;
//	public static int maxStatements;
	public static int idleConnectionTestPeriod;
	public static int acquireRetryDelay;
	public static int acquireRetryAttempts;
	public static String breakAfterAcquireFailure;
//	public static String testConnectionOnCheckout;
	public static String preferredTestQuery;
	private static ComboPooledDataSource cpDataSource = null;
	public static String testConnectionOnCheckin;
	public static String testConnectionOnCheckout;
//	public static String validationQuery;

	//加载驱动
	static {
		try {
			//读取配置文件，加载JDBC四大参数

			Properties config = new Properties();
			config.load(DBDataSource.class.getClassLoader().getResourceAsStream("db.properties"));
			DRIVER = config.getProperty("drivername");
			URL = config.getProperty("url");
			USER = config.getProperty("username");
			PWD	= config.getProperty("password");
//			//保持持续连接：validationQuery=SELECT 1
//			validationQuery=config.getProperty("validationQuery");
			max_statements = Integer.parseInt(config.getProperty("max_statements"));
			acquireRetryDelay = Integer.parseInt(config.getProperty("acquireRetryDelay"));
			maxPoolSize	= Integer.parseInt(config.getProperty("maxPoolSize"));
			minPoolSize	= Integer.parseInt(config.getProperty("minPoolSize"));
			maxIdleTime=Integer.parseInt(config.getProperty("maxIdleTime"));
			acquireRetryAttempts=Integer.parseInt(config.getProperty("acquireRetryAttempts"));
//			acquireRetryAttempts=Integer.parseInt(config.getProperty("acquireRetryAttempts"));
			initialPoolSize = Integer.parseInt(config.getProperty("initialPoolSize"));
//			maxStatements= Integer.parseInt(config.getProperty("maxStatements"));
//			testConnectionOnCheckout=config.getProperty("testConnectionOnCheckout");
			breakAfterAcquireFailure=config.getProperty("breakAfterAcquireFailure");
			checkoutTimeout = Integer.parseInt(config.getProperty("checkoutTimeout"));
			preferredTestQuery=config.getProperty("preferredTestQuery");
			idleConnectionTestPeriod=Integer.parseInt(config.getProperty("idleConnectionTestPeriod"));
			testConnectionOnCheckin=config.getProperty("testConnectionOnCheckin");
			testConnectionOnCheckout=config.getProperty("testConnectionOnCheckout");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**************** c3p0 数据库连接池 启动方法******************/
	private static void c3p0DataSource() throws Exception {
		cpDataSource = new ComboPooledDataSource();
		cpDataSource.setDriverClass(DRIVER);
		cpDataSource.setJdbcUrl(URL);
		cpDataSource.setUser(DESUtils.getDecryptString(USER));
		cpDataSource.setPassword(DESUtils.getDecryptString(PWD));
		cpDataSource.setPreferredTestQuery(preferredTestQuery);
		cpDataSource.setMaxPoolSize(maxPoolSize);
		cpDataSource.setMinPoolSize(minPoolSize);
		cpDataSource.setMaxIdleTime(maxIdleTime);
		cpDataSource.setAcquireRetryDelay(acquireRetryDelay);
		cpDataSource.setInitialPoolSize(initialPoolSize);
		cpDataSource.setMaxStatements(max_statements);
		cpDataSource.setAcquireRetryAttempts(acquireRetryAttempts);
		cpDataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
//		cpDataSource.setAcquireRetryAttempts(acquireRetryAttempts);
//		cpDataSource.setMaxStatements(maxStatements);
//		cpDataSource.setTestConnectionOnCheckout(Boolean.parseBoolean(testConnectionOnCheckout));
		cpDataSource.setCheckoutTimeout(checkoutTimeout);
		cpDataSource.setTestConnectionOnCheckin(Boolean.parseBoolean(testConnectionOnCheckin));
		cpDataSource.setTestConnectionOnCheckout(Boolean.parseBoolean(testConnectionOnCheckout));
		cpDataSource.setBreakAfterAcquireFailure(Boolean.parseBoolean(breakAfterAcquireFailure));
	}

	/**
	 * c3p0数据库连接入
	 *
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnectionC3P0() throws Exception {
		if (cpDataSource == null) {
			c3p0DataSource();
		}
		Connection conn = null;
		if (cpDataSource != null) {
			conn = cpDataSource.getConnection();
		}
		if(conn!=null){
			return conn;
		}else{
			conn = cpDataSource.getConnection();
		}
		return conn;
	}

}
