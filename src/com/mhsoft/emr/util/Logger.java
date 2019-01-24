package com.mhsoft.emr.util;

import org.apache.log4j.PropertyConfigurator;

import com.mhsoft.emr.mvc.UploadAction;

public class Logger
{ 
	public static org.apache.log4j.Logger logger;
	static {
		PropertyConfigurator.configure("C:/emr/log4j.lcf");
		logger = org.apache.log4j.Logger.getRootLogger();
	}
	
	public static org.apache.log4j.Logger getLogger(String ip){
		return org.apache.log4j.Logger.getLogger(ip);
	}
	
	public static void debug(String message) {
		logger.debug(message);
	}
	
	public static void debug(Object obj, String message) {
		logger.debug("["+obj.getClass().getName()+"] - "+message);
	}
	
	public static void info(String message) {
		logger.info(message);
	}
	
	public static void warn(String message) {
		logger.warn(message);
	}
	
	public static void error(String message) {
		logger.error(message);
	}
}
