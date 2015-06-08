package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.OperateLimit;
/**
 * OperateLimitService
 * @author EasyJWeb 1.0-m2
 * $Id: OperateLimitService.java,v 0.0.1 2011-3-11 11:14:00 EasyJWeb 1.0-m2 Exp $
 */
public interface IOperateLimitService {
	/**
	 * 保存一个OperateLimit，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addOperateLimit(OperateLimit instance);
	
	/**
	 * 根据一个ID得到OperateLimit
	 * 
	 * @param id
	 * @return
	 */
	OperateLimit getOperateLimit(Long id);
	
	/**
	 * 删除一个OperateLimit
	 * @param id
	 * @return
	 */
	boolean delOperateLimit(Long id);
	
	/**
	 * 批量删除OperateLimit
	 * @param ids
	 * @return
	 */
	boolean batchDelOperateLimits(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到OperateLimit
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getOperateLimitBy(IQueryObject queryObject);
	
	/**
	  * 更新一个OperateLimit
	  * @param id 需要更新的OperateLimit的id
	  * @param dir 需要更新的OperateLimit
	  */
	boolean updateOperateLimit(Long id,OperateLimit instance);
	
	String loadTree(Integer currentNodeId);
}
