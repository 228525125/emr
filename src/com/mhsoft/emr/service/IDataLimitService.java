package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.DataLimit;
/**
 * DataLimitService
 * @author EasyJWeb 1.0-m2
 * $Id: DataLimitService.java,v 0.0.1 2011-3-11 11:13:17 EasyJWeb 1.0-m2 Exp $
 */
public interface IDataLimitService {
	/**
	 * 保存一个DataLimit，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addDataLimit(DataLimit instance);
	
	/**
	 * 根据一个ID得到DataLimit
	 * 
	 * @param id
	 * @return
	 */
	DataLimit getDataLimit(Long id);
	
	/**
	 * 删除一个DataLimit
	 * @param id
	 * @return
	 */
	boolean delDataLimit(Long id);
	
	/**
	 * 批量删除DataLimit
	 * @param ids
	 * @return
	 */
	boolean batchDelDataLimits(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到DataLimit
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getDataLimitBy(IQueryObject queryObject);
	
	/**
	  * 更新一个DataLimit
	  * @param id 需要更新的DataLimit的id
	  * @param dir 需要更新的DataLimit
	  */
	boolean updateDataLimit(Long id,DataLimit instance);
	
	String loadTree(Integer currentNodeId);
}
