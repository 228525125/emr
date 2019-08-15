package com.mhsoft.emr.mvc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.FileClass;
import com.mhsoft.emr.domain.History;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.domain.Role;
import com.mhsoft.emr.service.IDepartmentService;
import com.mhsoft.emr.service.IEmployeeService;
import com.mhsoft.emr.service.IFileClassService;
import com.mhsoft.emr.service.IMaxidService;
import com.mhsoft.emr.service.IOrganizationService;
import com.mhsoft.emr.service.IRoleService;
import com.mhsoft.emr.service.JDBCQueryService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.util.Logger;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.ActionContext;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.errorhandler.RedirectErrorHandler;
import com.easyjf.web.tools.IPageList;

/**
 * 
 * @author 陈贤
 * 需实现：
 * 根据部门id查询职员列表 参数：部门id 返回：职员列表
 * 根据机构id查询职员列表 参数：机构id 返回：职员列表
 * 给某个职员添加多个部门 参数：多个部门id 职员id 返回：无
 * 给某个职员添加多个岗位 参数：多个岗位id 职员id 返回：无
 * 给某个职员添加多个角色 参数：多个角色id 职员id 返回：无
 * 
 * 注意：
 * 在保存职员信息时，要同时保存其部门信息和机构信息；
 *
 */
public class EmployeeAction extends BaseAction {
	
	@Inject
	private IEmployeeService service;
	
	@Inject
	private IDepartmentService departmentService;
	
	@Inject
	private IOrganizationService organizationService;
	
	@Inject
	private IFileClassService fileClassService;
	
	@Inject
	private IRoleService roleService;
	
	@Inject
	private IMaxidService maxidService;
	
