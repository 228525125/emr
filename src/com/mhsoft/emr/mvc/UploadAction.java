package com.mhsoft.emr.mvc;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.FileItem;
import org.apache.tools.ant.taskdefs.Get;

import com.easyjf.container.annonation.Inject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.ActionContext;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.EmrUser;
import com.mhsoft.emr.domain.FileClass;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IDepartmentService;
import com.mhsoft.emr.service.IEmployeeService;
import com.mhsoft.emr.service.IFileClassService;
import com.mhsoft.emr.service.IMaxidService;
import com.mhsoft.emr.service.IOrganizationService;
import com.mhsoft.emr.util.Logger;
import com.mhsoft.emr.util.PdfUtil;

public class UploadAction extends BaseAction {

	public final static String ExtraPath = "extra";
	
	@Inject
	private IEmployeeService service;
	
	@Inject
	private IDepartmentService departmentService;
	
	@Inject
	private IOrganizationService organizationService;
	
	@Inject
	private IFileClassService fileClassService;
	
	@Inject
	private IMaxidService maxidService;

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public void setService(IEmployeeService service) {
		this.service = service;
	}
	
	public void setFileClassService(IFileClassService fileClassService) {
		this.fileClassService = fileClassService;
	}

	public void setMaxidService(IMaxidService maxidService) {
		this.maxidService = maxidService;
	}

	public Page doIndex(WebForm form, Module m) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String log = "["+df.format(new Date(System.currentTimeMillis()));
		log += "][SaveFile]";
		
		Employee object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getEmployee(id);
			
