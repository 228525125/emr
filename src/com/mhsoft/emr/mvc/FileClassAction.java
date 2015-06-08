package com.mhsoft.emr.mvc;

import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.FileClass;
import com.mhsoft.emr.service.IFileClassService;
import com.mhsoft.emr.util.AppContext;

public class FileClassAction extends BaseAction {
	
	@Inject
	private IFileClassService service;

	public void setService(IFileClassService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("list");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		if(null!=form.get("fileClassId")&&!"".equals(form.get("fileClassId").toString())){
			Long id = new Long(CommUtil.null2String(form.get("fileClassId")));
			if(0l==id)
				qo.addQuery("parent is null", null);
			else if(AppContext.SELECT_ALL==id)
				;
			else
				qo.addQuery("parent.id=?", new Object[]{id});
		}
		
		IPageList pageList = service.getFileClassBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}
	
	public Page doSelectList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		qo.addQuery("parent is not null", null);
		IPageList pageList = service.getFileClassBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delFileClass(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		FileClass object;
		if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
			Long id = new Long(CommUtil.null2String(form.get("id")));
			object = service.getFileClass(id);
			form.toPo(object, true);
			if (!hasErrors())
				service.updateFileClass(id, object);
		}else{
			object = form.toPo(FileClass.class);
			if (!hasErrors())
				service.addFileClass(object);
		}
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		FileClass object = service.getFileClass(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateFileClass(id, object);
		return pageForExtForm(form);
	}
	
	public Page tree(WebForm form){
		if(null!=form.get("fileClassId")&&!"".equals(form.get("fileClassId").toString())){
			Integer id = new Integer(form.get("fileClassId").toString());			
			form.addResult("json", service.loadTree(id));			
		}
		return getJsonByPage();
	}
}