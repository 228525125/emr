package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.Station;
import com.mhsoft.emr.service.IStationService;

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
 * StationAction
 * @author EasyJWeb 1.0-m2
 * $Id: StationAction.java,v 0.0.1 2011-3-11 11:14:25 EasyJWeb 1.0-m3 with ExtJS Exp $
 */
@Action
public class StationAction extends AbstractPageCmdAction {
	
	@Inject
	private IStationService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IStationService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = service.getStationBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delStation(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Station object = form.toPo(Station.class);
		if (!hasErrors())
			service.addStation(object);
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Station object = service.getStation(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateStation(id, object);
		return pageForExtForm(form);
	}
}