	@Inject
	private JDBCQueryService jdbcService;
	
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public void setOrganizationService(IOrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	public void setFileClassService(IFileClassService fileClassService) {
		this.fileClassService = fileClassService;
	}

	public void setService(IEmployeeService service) {
		this.service = service;
	}
	
	public void setMaxidService(IMaxidService maxidService) {
		this.maxidService = maxidService;
	}

	public void setJdbcService(JDBCQueryService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = null;
		//根据条件模糊查找部门		
		if(null!=form.get("query")&&!"".equals(form.get("query").toString())){			
			String query = form.get("query").toString();
			qo.addQuery("(obj.code like '%"+query+"%' or obj.description like '%"+query+"%' or obj.department.tuhao like '%"+query+"%' or obj.department.model like '%"+query+"%' or obj.department.code like '%"+query+"%' or obj.department.name like '%"+query+"%')", null);
		}else{
			//根据部门查询职员列表
			if(null!=form.get("departmentId")&&!"".equals(form.get("departmentId").toString())){
				Long id = new Long(CommUtil.null2String(form.get("departmentId")));
				if(0l==id)
					qo.addQuery("obj.department is null", null);
				else if(AppContext.SELECT_ALL==id)
					;
				else{
					qo.addQuery("obj.department.id=?", new Object[]{id});
				}
			}
			//根据机构查询职员列表
			if(null!=form.get("organizationId")&&!"".equals(form.get("organizationId").toString())){
				Long id = new Long(CommUtil.null2String(form.get("organizationId")));			
				Organization organization = organizationService.getOrganization(id);
				qo.addQuery("? MEMBER obj.organizationList", new Object[]{organization});
			}
		}
		
		//根据文件类型
		if(null!=form.get("fileClass")&&!"".equals(form.get("fileClass").toString())){
			Long id = new Long(CommUtil.null2String(form.get("fileClass")));
			qo.addQuery("obj.fileClass.id=?", new Object[]{id});
		}
		
		if(null!=form.get("disabled")&&!"".equals(form.get("disabled").toString())){
			Boolean disabled = new Boolean(form.get("disabled").toString());
			qo.addQuery("obj.disabled=?", new Object[]{disabled});
		}
		
		if(null!=form.get("checked")&&!"".equals(form.get("checked").toString())){
			Boolean checked = new Boolean(form.get("checked").toString());
			qo.addQuery("obj.checked=?", new Object[]{checked});
		}
		
		pageList = service.getEmployeeBy(qo);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("currentPage", pageList.getCurrentPage());
		result.put("nextPage", pageList.getNextPage());
		result.put("pages", pageList.getPages());
		result.put("pageSize", pageList.getPageSize());
		result.put("previousPage", pageList.getPreviousPage());
		result.put("rowCount", pageList.getRowCount());
		List list = pageList.getResult();
		List<Map> res = new ArrayList<Map>();
		if(null!=list){
			for(Object obj : list){
				Employee emp = (Employee) obj;
				Map<String,Object> bean = new HashMap<String, Object>();
				bean.put("id", emp.getId());
				bean.put("code", emp.getCode());
				bean.put("name", emp.getName());
				bean.put("fileClass", emp.getFileClass());
				bean.put("disabled", emp.getDisabled());
				bean.put("description", emp.getDescription());
				bean.put("address", emp.getAddress());
				bean.put("department", emp.getDepartment().getName());
				bean.put("departmentCode", emp.getDepartment().getCode());
				bean.put("departmentModel", emp.getDepartment().getModel());
				bean.put("departmentId", emp.getDepartment().getId());
				bean.put("empty", emp.getEmpty());
				bean.put("selected", emp.getSelected());
				bean.put("version", emp.getVersion());
				bean.put("extraEmpty", emp.getExtraEmpty());
				bean.put("cite", emp.getCite());
				bean.put("checked", emp.getChecked());
				bean.put("checker", emp.getChecker());
				
				if(null!=emp.getDate()){
					SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.put("date", sFormat.format(emp.getDate()));
				}
				
				if(null!=emp.getCheckDate()){
					SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.put("checkDate", sFormat.format(emp.getCheckDate()));
				}
				
				res.add(bean);
			}
		}
		result.put("result", res);
		form.jsonResult(result);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delEmployee(id);
		return pageForExtForm(form);
	}

	/**
	 * 该方法被UploadAction.doIndex代替
	 * @param form
	 * @return
	 */
	public Page doSave(WebForm form) {
		Employee object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getEmployee(id);
			
			form.toPo(object, true);
			if (!hasErrors())
				service.updateEmployee(id, object);
		}else{
			Long did = new Long(CommUtil.null2String(form.get("department")));    //只有当新增职员时才添加部门信息，如果要修改部门信息需在列表中完成			
			Department department = departmentService.getDepartment(did);
			Organization organization = department.getOrganization();
			object = form.toPo(Employee.class);			
			if (!hasErrors()){
				department.getEmployeeList().add(object);
				organization.getEmployeeList().add(object);
				departmentService.updateDepartment(did, department);
				organizationService.updateOrganization(organization.getId(), organization);
			}
		}
		return pageForExtForm(form);
	}
	
	public Page updateDepartment(WebForm form){
		if(null!=form.get("employeeId")&&!"".equals(form.get("employeeId").toString())
		 &&null!=form.get("departments")&&!"".equals(form.get("departments").toString())){
			Long eid = new Long(CommUtil.null2String(form.get("employeeId")));
			Employee employee = service.getEmployee(eid);
			String departments = form.get("departments").toString();
			String [] dids = departments.split(",");
			for(String did : dids){
				Department department = departmentService.getDepartment(Long.valueOf(did));
				if(department.getEmployeeList().contains(employee))
					continue;
				else{
					department.getEmployeeList().add(employee);
					departmentService.updateDepartment(department.getId(), department);
				}
			}			
		}
		return success2(form, true, null);
	}
	
