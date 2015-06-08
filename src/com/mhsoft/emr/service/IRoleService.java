package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Role;
/**
 * RoleService
 * @author EasyJWeb 1.0-m2
 * $Id: RoleService.java,v 0.0.1 2011-3-11 11:14:16 EasyJWeb 1.0-m2 Exp $
 */
public interface IRoleService {
	/**
	 * 保存一个Role，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addRole(Role instance);
	
	/**
	 * 根据一个ID得到Role
	 * 
	 * @param id
	 * @return
	 */
	Role getRole(Long id);
	
	/**
	 * 删除一个Role
	 * @param id
	 * @return
	 */
	boolean delRole(Long id);
	
	/**
	 * 批量删除Role
	 * @param ids
	 * @return
	 */
	boolean batchDelRoles(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Role
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getRoleBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Role
	  * @param id 需要更新的Role的id
	  * @param dir 需要更新的Role
	  */
	boolean updateRole(Long id,Role instance);
	
	String loadTree(Integer currentNodeId);
	
	/**
	 * 根据code获取一个role
	 * @param code
	 * @return
	 */
	Role getRole(String code);
}
