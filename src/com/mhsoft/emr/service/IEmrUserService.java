package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.EmrUser;
/**
 * EmrUserService
 * @author EasyJWeb 1.0-m2
 * $Id: EmrUserService.java,v 0.0.1 2011-3-5 14:18:17 EasyJWeb 1.0-m2 Exp $
 */
public interface IEmrUserService {
	/**
	 * 保存一个EmrUser，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addEmrUser(EmrUser instance);
	
	/**
	 * 根据一个ID得到EmrUser
	 * 
	 * @param id
	 * @return
	 */
	EmrUser getEmrUser(Long id);
	
	/**
	 * 删除一个EmrUser
	 * @param id
	 * @return
	 */
	boolean delEmrUser(Long id);
	
	/**
	 * 批量删除EmrUser
	 * @param ids
	 * @return
	 */
	boolean batchDelEmrUsers(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到EmrUser
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getEmrUserBy(IQueryObject queryObject);
	
	/**
	  * 更新一个EmrUser
	  * @param id 需要更新的EmrUser的id
	  * @param dir 需要更新的EmrUser
	  */
	boolean updateEmrUser(Long id,EmrUser instance);
	
	EmrUser getEmrUser(String account);
}
