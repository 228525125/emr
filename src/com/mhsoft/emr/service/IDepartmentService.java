package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Department;
/**
 * DepartmentService
 * @author EasyJWeb 1.0-m2
 * $Id: DepartmentService.java,v 0.0.1 2011-3-11 11:13:28 EasyJWeb 1.0-m2 Exp $
 */
public interface IDepartmentService {
	/**
	 * 保存一个Department，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addDepartment(Department instance);
	
	/**
	 * 根据一个ID得到Department
	 * 
	 * @param id
	 * @return
	 */
	Department getDepartment(Long id);
	
	/**
	 * 删除一个Department
	 * @param id
	 * @return
	 */
	boolean delDepartment(Long id);
	
	/**
	 * 批量删除Department
	 * @param ids
	 * @return
	 */
	boolean batchDelDepartments(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Department
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getDepartmentBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Department
	  * @param id 需要更新的Department的id
	  * @param dir 需要更新的Department
	  */
	boolean updateDepartment(Long id,Department instance);
	
	String loadTree(Integer organizationId, Integer departmentId);
	
	/**
	 * 根据图号查询物料代码
	 * @param tuhao
	 * @return
	 */
	String queryForTuhao(String tuhao);
	
	Department getDepartment(String tuhao);
}
