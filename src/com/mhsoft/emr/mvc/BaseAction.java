package com.mhsoft.emr.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.easyjf.web.ActionContext;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.mhsoft.emr.domain.EmrUser;
import com.mhsoft.emr.util.JsonUtil;

public class BaseAction extends AbstractPageCmdAction {
	
	private String port = "80";
	private String host = "http://www.mhsoft.com";
	private Boolean needLogin = true;
	private EmrUser user;
	
	public EmrUser getUser() {
		return user;
	}

	public Boolean getNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(Boolean needLogin) {
		this.needLogin = needLogin;
	}
	
	@Override
	public Object doBefore(WebForm form, Module module) {
		// TODO Auto-generated method stub
		init();
		
		return reset(form, module);
	}
	
	@Override
	public Object doAfter(WebForm form, Module module) {
		// TODO Auto-generated method stub
		String contextPath = ActionContext.getContext().getRequest().getContextPath();
		form.addResult("ContextPath", contextPath);
		form.addResult("realPath", host+":"+port+contextPath);
		form.addResult("user", user);
		return super.doAfter(form, module);
	}
	
	protected Page getJsonByPage(){
		Page page = new Page("jsonstore","frame/store.json");
		page.setContentType("application/json;charset=UTF-8");
		return page;
	}
	
	protected void init(){
		user = (EmrUser) ActionContext.getContext().getSession().getAttribute("user");
	}
	
	protected Page reset(WebForm form, Module module){	
		return null;
	}
	
	protected Page getXmlByPage(){
		Page page = new Page("xmlPage","test/data.xml");
		page.setContentType("xml");
		return page;
	}
	
	protected Page getXmlByPage(WebForm form, Object data){
		form.addResult("xml", data);
		return getXmlByPage();
	}
	
	/**
	 * 
	 * @param data 类型必须为List<Map> / Map / String []
	 * @return
	 */
	protected Page getJsonByPage(WebForm form, Object data){
		if (data instanceof List) {
			List list = (List) data;
			form.addResult("json", JsonUtil.toJSONArray(JSONArray.toJSONString(list)));
		}else if(data instanceof Map){
			Map map = (Map) data;
			form.addResult("json", JsonUtil.toJSONObject(JSONObject.toJSONString(map)));
		}else if(data instanceof String[]){
			String[] values = (String[]) data;
			form.addResult("json", JsonUtil.toJSONValue(values[0], values[1]));
		}else{
			form.addResult("json", JsonUtil.toJSONEmpty());
		}
		return getJsonByPage();
	}
	
	/**
	 * 该方法对应，Ext 的 doAction 的回调方法success/failure
	 * @param form
	 * @param isSuccess 如果true 则触发success，false则触发failure 
	 * @param data 类型必须为List<Map> / Map / String []  或者为null
	 * @param msg 可以为null
	 * @return
	 */
	protected Page success(WebForm form, boolean isSuccess, Object data, String msg){
		form.addResult("json", JsonUtil.success(isSuccess, data, msg));
		return getJsonByPage();
	}
	
	/**
	 * 该方法与上面的方法不同之处在于，这个方法用于Ext.Ajax.request.success回调方法
	 * @param form
	 * @param isSuccess
	 * @param data String / Map
	 * @return
	 */
	protected Page success2(WebForm form, boolean isSuccess, Object data){
		Map map = new HashMap();
		if (data instanceof String) {
			map.put("msg", data);			
		}else if(data instanceof Map){
			map = (Map) data;
		}		
		map.put("success", isSuccess);							
		return getJsonByPage(form, map);
	}
	
}
