package com.mhsoft.emr.mvc;

import com.mhsoft.emr.domain.EmrUser;
import com.mhsoft.emr.service.IEmrUserService;

import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.ActionContext;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.ExtResult;
import com.easyjf.web.tools.IPageList;

public class EmrUserAction extends BaseAction {
	
	@Inject
	private IEmrUserService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(IEmrUserService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("index");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = service.getEmrUserBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delEmrUser(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		EmrUser object = form.toPo(EmrUser.class);
		if (!hasErrors())
			service.addEmrUser(object);
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		EmrUser object = service.getEmrUser(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateEmrUser(id, object);
		return pageForExtForm(form);
	}
	
	public void validate(WebForm form){
		String account = form.get("account").toString();
		String pwd = form.get("password").toString();
		EmrUser user = service.getEmrUser(account);
		if(null!=user){
			if(user.getPassword().equals(pwd)){
				ActionContext.getContext().getSession().setAttribute("user", user);
				page("loading");
			}else{
				form.addResult("msg", "密码错误！");
				page("index");
			}
		}else{
			form.addResult("msg", "用户不存在！");
			page("index");
		}
	}
	
	public Page validate1(WebForm form){
		ExtResult r = new ExtResult();		
		if(getNeedLogin()){
			String acc = form.get("account").toString();
			String pwd = form.get("password").toString();
			EmrUser user = service.getEmrUser(acc);
			if (null!=user&&pwd.equals(user.getPassword())){
				ActionContext.getContext().getSession().setAttribute("user", user);
				r.setSuccess(true);	
			}
			else{
				r.setSuccess(false);
				r.getErrors().put("password", "用户或密码不正确，请检查！");
			}
		}else{
			r.setSuccess(true);
		}
		form.jsonResult(r);
		this.forwardPage=Page.JSONPage;
		return Page.JSONPage;
	}
}