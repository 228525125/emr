package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Weishengju;
/**
 * WeishengjuService
 * @author EasyJWeb 1.0-m2
 * $Id: WeishengjuService.java,v 0.0.1 2011-3-24 14:21:11 EasyJWeb 1.0-m2 Exp $
 */
public interface IWeishengjuService {
	/**
	 * 保存一个Weishengju，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addWeishengju(Weishengju instance);
	
	/**
	 * 根据一个ID得到Weishengju
	 * 
	 * @param id
	 * @return
	 */
	Weishengju getWeishengju(Long id);
	
	/**
	 * 删除一个Weishengju
	 * @param id
	 * @return
	 */
	boolean delWeishengju(Long id);
	
	/**
	 * 批量删除Weishengju
	 * @param ids
	 * @return
	 */
	boolean batchDelWeishengjus(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Weishengju
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getWeishengjuBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Weishengju
	  * @param id 需要更新的Weishengju的id
	  * @param dir 需要更新的Weishengju
	  */
	boolean updateWeishengju(Long id,Weishengju instance);
	
	String loadTree(Integer currentNodeId);
}
