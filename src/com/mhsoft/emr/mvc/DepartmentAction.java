package com.mhsoft.emr.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IDepartmentService;
import com.mhsoft.emr.service.IEmployeeService;
import com.mhsoft.emr.util.AppContext;
import com.easyjf.container.annonation.Action;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.easyjf.web.tools.IPageList;

/**
 * 
 * @author 陈贤
 * 需要实现的方法：
 * 根据机构、部门查询下级部门列表 参数：上级机构id，上级部门id 返回：下级部门列表
 * 根据机构、部门查询下级部门的树 参数：上级机构id，上级部门id 返回：下级部门的树形目录
 * 根据职员查询职员所的部门列表   参数：职员id 返回：部门列表
 * 
 * 注意：
 * 一个部门可以根据机构和部门的id来确定，部门可以为null或为all（表示不加任何条件），但必须有机构id
 * 虽然下级部门只需要知道上级部门id就能确定，但因为处理树形目录时，最上级部门的上级部门为null，而机构id是不能为空的，
 * 所以统一规定：部门必须根据机构和上级部门两个id才能确定。
 * 
 */
public class DepartmentAction extends BaseAction {
	
	@Inject
	private IDepartmentService service;
	
	@Inject
	private IEmployeeService employeeService;

	public void setService(IDepartmentService service) {
		this.service = service;
	}
	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		//根据机构、部门查询下级部门
		if(null!=form.get("departmentId")&&!"".equals(form.get("departmentId").toString())       
		 &&null!=form.get("organizationId")&&!"".equals(form.get("organizationId").toString())){   //根据机构和上级部门编号，返回下级部门
			Long oid = new Long(CommUtil.null2String(form.get("organizationId")));
			Long did = new Long(CommUtil.null2String(form.get("departmentId")));
			if(0l==oid)
				qo.addQuery("organization is null", null);
			else
				qo.addQuery("organization.id=?", new Object[]{oid});
			if(0l==did)
				qo.addQuery("parent is null", null);
			else if(AppContext.SELECT_ALL==did)
				;
			else
				qo.addQuery("parent.id=?", new Object[]{did});
			qo.setOrderBy("code");
		}
		//根据职员查询部门
		if(null!=form.get("employeeId")&&!"".equals(form.get("employeeId").toString())){
			Long eid = new Long(CommUtil.null2String(form.get("employeeId")));
			Employee emp = employeeService.getEmployee(eid);
			qo.addQuery("? MEMBER obj.employeeList", new Object[]{emp});
			qo.addQuery("grade=4", null);       //最下级物料
		}
		//根据条件模糊查找部门		
		if(null!=form.get("query")&&!"".equals(form.get("query").toString())){			
			String query = form.get("query").toString();
			qo.addQuery("tuhao like '%"+query+"%'", null);
		}
		
		//不允许为空的字段
		if(null!=form.get("notNullField")&&!"".equals(form.get("notNullField").toString())){
			String notNullField = form.get("notNullField").toString();
			qo.addQuery(notNullField+" is not null and "+notNullField+"<>''", null);
		}
		
		IPageList pageList = service.getDepartmentBy(qo);		
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
				Department dep = (Department) obj;
				Map<String,Object> bean = new HashMap<String, Object>();
				bean.put("id", dep.getId());
				bean.put("code", dep.getCode());
				bean.put("name", dep.getName());
				bean.put("auxCode", dep.getAuxCode());
				bean.put("disabled", dep.getDisabled());
				bean.put("description", dep.getDescription());
				bean.put("model", dep.getModel());
				bean.put("tuhao", dep.getTuhao());
				Map<String,Object> organization = new HashMap<String,Object>();
				if(null!=dep.getOrganization()){
					organization.put("id", dep.getOrganization().getId());
					organization.put("name", dep.getOrganization().getName());
					bean.put("organization", organization);
				}else{
					bean.put("organization", null);
				}
				
				Map<String,Object> parent = new HashMap<String,Object>();
				if(null!=dep.getParent()){
					parent.put("id", dep.getParent().getId());
					parent.put("name", dep.getParent().getName());
					bean.put("parent", parent);
				}else{
					bean.put("parent", null);
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
		service.delDepartment(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Department object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getDepartment(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateDepartment(id, object);
		}else{
			object = form.toPo(Department.class);
			if (!hasErrors())
				service.addDepartment(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Department object = service.getDepartment(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateDepartment(id, object);
		return pageForExtForm(form);
	}
	
	public Page tree(WebForm form){
		if(null!=form.get("departmentId")&&!"".equals(form.get("departmentId").toString())
		 &&null!=form.get("organizationId")&&!"".equals(form.get("organizationId").toString())){    //根据机构和上级部门编号获取下级部门列表
			Integer did = new Integer(form.get("departmentId").toString());
			Integer oid = new Integer(form.get("organizationId").toString());
			form.addResult("json", service.loadTree(oid,did));			
		}
		return getJsonByPage();
	}
}