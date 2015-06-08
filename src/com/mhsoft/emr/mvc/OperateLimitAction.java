package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.OperateLimit;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IOperateLimitService;

import com.easyjf.container.annonation.Action;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.easyjf.web.tools.IPageList;

public class OperateLimitAction extends BaseAction {
	
	@Inject
	private IOperateLimitService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IOperateLimitService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		if(null!=form.get("moduleId")&&!"".equals(form.get("moduleId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("moduleId")));
			if(0l==id)
				qo.addQuery("module is null", null);
			else
				qo.addQuery("module.id=?", new Object[]{id});
		}
		IPageList pageList = service.getOperateLimitBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delOperateLimit(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		OperateLimit object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getOperateLimit(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateOperateLimit(id, object);
		}else{
			object = form.toPo(OperateLimit.class);
			if (!hasErrors())
				service.addOperateLimit(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		OperateLimit object = service.getOperateLimit(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateOperateLimit(id, object);
		return pageForExtForm(form);
	}
	
	public Page tree(WebForm form){
		if(null!=form.get("moduleId")&&!"".equals(form.get("moduleId").toString())){
			Integer id = new Integer(form.get("moduleId").toString());			
			form.addResult("json", service.loadTree(id));			
		}
		return getJsonByPage();
	}
}