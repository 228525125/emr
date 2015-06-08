package com.mhsoft.emr.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.domain.Role;
import com.mhsoft.emr.service.IEmployeeService;
import com.mhsoft.emr.service.IRoleService;
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

public class RoleAction extends BaseAction {
	
	@Inject
	private IRoleService service;
	
	@Inject
	private IEmployeeService employeeService;
	
	public void setEmployeeService(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setService(IRoleService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		if(null!=form.get("roleId")&&!"".equals(form.get("roleId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("roleId")));
			if(0l==id)
				qo.addQuery("parent is null", null);
			else if(AppContext.SELECT_ALL==id)
				;
			else
				qo.addQuery("parent.id=?", new Object[]{id});
		}
		
		//根据职员查询包含该职员的角色
		if(null!=form.get("employeeId")&&!"".equals(form.get("employeeId").toString())){
			Long eid = new Long(CommUtil.null2String(form.get("employeeId")));
			Employee emp = employeeService.getEmployee(eid);
			qo.addQuery("? MEMBER obj.employeeList", new Object[]{emp});
		}
		IPageList pageList = service.getRoleBy(qo);
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
				Role r = (Role) obj;
				Map<String,Object> bean = new HashMap<String, Object>();
				bean.put("id", r.getId());
				bean.put("code", r.getCode());
				bean.put("name", r.getName());
				bean.put("disabled", r.getDisabled());
				bean.put("description", r.getDescription());
				res.add(bean);
			}
		}
		result.put("result", res);
		
		form.jsonResult(result);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delRole(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Role object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getRole(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateRole(id, object);
		}else{
			object = form.toPo(Role.class);
			if (!hasErrors())
				service.addRole(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Role object = service.getRole(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateRole(id, object);
		return pageForExtForm(form);
	}
	
	public Page tree(WebForm form){
		if(null!=form.get("roleId")&&!"".equals(form.get("roleId").toString())){
			Integer id = new Integer(form.get("roleId").toString());			
			form.addResult("json", service.loadTree(id));			
		}
		return getJsonByPage();
	}
}