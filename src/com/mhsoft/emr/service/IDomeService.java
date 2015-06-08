package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Dome;
/**
 * DomeService
 * @author EasyJWeb 1.0-m2
 * $Id: DomeService.java,v 0.0.1 2011-3-1 10:20:29 EasyJWeb 1.0-m2 Exp $
 */
public interface IDomeService {
	/**
	 * 保存一个Dome，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addDome(Dome instance);
	
	/**
	 * 根据一个ID得到Dome
	 * 
	 * @param id
	 * @return
	 */
	Dome getDome(Long id);
	
	/**
	 * 删除一个Dome
	 * @param id
	 * @return
	 */
	boolean delDome(Long id);
	
	/**
	 * 批量删除Dome
	 * @param ids
	 * @return
	 */
	boolean batchDelDomes(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Dome
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getDomeBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Dome
	  * @param id 需要更新的Dome的id
	  * @param dir 需要更新的Dome
	  */
	boolean updateDome(Long id,Dome instance);
}
