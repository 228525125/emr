package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Organization;
/**
 * OrganizationService
 * @author EasyJWeb 1.0-m2
 * $Id: OrganizationService.java,v 0.0.1 2011-3-11 11:14:10 EasyJWeb 1.0-m2 Exp $
 */
public interface IOrganizationService {
	/**
	 * 保存一个Organization，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addOrganization(Organization instance);
	
	/**
	 * 根据一个ID得到Organization
	 * 
	 * @param id
	 * @return
	 */
	Organization getOrganization(Long id);
	
	/**
	 * 删除一个Organization
	 * @param id
	 * @return
	 */
	boolean delOrganization(Long id);
	
	/**
	 * 批量删除Organization
	 * @param ids
	 * @return
	 */
	boolean batchDelOrganizations(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Organization
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getOrganizationBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Organization
	  * @param id 需要更新的Organization的id
	  * @param dir 需要更新的Organization
	  */
	boolean updateOrganization(Long id,Organization instance);
	
	String loadTree(Integer currentNodeId);
}
