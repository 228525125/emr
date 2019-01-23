package com.mhsoft.emr.mvc;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.FileItem;

import com.easyjf.container.annonation.Inject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.util.CommUtil;
import com.easyjf.web.ActionContext;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.Notify;
import com.mhsoft.emr.service.INotifyService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.util.Logger;

public class NotifyAction extends BaseAction {

	public final static String ExtraPath = "extra";
	
	@Inject
	private INotifyService service;
	/*
	 * set the current service
	 * return service
	 */
	public void setService(INotifyService service) {
		this.service = service;
	}
	
	public Page doIndex(WebForm form, Module m) {
		if("admin".equals(getUser().getAccount()) || "zhaogaolin".equals(getUser().getAccount()) || "zhangyiya".equals(getUser().getAccount())){
			ServletContext sc = ActionContext.getContext().getSession().getServletContext();
			String fileSavePath = sc.getInitParameter("uploadPath");
			
			Notify object;
			if(null!=form.get("id")&&!"".equals(form.get("id").toString())){
				Long id = new Long(CommUtil.null2String(form.get("id")));
				object = service.getNotify(id);
				
				form.toPo(object, true);
				if (!hasErrors()){
					Boolean empty = object.getEmpty();   //是否引用或上传文档
					
					if(!form.getFileElement().keySet().isEmpty()){
						String selected = saveFile(form, fileSavePath+object.getCode());
						if(!"".equals(selected)){       //表示没有上传文件，只是修改了字段
							object.setSelected(selected);
							empty = false;
							//只有上传文件才更新时间
							object.setDate(new Date(System.currentTimeMillis()));
							
							object.setAddress("file-backups/"+object.getCode()+"/"+selected);
						}
					}
					
					if(!"".equals(saveExtraFile(form, fileSavePath+object.getCode()+"/"+ExtraPath))){
						object.setExtraEmpty(false);
					}
					
					object.setEmpty(empty);
					
					service.updateNotify(id, object);
				}
			}else{
				object = form.toPo(Notify.class);
				if (!hasErrors()){
					Boolean empty = object.getEmpty();   //是否引用或上传文档
					
					if(!form.getFileElement().keySet().isEmpty()){
						String selected = saveFile(form, fileSavePath+object.getCode());
						if(!"".equals(selected)){       //表示上传文件
							object.setSelected(selected);
							empty = false;
							
							//只有上传文件才更新时间
							object.setDate(new Date(System.currentTimeMillis()));
							
							object.setAddress("file-backups/"+object.getCode()+"/"+selected);
						}
					}
					
					if(!"".equals(saveExtraFile(form, fileSavePath+object.getCode()+"/"+ExtraPath))){
						object.setExtraEmpty(false);
					}
					
					object.setEmpty(empty);
					
					service.addNotify(object);
				}
			}
		}

		Page page = pageForExtForm(form);
		page.setContentType("text/html;charset=UTF-8");
		return page;
	}
	
	public Page extrafile(WebForm form) {
		ServletContext sc = ActionContext.getContext().getSession().getServletContext();
		String fileSavePath = sc.getInitParameter("uploadPath");
		
		QueryObject qo = form.toPo(QueryObject.class);
		IPageList pageList = null;
		List<Map<String,String>> files = new ArrayList<Map<String,String>>();
		
		//根据物料查询
		if(null!=form.get("code")&&!"".equals(form.get("code").toString())){
			String code = form.get("code").toString();
			qo.addQuery("code = ?", new Object[]{code});
			qo.addQuery(" disabled = ?", new Object[]{false});
			
			pageList = service.getNotifyBy(qo);
			List list = pageList.getResult();
			if(null!=list)
				for(Object o : list){
					Notify e = (Notify) o;			
					File file = new File(fileSavePath+e.getCode()+"/"+UploadAction.ExtraPath+"/");
						
					if(null!=file.listFiles()){
						for(File f :file.listFiles()){
								String download = "file-backups/"+e.getCode()+"/"+UploadAction.ExtraPath+"/";
								String path = e.getCode()+"/"+UploadAction.ExtraPath+"/";
								String name = f.getName();								
								Map<String,String> bean = new HashMap<String,String>();
								bean.put("name", name);
								bean.put("download", download+name);
								bean.put("path", path);
								files.add(bean);
						}
					}
				}
		}
		
		form.addResult("files", files);
		return page("extrafile");
	}
	
