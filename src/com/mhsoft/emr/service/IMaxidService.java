package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Maxid;
/**
 * MaxidService
 * @author EasyJWeb 1.0-m2
 * $Id: MaxidService.java,v 0.0.1 2011-7-29 12:39:52 EasyJWeb 1.0-m2 Exp $
 */
public interface IMaxidService {
	/**
	 * 保存一个Maxid，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addMaxid(Maxid instance);
	
	/**
	 * 根据一个ID得到Maxid
	 * 
	 * @param id
	 * @return
	 */
	Maxid getMaxid(Long id);
	
	/**
	 * 删除一个Maxid
	 * @param id
	 * @return
	 */
	boolean delMaxid(Long id);
	
	/**
	 * 批量删除Maxid
	 * @param ids
	 * @return
	 */
	boolean batchDelMaxids(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Maxid
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getMaxidBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Maxid
	  * @param id 需要更新的Maxid的id
	  * @param dir 需要更新的Maxid
	  */
	boolean updateMaxid(Long id,Maxid instance);
	
	/**
	 * 创建一个几位数的id，使用0补全
	 * @param unit
	 * @return
	 */
	public String createMaxId(Integer unit);
}
