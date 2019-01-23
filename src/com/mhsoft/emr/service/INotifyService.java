package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Notify;

public interface INotifyService {

	/**
	 * 保存一个Notify，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addNotify(Notify instance);
	
	/**
	 * 根据一个ID得到Notify
	 * 
	 * @param id
	 * @return
	 */
	Notify getNotify(Long id);
	
	/**
	 * 删除一个Notify
	 * @param id
	 * @return
	 */
	boolean delNotify(Long id);
	
	/**
	 * 批量删除Notify
	 * @param ids
	 * @return
	 */
	boolean batchDelNotify(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Notify
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getNotifyBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Notify
	  * @param id 需要更新的Notify的id
	  * @param dir 需要更新的Notify
	  */
	boolean updateNotify(Long id,Notify instance);
}