	public Page updateRole(WebForm form){
		if(null!=form.get("employeeId")&&!"".equals(form.get("employeeId").toString())
		 &&null!=form.get("roles")&&!"".equals(form.get("roles").toString())){
			Long eid = new Long(CommUtil.null2String(form.get("employeeId")));
			Employee employee = service.getEmployee(eid);
			
			//首先要清空职员角色列表
			Set<Role> roleList = employee.getRoleList(); 
			Iterator<Role> it = roleList.iterator();
			while(it.hasNext()){
				Role r = it.next();
				r.getEmployeeList().remove(employee);
				roleService.updateRole(r.getId(), r);
			}
			
			String roles = form.get("roles").toString();
			String [] roleIds = roles.split(",");			
			
			for(String rid : roleIds){				
				Role role = roleService.getRole(Long.valueOf(rid));				
				if(null==role||role.getEmployeeList().contains(employee))
					continue;
				else{
					role.getEmployeeList().add(employee);
					roleService.updateRole(role.getId(), role);
				}
			}
		}
		return success2(form, true, null);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Employee object = service.getEmployee(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateEmployee(id, object);
		return pageForExtForm(form);
	}
	
	public void search(WebForm form){
		if(null!=form.get("department")&&!"".equals(form.get("department").toString())){
			String department = form.get("department").toString();
			form.addResult("department", department);
		}
		
		if(null!=form.get("fileClass")&&!"".equals(form.get("fileClass").toString())){
			String fileClass = form.get("fileClass").toString();
			form.addResult("fileClass", fileClass);
		}
		
		if(null!=form.get("serial")&&!"".equals(form.get("serial").toString())){
			String serial = form.get("serial").toString();
			form.addResult("serial", serial);
		}
	}
	
	/**
	 * 该方法用于平板模式的文档查询功能，注意：该方法没有对引用的情况进行处理
	 * @param form
	 * @return
	 */
	public Page searchFile(WebForm form) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		String queryForFileType = sc.getInitParameter("queryForFileType");
		
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = null;
		List<String> files = new ArrayList<String>();
		
		//根据物料查询
		if(null!=form.get("department")&&!"".equals(form.get("department").toString())){
			String code = form.get("department").toString();
			if(-1==code.indexOf(".")){
				if(-1!=code.toLowerCase().indexOf("work"))            //如果是任务单的情况
					code = jdbcService.queryForWorkNo(code);
				else
					code = departmentService.queryForTuhao(code);
			}
			
			qo.addQuery("department.code = ?", new Object[]{code});
				
			//根据工位查询
			if(null!=form.get("role")&&!"".equals(form.get("role").toString())){
				String role = form.get("role").toString();
				Role r = roleService.getRole(role);
				qo.addQuery("? MEMBER obj.roleList", new Object[]{r});
			}
			
			//根据文件类型
			if(null!=form.get("fileClass")&&!"".equals(form.get("fileClass").toString())){
				String fileClass = form.get("fileClass").toString();
				if(-1==fileClass.indexOf(".")){                  //是id的情况
					Long fileClassId = Long.valueOf(form.get("fileClass").toString());
					qo.addQuery("fileClass.id = ?", new Object[]{fileClassId});
				}else{                                           //是code的情况
					qo.addQuery("fileClass.code = ?", new Object[]{fileClass});
				}
			}
			
			if(null!=form.get("serial")&&!"".equals(form.get("serial").toString())){
				String serial = form.get("serial").toString();
				qo.addQuery(" code like '%"+serial+"%'", null);
			}
			
			qo.addQuery(" disabled = ?", new Object[]{false});
			
			pageList = service.getEmployeeBy(qo);
			List list = pageList.getResult();
			if(null!=list)
				for(Object o : list){
					Employee e = (Employee) o;			
					File file = new File(fileSavePath+e.getCode()+"/"+e.getFileClass().getCode()+"/"+e.getSelected());
					if(file.exists()){
						if(isQueryForFileType(file.getName(), queryForFileType)){
							String path = "file-backups/"+e.getCode()+"/"+e.getFileClass().getCode()+"/";
							files.add(path+file.getName());
						}
					}
					
					//System.out.println(file.getAbsolutePath());
					//System.out.println(file.listFiles());
					/*if(null!=file.listFiles()){
						for(File f :file.listFiles()){
							if(-1!=f.getName().indexOf(".pdf")){
								String path = "file-backups/"+e.getCode()+"/"+e.getFileClass().getCode()+"/";
								files.add(path+f.getName());
							}
						}
					}*/
				}
		}
		
		form.addResult("files", files);
		return page("result");
	}
	
