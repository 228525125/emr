package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.Glossary;
import com.mhsoft.emr.service.IGlossaryService;

import com.easyjf.container.annonation.Action;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.easyjf.web.tools.IPageList;


public class GlossaryAction extends BaseAction {
	
	@Inject
	private IGlossaryService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IGlossaryService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = service.getGlossaryBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delGlossary(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Glossary object = form.toPo(Glossary.class);
		if (!hasErrors())
			service.addGlossary(object);
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Glossary object = service.getGlossary(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateGlossary(id, object);
		return pageForExtForm(form);
	}
}