	/**
	 * 保存文件
	 * @param form
	 * @param upLoadPath
	 * @return
	 */
	private String saveFile(WebForm form, String upLoadPath){
		String fileName = "";
		Set keys = form.getFileElement().keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {				
			String fieldName = (String) it.next();
			if("file".equals(fieldName)){
				FileItem item = (FileItem) form.getFileElement().get(fieldName);
				if (item.getSize() == 0) {
					break;
				}

			    File file = new File(upLoadPath);
			    
			    try {
			    	if(!file.exists()){
						file.mkdirs();
					} 
			    	
			    	fileName = item.getName(); 
			    	
			    	file = new File(upLoadPath+"/"+fileName);
			    	if(!file.exists()){
			    		file.createNewFile();
			    	}
			    	item.write(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				fieldName = "";
			}
		}
		return fileName;
	}
	
	/**
	 * 只保存附件
	 * @param form
	 * @param upLoadPath
	 * @param fileCode
	 * @param user
	 * @param version
	 * @return
	 */
	private String saveExtraFile(WebForm form, String upLoadPath){
		String fileName = "";
		Set keys = form.getFileElement().keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {				
			String fieldName = (String) it.next();
			if("extrafile".equals(fieldName)){
				FileItem item = (FileItem) form.getFileElement().get(fieldName);
				if (item.getSize() == 0) {
					break;
				}

			    File file = new File(upLoadPath);
			    
			    try {
			    	if(!file.exists()){
						file.mkdirs();
					}
			    	
			    	fileName = item.getName(); 
			    	
			    	file = new File(upLoadPath+"/"+fileName);
			    	if(!file.exists()){
			    		file.createNewFile();
			    	}
			    	item.write(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				fieldName = "";
			}
		}
		
		return fileName;
	}

	public Page doList(WebForm form) {
		QueryObject qo = form.toPo(QueryObject.class);
		
		if(null!=form.get("query")&&!"".equals(form.get("query").toString())){			
			String query = form.get("query").toString();
			qo.addQuery("(obj.name like '%"+query+"%' or obj.code like '%"+query+"%') ", null);
		}
		
		if(null!=form.get("disabled")&&!"".equals(form.get("disabled").toString())){
			Boolean disabled = new Boolean(form.get("disabled").toString());
			qo.addQuery("obj.disabled=?", new Object[]{disabled});
		}
		
		IPageList pageList = service.getNotifyBy(qo);
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("currentPage", pageList.getCurrentPage());
		result.put("nextPage", pageList.getNextPage());
		result.put("pages", pageList.getPages());
		result.put("pageSize", pageList.getPageSize());
		result.put("previousPage", pageList.getPreviousPage());
		result.put("rowCount", pageList.getRowCount());
		List list = pageList.getResult();
		List<Map> res = new ArrayList<Map>();
		if(null!=list){
			for(Object obj : list){
				Notify n = (Notify) obj;
				Map<String,Object> bean = new HashMap<String, Object>();
				bean.put("id", n.getId());
				bean.put("code", n.getCode());
				bean.put("name", n.getName());
				bean.put("disabled", n.getDisabled());
				bean.put("description", n.getDescription());
				bean.put("address", n.getAddress());
				bean.put("empty", n.getEmpty());
				bean.put("selected", n.getSelected());
				bean.put("extraEmpty", n.getExtraEmpty());
				bean.put("checked", n.getChecked());
				bean.put("checker", n.getChecker());
				
				if(null!=n.getDate()){
					SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.put("date", sFormat.format(n.getDate()));
				}
				
				if(null!=n.getCheckDate()){
					SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					bean.put("checkDate", sFormat.format(n.getCheckDate()));
				}
				
				res.add(bean);
			}
		}
		
		result.put("result", res);
		form.jsonResult(result);
		return Page.JSONPage;
	}

	public Page doRemove(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		service.delNotify(id);
		return pageForExtForm(form);
	}

	public Page doSave(WebForm form) {
		Notify object = form.toPo(Notify.class);
		if (!hasErrors())
			service.addNotify(object);
		return pageForExtForm(form);
	}
	
	public Page doUpdate(WebForm form) {
		Long id = new Long(CommUtil.null2String(form.get("id")));
		Notify object = service.getNotify(id);
		form.toPo(object, true);
		if (!hasErrors())
			service.updateNotify(id, object);
		return pageForExtForm(form);
	}
}