	/**
	 * 用于合格证程序接口
	 * 条件：物料代码 + 文件类型代码 = 找到的第一个PDF文件 
	 * @param form
	 * @return
	 */
	public Page searchFile2(WebForm form, Module module) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		String queryForFileType = sc.getInitParameter("queryForFileType");
		
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = null;
		String filePath = null;
		
		//根据物料查询
		if(null!=form.get("department")&&!"".equals(form.get("department").toString())){
			String code = form.get("department").toString();
			qo.addQuery("department.code = ?", new Object[]{code});
			
			//根据文件类型
			if(null!=form.get("fileClass")&&!"".equals(form.get("fileClass").toString())){
				String fileClass = form.get("fileClass").toString();
				qo.addQuery("fileClass.code = ?", new Object[]{fileClass});
			}
			
			qo.addQuery(" disabled = ?", new Object[]{false});
			
			pageList = service.getEmployeeBy(qo);
			List list = pageList.getResult();
			if(null!=list){
				Employee e = (Employee) list.get(0);
				File file = new File(fileSavePath+e.getCode()+"/"+e.getFileClass().getCode()+"/"+e.getSelected());
				if(file.exists()){
					if(isQueryForFileType(file.getName(), queryForFileType)){
						String path = "/file-backups/"+e.getCode()+"/"+e.getFileClass().getCode()+"/";
						filePath = path+file.getName();
					}
				}
			}
		}
		
