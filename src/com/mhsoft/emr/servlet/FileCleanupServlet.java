package com.mhsoft.emr.servlet;

import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * 文件清理任务
 * 文件名凡在数据库没有记录的删除，判断依据：
 * 1、没有记录；
 * 2、selected字段为null；
 * @author admin
 *
 */
public class FileCleanupServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		FileCleanupTask task = new FileCleanupTask(config.getServletContext());
		Timer timer = new Timer();
		//timer.schedule(task, 5000,1000*60*60*5);      //一小时执行一次
		timer.schedule(task, 10000);      //一小时执行一次
		//task.run();
	}
}
