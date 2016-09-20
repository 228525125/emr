package com.mhsoft.emr.util;

import org.apache.log4j.PropertyConfigurator;

import com.mhsoft.emr.mvc.UploadAction;

public class Logger
{ 
	static {
		PropertyConfigurator.configure("F:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/emr/WEB-INF/classes/log4j.lcf");
	}
	
	public static org.apache.log4j.Logger getLogger(String ip){
		return org.apache.log4j.Logger.getLogger(ip);
	}
}
