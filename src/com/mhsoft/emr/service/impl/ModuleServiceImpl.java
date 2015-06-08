package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Module;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IModuleService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.dao.IModuleDAO;


/**
 * ModuleServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: ModuleServiceImpl.java,v 0.0.1 2011-3-11 11:13:51 EasyJWeb 1.0-m2 Exp $
 */
public class ModuleServiceImpl implements IModuleService{
	
	private IModuleDAO moduleDao;
	
	public void setModuleDao(IModuleDAO moduleDao){
		this.moduleDao=moduleDao;
	}
	
	public Long addModule(Module module) {	
		updateLevel(module);
		this.moduleDao.save(module);
		if (module != null && module.getId() != null) {
			return module.getId();
		}
		return null;
	}
	
	public Module getModule(Long id) {
		Module module = this.moduleDao.get(id);
		return module;
		}
	
	public boolean delModule(Long id) {	
			Module module = this.getModule(id);
			if (module != null) {
				this.moduleDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelModules(List<Serializable> moduleIds) {
		
		for (Serializable id : moduleIds) {
			delModule((Long) id);
		}
		return true;
	}
	
	public IPageList getModuleBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Module.class,this.moduleDao);		
	}
	
	public boolean updateModule(Long id, Module module) {
		if (id != null) {
			module.setId(id);
		} else {
			return false;
		}
		updateLevel(module);
		this.moduleDao.update(module);
		return true;
	}	
	
	@Override
	public String loadTree(Integer currentNodeId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql;
		if(new Integer(0).equals(currentNodeId))
			sql = "select o from Module o where o.parent is null and o.disabled=0";
		else
			sql = "select o from Module o where o.parent.id="+currentNodeId+" and o.disabled=0";
		List list = moduleDao.query(sql, null, 0, AppContext.RESULTSIZE);
		for(int i=0;i<list.size();i++){
			Module bean = (Module) list.get(i);
			Long id = bean.getId();
			result += "{id:"+id+",text:'"+bean.getName()+"("+bean.getCode()+")',";
			if(isLeaf(id))
				result += "leaf:true}";
			else
				result += "leaf:false}";
			if(i<list.size()-1)
				result += ",";
		}
		return result+"]";
	}
	
	private boolean isLeaf(Long nodeId){
		String sql = "select o from Module o where o.parent.id="+nodeId+" and o.disabled=false";
		List list = moduleDao.query(sql, null, 0, 1);
		return list.isEmpty();
	}
	
	private void updateLevel(Module module){
		Module parent = module.getParent();
		if(null!=parent)
			module.setLevel(parent.getLevel()+"-"+module.getCode());
		else
			module.setLevel(module.getCode());
	}
}
