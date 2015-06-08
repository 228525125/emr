package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.Weishengju;
import com.mhsoft.emr.service.IWeishengjuService;

import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.tools.IPageList;

public class WeishengjuAction extends BaseAction {
	
	@Inject
	private IWeishengjuService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IWeishengjuService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		if(null!=form.get("weishengjuId")&&!"".equals(form.get("weishengjuId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("weishengjuId")));
			if(0l==id)
				qo.addQuery("parent is null", null);
			else
				qo.addQuery("parent.id=?", new Object[]{id});
		}
		IPageList pageList = service.getWeishengjuBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delWeishengju(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Weishengju object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getWeishengju(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateWeishengju(id, object);
		}else{
			object = form.toPo(Weishengju.class);
			if (!hasErrors())
				service.addWeishengju(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Weishengju object = service.getWeishengju(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateWeishengju(id, object);
		return pageForExtForm(form);
	}
	
	public Page tree(WebForm form){
		if(null!=form.get("weishengjuId")&&!"".equals(form.get("weishengjuId").toString())){
			Integer id = new Integer(form.get("weishengjuId").toString());			
			form.addResult("json", service.loadTree(id));			
		}
		return getJsonByPage();
	}
}