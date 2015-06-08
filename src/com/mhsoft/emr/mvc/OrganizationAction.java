package com.mhsoft.emr.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IOrganizationService;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.tools.IPageList;

/**
 * 
 * @author 陈贤
 * 需要实现的方法：
 * 根据上级机构查询下级机构 参数：上级机构id 返回：下级机构列表
 * 根据上级机构查询下级机构的树 参数：上级机构id 返回：下级机构的树形目录
 *
 */
public class OrganizationAction extends BaseAction {
	
	@Inject
	private IOrganizationService service;
	
	public void setService(IOrganizationService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		if(null!=form.get("organizationId")&&!"".equals(form.get("organizationId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("organizationId")));
			if(0l==id)
				qo.addQuery("parent is null", null);
			else
				qo.addQuery("parent.id=?", new Object[]{id});
		}
		IPageList pageList = service.getOrganizationBy(qo);
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
				Organization org = (Organization) obj;
				Map<String,Object> bean = new HashMap<String, Object>();
				bean.put("id", org.getId());
				bean.put("code", org.getCode());
				bean.put("name", org.getName());
				bean.put("disabled", org.getDisabled());
				bean.put("description", org.getDescription());
				bean.put("address", org.getAddress());
				bean.put("contact", org.getContact());
				bean.put("legalPerson", org.getLegalPerson());
				res.add(bean);
			}
		}
		result.put("result", res);
		form.jsonResult(result);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delOrganization(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Organization object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getOrganization(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateOrganization(id, object);
		}else{
			object = form.toPo(Organization.class);
			if (!hasErrors())
				service.addOrganization(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Organization object = service.getOrganization(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateOrganization(id, object);
		return pageForExtForm(form);
	}
	
	public Page tree(WebForm form){
		if(null!=form.get("organizationId")&&!"".equals(form.get("organizationId").toString())){
			Integer id = new Integer(form.get("organizationId").toString());			
			form.addResult("json", service.loadTree(id));			
		}
		return getJsonByPage();
	}
}