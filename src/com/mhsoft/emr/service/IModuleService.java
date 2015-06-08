package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Module;
/**
 * ModuleService
 * @author EasyJWeb 1.0-m2
 * $Id: ModuleService.java,v 0.0.1 2011-3-11 11:13:51 EasyJWeb 1.0-m2 Exp $
 */
public interface IModuleService {
	/**
	 * 保存一个Module，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addModule(Module instance);
	
	/**
	 * 根据一个ID得到Module
	 * 
	 * @param id
	 * @return
	 */
	Module getModule(Long id);
	
	/**
	 * 删除一个Module
	 * @param id
	 * @return
	 */
	boolean delModule(Long id);
	
	/**
	 * 批量删除Module
	 * @param ids
	 * @return
	 */
	boolean batchDelModules(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Module
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getModuleBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Module
	  * @param id 需要更新的Module的id
	  * @param dir 需要更新的Module
	  */
	boolean updateModule(Long id,Module instance);
	
	String loadTree(Integer currentNodeId);
}