			form.toPo(object, true);
			if (!hasErrors()){
				Boolean empty = object.getEmpty();   //是否引用或上传文档
				
				if(!form.getFileElement().keySet().isEmpty()){
					String selected = saveFile(form, fileSavePath+object.getCode()+"/"+object.getFileClass().getCode(),object.getCode(), getUser().getAccount(), object.getVersion());
					if(!"".equals(selected)){       //表示没有上传文件，只是修改了字段
						object.setSelected(selected);
						empty = false;
						//只有上传文件才更新时间
						object.setDate(new Date(System.currentTimeMillis()));
					}
				}
				
				if(!"".equals(saveExtraFile(form, fileSavePath+object.getCode()+"/"+object.getFileClass().getCode()+"/"+ExtraPath, getUser().getAccount()))){
					object.setExtraEmpty(false);
				}
				
				if(null!=object.getCite() && !"".equals(object.getCite())){
					empty = false;
				}
				
				object.setEmpty(empty);
				
				service.updateEmployee(id, object);
				log += "[FileCode:"+object.getCode()+"]";
			}
		}else{
			Long did = new Long(CommUtil.null2String(form.get("department")));    //只有当新增文件时才添加物料信息，如果要修改物料信息需在列表中完成			
			Department department = departmentService.getDepartment(did);
			Organization organization = department.getOrganization();
			object = form.toPo(Employee.class);
			if (!hasErrors()){
				Boolean empty = object.getEmpty();   //是否引用或上传文档
				
				String code = "";
				if(null!=department.getTuhao()&&!"".equals(department.getTuhao()))
					code = department.getTuhao();
				else
					code = department.getCode();
				object.setCode(code+"."+object.getFileClass().getCode()+"."+maxidService.createMaxId(6));
				
				if(!form.getFileElement().keySet().isEmpty()){
					String selected = saveFile(form, fileSavePath+object.getCode()+"/"+object.getFileClass().getCode(),object.getCode(), getUser().getAccount(), object.getVersion());
					if(!"".equals(selected)){       //表示上传文件
						object.setSelected(selected);
						empty = false;
						
						//只有上传文件才更新时间
						object.setDate(new Date(System.currentTimeMillis()));
					}
					
					if(!"".equals(saveExtraFile(form, fileSavePath+object.getCode()+"/"+object.getFileClass().getCode()+"/"+ExtraPath, getUser().getAccount()))){
						object.setExtraEmpty(false);
					}
				}
				
				if(null!=object.getCite() && !"".equals(object.getCite())){
					empty = false;
				}
				
				object.setEmpty(empty);
				
				department.getEmployeeList().add(object);
				organization.getEmployeeList().add(object);
				departmentService.updateDepartment(did, department);
				organizationService.updateOrganization(organization.getId(), organization);
				
				log += "[FileCode:"+object.getCode()+"]";
			}
		}
		
		///////////////////////////////////////////////
		/*String path = "F:/CX/项目/MyEclipse/workspace101/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/emr/pdff/";
		
		File file = new File(path);
		
		int i = 0;
		
		long fid = 1802815;
		
		List<String> result = new ArrayList<String>();
		
		for(File f : file.listFiles()){ 
			String fileName = f.getName();
			
			System.out.println("--------"+fileName+":begin---------");
			
			List<String> list = new ArrayList<String>();
			String [] ss = fileName.split("\\[");
			for(String s : ss){
				if(s.length()>2){
					list.add(s.substring(0, s.indexOf("]")));
				}
			}
			if(4==list.size()){
				String code = list.get(0);
				String account = list.get(1);
				
				DateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = list.get(2);
				Date date = null;
				try {
					date = datef.parse(dateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String version = list.get(3);
				
				
				String tuhao = code.substring(0, code.indexOf("."));
				String id = code.substring(code.lastIndexOf(".")+1);
				String fileClassCode = code.substring(tuhao.length()+1,code.lastIndexOf(id)-1);
				
				
				
				Department department = departmentService.getDepartment(tuhao);
				if(null==department)
					continue;
				Organization organization = department.getOrganization();
				FileClass fileClass = fileClassService.getFileClas(fileClassCode);
				
				if(null==fileClass)
					continue;
				
				String sql = "";
				sql += "IF EXISTS(select 1 from employee where code='"+code+"') update employee set date='"+dateString+"',empty=0,version='"+version+"',selected='"+fileName+"',checked=1,checker='admin',checkDate=getdate() where code='"+code+"' ELSE BEGIN "
						+ "insert employee (id,code,name,auxCode,disabled,description,address,level,date,fileClassId,departmentId,empty,version,selected,extraEmpty,cite,checked,checker,checkDate) "
						+ "values ("+fid+",'"+code+"','"+department.getName()+" - "+fileClass.getName()+"',null,0,'xthf',null,null,'"+dateString+"',"+fileClass.getId()+","+department.getId()+",0,'"+version+"','"+fileName+"',1,null,1,'admin',getdate()) insert OrganizationASEmployee (organizationId,employeeId) values (1,"+fid+") END ";

				result.add(sql);	*/			
				
				/*Employee employee = form.toPo(Employee.class);
				employee.setId(null);
				employee.setCode(code);
				employee.setName(department.getName()+"-"+fileClass.getName());
				employee.setDate(date);
				employee.setFileClass(fileClass);
				employee.setEmpty(false);
				employee.setVersion(version);
				employee.setSelected(fileName);
				
				organization.getEmployeeList().add(employee);
				department.getEmployeeList().add(employee);
				departmentService.updateDepartment(department.getId(), department);
				organizationService.updateOrganization(organization.getId(), organization);*/
				
				/*i++;
				fid += 1l;
				System.out.println("--------"+fileName+":end---------");
			}
		}
		
		for(String sql : result)
			System.out.println(sql);*/
		
		//////////////////////////////////////////////////////////
		
		
		
		String IP = ActionContext.getContext().getRequest().getRemoteAddr();
		Logger.getLogger(IP).warn(log);
		Page page = pageForExtForm(form);
		page.setContentType("text/html;charset=UTF-8");
		return page;
	}
	
	/**
	 * 只保存文件
	 * @param form
	 * @param upLoadPath
	 * @param fileCode
	 * @param user
	 * @param version
	 * @return 返回保存的文件名
	 */
	public static String saveFile(WebForm form, String upLoadPath, String fileCode, String user, String version){
		String fileName = "";
		Set keys = form.getFileElement().keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {				
			String fieldName = (String) it.next();
			if("file".equals(fieldName)){
				FileItem item = (FileItem) form.getFileElement().get(fieldName);
				if (item.getSize() == 0) {
					break;
				}

			    File file = new File(upLoadPath);
			    
			    try {
			    	if(!file.exists()){
						file.mkdirs();
					}
			    	/*for(File f : file.listFiles()){   //清空文件夹
			    		f.delete();
			    	}*/
			    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	String curTime = df.format(new Date(System.currentTimeMillis())); 
			    	
			    	String [] ss = item.getName().split("\\.");
			    	String suffix = ss[ss.length-1];
			    	
			    	String curDate = curTime.substring(0, 10);
			    	String v = getVersion(file, version);
			    	
			    	fileName = "["+fileCode+"]["+user+"]["+curDate+"]["+version+"-"+v+"]."+suffix.toLowerCase(); 
			    	
			    	file = new File(upLoadPath+"/"+fileName);
			    	if(!file.exists()){
			    		file.createNewFile();
			    	}
			    	item.write(file);
			    	
			    	
					String log = curTime;
					log += "[UploadFile]";
					log += "[FileName:"+item.getName()+"]";
					log += "[User:"+user+"]";
					log += "[version:"+version+v+"]";
			    	
					String IP = ActionContext.getContext().getRequest().getRemoteAddr();
			    	Logger.getLogger(IP).warn(log);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				fieldName = "";
			}
		}
		
		return fileName;
	}
	
	/**
	 * 只保存附件
	 * @param form
	 * @param upLoadPath
	 * @param fileCode
	 * @param user
	 * @param version
	 * @return
	 */
	public static String saveExtraFile(WebForm form, String upLoadPath, String user){
		String fileName = "";
		Set keys = form.getFileElement().keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {				
			String fieldName = (String) it.next();
			if("extrafile".equals(fieldName)){
				FileItem item = (FileItem) form.getFileElement().get(fieldName);
				if (item.getSize() == 0) {
					break;
				}

			    File file = new File(upLoadPath);
			    
			    try {
			    	if(!file.exists()){
						file.mkdirs();
					}
			    	
			    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	String curTime = df.format(new Date(System.currentTimeMillis())); 
			    	
			    	fileName = item.getName(); 
			    	
			    	file = new File(upLoadPath+"/"+fileName);
			    	if(!file.exists()){
			    		file.createNewFile();
			    	}
			    	item.write(file);
			    	
			    	
					String log = curTime;
					log += "[UploadExtraFile]";
					log += "[FileName:"+item.getName()+"]";
					log += "[User:"+user+"]";
			    	
					String IP = ActionContext.getContext().getRequest().getRemoteAddr();
			    	Logger.getLogger(IP).warn(log);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				fieldName = "";
			}
		}
		
		return fileName;
	}
	
	private static String getVersion(File dir, String version){
		List<String> list = new ArrayList<String>();
		List<Integer> vList = new ArrayList<Integer>();
    	
    	for(File f : dir.listFiles()){
    		if(f.isFile())
    			list.add(f.getName());
    	}
    	
    	for(String name : list){
    		String v = name.split("]")[3].substring(1);  //完成的版本号 A1
    		String bv = v.substring(0, 1);               //去大版本 A
    		if(bv.equals(version)){
    			v = v.substring(version.length());                          //只去版本小号  1
        		vList.add(Integer.valueOf(v));
    		}
    	}
    	
    	Collections.sort(vList);
    	
    	if(vList.isEmpty())
    		return "1";
    	else
    		return String.valueOf(vList.get(vList.size()-1)+1);
	}
	
	public static void main(String[] args) {
		String str = "[文件编号][用户][日期][版本号]";
		str = "20140815165840.pdf";
		System.out.println(str.split("\\.")[1]);
		
		/*for(String s : str.split("]"))
			System.out.println(s.substring(1, s.length()));*/
	}
}
