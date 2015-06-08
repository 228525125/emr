package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.Module;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IModuleService;

import com.easyjf.container.annonation.Action;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.easyjf.web.tools.IPageList;

public class ModuleAction extends BaseAction {
	
	@Inject
	private IModuleService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IModuleService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, com.easyjf.web.Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		if(null!=form.get("moduleId")&&!"".equals(form.get("moduleId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("moduleId")));
			if(0l==id)
				qo.addQuery("parent is null", null);
			else
				qo.addQuery("parent.id=?", new Object[]{id});
		}
		IPageList pageList = service.getModuleBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delModule(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Module object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getModule(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateModule(id, object);
		}else{
			object = form.toPo(Module.class);
			if (!hasErrors())
				service.addModule(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Module object = service.getModule(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateModule(id, object);
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