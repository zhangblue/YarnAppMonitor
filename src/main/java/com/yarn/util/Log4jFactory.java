package com.yarn.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jFactory
{
	//private static final String logQueryLog4J_Properties = "/home/hdfs/CMCCRadius/log4j_downloadCMCCRadius.properties";// LogQuery.jar使用的log4j配置文件
	public static Logger logger_system = null;

	public static void initLog4J(String log4jproperties)
	{
		PropertyConfigurator.configure(log4jproperties);
		logger_system = Logger.getLogger("sys");

	}
}
