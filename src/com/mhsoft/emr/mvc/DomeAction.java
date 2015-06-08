package com.mhsoft.emr.mvc;

import java.util.List;
import java.util.Map;

import com.mhsoft.emr.domain.Dome;
import com.mhsoft.emr.service.IDomeService;
import com.mhsoft.emr.service.JDBCQueryService;

import com.easyjf.container.annonation.Action;
import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.core.AbstractPageCmdAction;
import com.easyjf.web.tools.IPageList;


public class DomeAction extends BaseAction {
	
	@Inject
	private IDomeService service;
	
	@Inject
	private JDBCQueryService jdbcService;
	
	public void setJdbcService(JDBCQueryService jdbcService) {
		this.jdbcService = jdbcService;
	}

	public void setService(IDomeService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm f, Module m) {
		return page("index");
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = service.getDomeBy(qo);
		form.jsonResult(pageList);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delDome(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Dome object = form.toPo(Dome.class);
		if (!hasErrors())
			service.addDome(object);
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Dome object = service.getDome(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateDome(id, object);
		return pageForExtForm(form);
	}
	
	public Page items(WebForm form){
		QueryObject qo = form.toPo(QueryObject.class);		
		String query = null;
		String queryType = null;
		if(null!=form.get("totalSQL")&&!"".equals(form.get("totalSQL").toString())
		 &&null!=form.get("querySQL")&&!"".equals(form.get("querySQL").toString())
		 &&null!=form.get("isCallable")&&!"".equals(form.get("isCallable").toString())
		 &&null!=form.get("queryFields")&&!"".equals(form.get("queryFields").toString())){
			String totalSQL = form.get("totalSQL").toString();
			String querySQL = form.get("querySQL").toString();
			Boolean isCallable = Boolean.valueOf(form.get("isCallable").toString());
			String queryFields = form.get("queryFields").toString();
			String condition = "";
			if(isCallable){
				
			}else{
				if(null!=form.get("query")&&!"".equals(form.get("query").toString())){
					query = form.get("query").toString();
					condition += " and (";
					String [] cs = queryFields.split(",");
					for(int i=0;i<cs.length;i++){
						condition += cs[i]+" like '%"+query+"%'";
						if(i<(cs.length-1))
							condition += " or ";
					}
					condition += " ) ";
				}
			}
			if(-1!=totalSQL.indexOf("&1")){
				totalSQL = totalSQL.replaceAll("&1", condition);
			}
			
			if(-1!=querySQL.indexOf("&1")){
				querySQL = querySQL.replaceAll("&1", condition);
			}
			
			List list = jdbcService.query_dynamicgrid(qo, totalSQL, querySQL, isCallable);
			
			if(null!=form.get("qType")&&!"".equals(form.get("qType").toString())){
				queryType = form.get("qType").toString();
			}
			if("store".equals(queryType))               //如果是store.load方法，就不返回表头信息
				form.jsonResult(list.get(0));
			else
				form.jsonResult(list);                  //这种方式表示为Ajax方式
		}
		return Page.JSONPage;
	}
}