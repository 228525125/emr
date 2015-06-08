package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.DataLimit;
import com.mhsoft.emr.domain.OperateLimit;
import com.mhsoft.emr.service.IDataLimitService;

import com.easyjf.container.annonation.Action;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.easyjf.web.tools.IPageList;

public class DataLimitAction extends BaseAction {
	
	@Inject
	private IDataLimitService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IDataLimitService service) {
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
		IPageList pageList = service.getDataLimitBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delDataLimit(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		DataLimit object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getDataLimit(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateDataLimit(id, object);
		}else{
			object = form.toPo(DataLimit.class);
			if (!hasErrors())
				service.addDataLimit(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		DataLimit object = service.getDataLimit(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateDataLimit(id, object);
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