		if(null!=filePath){
			HttpServletRequest request = ActionContext.getContext().getRequest();
			String webRootUrl = getWebappRootUrl(request);
			try {
				ActionContext.getContext().getResponse().sendRedirect(webRootUrl+filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return page("error");
	} 
	
	/**
	 * 获取web应用的根路径（url访问地址，如http://localhost:8090/mywebapp）
	 * @return
	 */
	public static String getWebappRootUrl(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		// root根路径
		String webappRootUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;
		return webappRootUrl;
	}
	
	private Boolean isQueryForFileType(String fileName ,String queryForFileType){
		Boolean ret = false;
		String [] types = queryForFileType.split(",");
		for(int i=0;i<types.length;i++){
			String type = types[i];
			if(-1!=fileName.indexOf(type)){
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public Page batchAddFile(WebForm form) {		
		if(null!=form.get("departmentId")&&!"".equals(form.get("departmentId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("departmentId")));
			Department department = departmentService.getDepartment(id);
			if(null!=department && service.isRepeat(department)){
				QueryObject qo = form.toPo(QueryObject.class);
				qo = form.toPo(QueryObject.class);
				qo.setPageSize(200);
				IPageList pageList = fileClassService.getFileClassBy(qo);
				List fileClassList = pageList.getResult();
				
				List<FileClass> fcList = new ArrayList<FileClass>(); 
				Set<Employee> fileSet = department.getEmployeeList();
				if(null!=fileSet){
					Iterator<Employee> fileIt = fileSet.iterator();
					while(fileIt.hasNext()){
						fcList.add(fileIt.next().getFileClass());
					}
				}
				
				for(Object obj : fileClassList){
					FileClass fileClass = (FileClass) obj;
					//if(null!=fileClass.getParent()&&!fcList.contains(fileClass)&&!fileClass.getDisabled()){
					if("01.01".equals(fileClass.getCode())
					 ||"01.02".equals(fileClass.getCode())
					 ||"01.03".equals(fileClass.getCode())
					 ||"04.02".equals(fileClass.getCode())
					 ||"02.02".equals(fileClass.getCode())
					 ||"02.01".equals(fileClass.getCode())
					 ||"03.03".equals(fileClass.getCode())
					 ||"04.04".equals(fileClass.getCode())
					 ||"06.03".equals(fileClass.getCode())){
						Employee bean = new Employee();
						String code = "";
						if(null!=department.getTuhao())
							code = department.getTuhao();
						else
							code = department.getCode();
						bean.setCode(code+"."+fileClass.getCode()+"."+maxidService.createMaxId(6));
						bean.setName(department.getName()+"-"+fileClass.getName());
						bean.setFileClass(fileClass);
						bean.setDepartment(department);
						bean.setDisabled(false);
						
						department.getEmployeeList().add(bean);
						Organization organization = department.getOrganization();
						
						organization.getEmployeeList().add(bean);
						departmentService.updateDepartment(id, department);
						organizationService.updateOrganization(organization.getId(), organization);
					}
				}
			}
		}
		return success2(form, true, null);
	}
	
	public Page batchDeleteFile(WebForm form) {
		if(("zhangyiya".equals(getUser().getAccount()) || "admin".equals(getUser().getAccount())) && null!=form.get("departmentId")&&!"".equals(form.get("departmentId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("departmentId")));
			Department department = departmentService.getDepartment(id);
			if(null!=department){
				String code = department.getCode();
				String sql = "exec delete_file '"+code+"'";
				jdbcService.execute(sql);
			}
		}
		
		return success2(form, true, null);
	}
	
	public Page history(WebForm form) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = null;
		List<History> files = new ArrayList<History>();
		
		//根据物料查询
		if(null!=form.get("department")&&!"".equals(form.get("department").toString())){
			String code = form.get("department").toString();
			if(-1==code.indexOf(".")){
				if(-1!=code.toLowerCase().indexOf("work"))            //如果是任务单的情况
					code = jdbcService.queryForWorkNo(code);
				else
					code = departmentService.queryForTuhao(code);
			}
			
			qo.addQuery("department.code = ?", new Object[]{code});
				
			//根据工位查询
			if(null!=form.get("role")&&!"".equals(form.get("role").toString())){
				String role = form.get("role").toString();
				Role r = roleService.getRole(role);
				qo.addQuery("? MEMBER obj.roleList", new Object[]{r});
			}
			
			//根据文件类型
			if(null!=form.get("fileClass")&&!"".equals(form.get("fileClass").toString())){
				String fileClass = form.get("fileClass").toString();
				if(-1==fileClass.indexOf(".")){                  //是id的情况
					Long fileClassId = Long.valueOf(form.get("fileClass").toString());
					qo.addQuery("fileClass.id = ?", new Object[]{fileClassId});
				}else{                                           //是code的情况
					qo.addQuery("fileClass.code = ?", new Object[]{fileClass});
				}
			}
			
			if(null!=form.get("serial")&&!"".equals(form.get("serial").toString())){
				String serial = form.get("serial").toString();
				qo.addQuery(" code like '%"+serial+"%'", null);
			}
			
			qo.addQuery(" disabled = ?", new Object[]{false});
			
			pageList = service.getEmployeeBy(qo);
			List list = pageList.getResult();
			if(null!=list)
				for(Object o : list){
					Employee e = (Employee) o;			
					File file = new File(fileSavePath+e.getCode()+"/"+e.getFileClass().getCode()+"/");
						
					if(null!=file.listFiles()){
						for(File f :file.listFiles()){
								if(f.isFile()){
									String path = "file-backups/"+e.getCode()+"/"+e.getFileClass().getCode()+"/";
									String name = f.getName();								
									History h = new History();
									h.setDate(name.split("]")[2].substring(1));
									h.setCode(name.split("]")[0].substring(1));
									h.setUser(name.split("]")[1].substring(1));
									h.setVersion(name.split("]")[3].substring(1));
									h.setBrowse(path+name);
									h.setDownload(path+name);
									files.add(h);
								}
						}
					}
				}
		}
		
		form.addResult("files", files);
		return page("history");
	}
	
	public Page extrafile(WebForm form) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = null;
		List<Map<String,String>> files = new ArrayList<Map<String,String>>();
		
		//根据物料查询
		if(null!=form.get("code")&&!"".equals(form.get("code").toString())){
			String code = form.get("code").toString();
			qo.addQuery("code = ?", new Object[]{code});
			qo.addQuery(" disabled = ?", new Object[]{false});
			
			pageList = service.getEmployeeBy(qo);
			List list = pageList.getResult();
			if(null!=list)
				for(Object o : list){
					Employee e = (Employee) o;			
					File file = new File(fileSavePath+e.getCode()+"/"+e.getFileClass().getCode()+"/"+UploadAction.ExtraPath+"/");
						
					if(null!=file.listFiles()){
						for(File f :file.listFiles()){
								String download = "file-backups/"+e.getCode()+"/"+e.getFileClass().getCode()+"/"+UploadAction.ExtraPath+"/";
								String path = e.getCode()+"/"+e.getFileClass().getCode()+"/"+UploadAction.ExtraPath+"/";
								String name = f.getName();								
								Map<String,String> bean = new HashMap<String,String>();
								bean.put("name", name);
								bean.put("download", download+name);
								bean.put("path", path);
								files.add(bean);
						}
					}
				}
		}
		
		form.addResult("files", files);
		return page("extrafile");
	}
	
	public Page deletefile(WebForm form) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		List<Map<String,String>> files = new ArrayList<Map<String,String>>();
		
		if(null!=form.get("path")&&!"".equals(form.get("path").toString())){
			String path = form.get("path").toString();
			String name = form.get("name").toString();
			/*try {
				name = new String(form.get("name").toString().getBytes("iso-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			File file = new File(fileSavePath+path);
			boolean extraEmpty = true;
			if(null!=file.listFiles()){
				for(File f :file.listFiles()){
					if(f.getName().equals(name))
						f.delete();
					else{
						String download = "file-backups/"+path;
						Map<String,String> bean = new HashMap<String,String>();
						bean.put("name", f.getName());
						bean.put("download", download+f.getName());
						bean.put("path", path);
						files.add(bean);
						
						extraEmpty = false;
					}
				}
				
				String fileCode = path.substring(0, path.indexOf("/"));
				Employee emp = service.getEmployee(fileCode);
				emp.setExtraEmpty(extraEmpty);
				service.updateEmployee(emp.getId(), emp);
			}
		}
		
		form.addResult("files", files);
		return page("extrafile");
	}
	
	public Page check(WebForm form){
		Object resp = form.get("resp");
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(resp.toString());
			JSONArray list = (JSONArray) obj.get("rows");
			for(Object jboj : list){
				JSONObject row = (JSONObject) jboj;
				try{
					Long id = new Long(CommUtil.null2String(row.get("id")));
					Employee employee = service.getEmployee(id);
					employee.setChecked(true);
					employee.setChecker(getUser().getAccount());
					employee.setCheckDate(new Date(System.currentTimeMillis()));
					service.updateEmployee(id, employee);
				} catch(java.lang.NumberFormatException e){
					continue;
				}
			}
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success2(form, true, "处理完毕！");
	}
	
	public Page uncheck(WebForm form){
		Object resp = form.get("resp");
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(resp.toString());
			JSONArray list = (JSONArray) obj.get("rows");
			for(Object jboj : list){
				JSONObject row = (JSONObject) jboj;
				try{
					Long id = new Long(CommUtil.null2String(row.get("id")));
					Employee employee = service.getEmployee(id);
					employee.setChecked(false);
					employee.setChecker(null);
					employee.setCheckDate(null);
					service.updateEmployee(id, employee);
				} catch(java.lang.NumberFormatException e){
					continue;
				}
			}
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success2(form, true, "处理完毕！");
	}
	
	public static void main(String[] args) {
		String s = "CSS测试";  
        try {  
            //css or js post/get data submmit  
            s = URLEncoder.encode(s, "UTF-8");  
            System.out.println("encode :"+s);  
            //backstage java/jsp dispose  
            s = URLDecoder.decode(s, "UTF-8");  
            System.out.println("decode :"+s);  
            s = new String(s.getBytes(),"GBK");  
            System.out.println("GBK :" +s);  
            //combine encoding change  
        //  s = new String(URLDecoder.decode(s, "UTF-8").getBytes(),"GBK");  
        } catch (UnsupportedEncodingException e) {  
            System.out.println("encoding cause,change failure");  
        }catch (Exception e) {  
            System.out.println("others cause,change failure");  
        }  
	}
}