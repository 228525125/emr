package com.mhsoft.emr.util;

import org.apache.log4j.PropertyConfigurator;

import com.mhsoft.emr.mvc.UploadAction;

public class Logger
{ 
	static {
		//PropertyConfigurator.configure("D:/apache-tomcat-6.0.32/webapps/emr/WEB-INF/classes/log4j.lcf");
		PropertyConfigurator.configure("E:/log4j.lcf");
	}
	
	public static org.apache.log4j.Logger getLogger(String ip){
		return org.apache.log4j.Logger.getLogger(ip);
	}
}
