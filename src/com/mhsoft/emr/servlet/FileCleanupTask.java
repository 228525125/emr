package com.mhsoft.emr.servlet;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mhsoft.emr.service.JDBCQueryService;
import com.mhsoft.emr.util.DeleteFileUtil;
import com.mhsoft.emr.util.Logger;

/**
 * 文件清理任务
 * @author admin
 *
 */
public class FileCleanupTask extends TimerTask {

	private ServletContext sc;
	
	public FileCleanupTask(ServletContext sc) {
		// TODO Auto-generated constructor stub
		this.sc = sc;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		JDBCQueryService jdbcService = (JDBCQueryService) context.getBean("jdbcService");
		Date date = new Date(System.currentTimeMillis());
		//if(3 == date.getHours()){       // 上午3点执行
			Map<String, File> map = new HashMap<String, File>();
			String fileSavePath = sc.getInitParameter("uploadPath");
			File file = new File(fileSavePath);
			
			
			File [] files = queryFiles(file, 1);
			
			for(int i=0;i<files.length;i++){
				File [] fils = queryFiles(files[i], 1);
				
				for(int j=0;j<fils.length;j++){
					File [] fis = queryFiles(fils[j], 2);
					
					FileList.getInstance().addArray(fis);
				}
			}
			
			
			
			for(String filePath : FileList.getInstance().getList()){
				Integer index = filePath.lastIndexOf("\\");
				
				if(Integer.valueOf(-1).equals(index))    //文件路径问题，则跳过
					continue;
				
				String fileName = filePath.substring(index+1);
				
				if(50>fileName.length())      //如果文件名不正确，例如文件名里带有\；
					continue;
				
				index = fileName.lastIndexOf(".");
				if(Integer.valueOf(-1).equals(index))  //如果文件名没有后缀，跳过
					continue;
				
				String suffix = fileName.substring(index);
				
				if(!".pdf".equals(suffix))             //如果不是pdf，跳过
					continue;
				
				index = fileName.indexOf("]");
				if(Integer.valueOf(-1).equals(index))   //文件名格式有问题，[PM9002WX.01.02.063752][zhangyiya][2018-02-12][A-1][]
					continue;
				
				String name = fileName.substring(1, index);

				String sql = "select selected from Employee where code='"+name+"'";
				List result = jdbcService.query(sql);
				if(!result.isEmpty()){
					
					Map bean = (Map) result.get(0);
					Object selected = bean.get("selected");
					if(null==selected){                  //没有选择文件的删除
						if(DeleteFileUtil.delete(filePath))
							Logger.warn("删除单个文件" + filePath + "成功！");
					}
				}else{                                   //数据库没有查找到记录的删除
					if(DeleteFileUtil.delete(filePath))
						Logger.warn("删除单个文件" + filePath + "成功！");
				}
			}
			//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//System.out.println("task time:"+df.format(date));
		//}
	}
	
	/**
	 * 
	 * @param file
	 * @param type dir：1 , file：2 ;
	 * @return
	 */
	private File [] queryFiles(File file, final Integer type) {
		return file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				if(Integer.valueOf(1).equals(type) && file.isDirectory()){
					return true;
				}else if(Integer.valueOf(2).equals(type) && file.isFile()){
					return true;
				}
				return false;
			}
			
		});
	}
	
	public static void main(String[] args) {
		String str = "[PM9002WX.01.02.063752][zhangyiya][2018-02-12][A-1][]";
		System.out.println(str.indexOf("]"));
	}

}
