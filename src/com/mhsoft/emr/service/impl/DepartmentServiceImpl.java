package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IDepartmentService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.dao.IDepartmentDAO;


/**
 * DepartmentServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: DepartmentServiceImpl.java,v 0.0.1 2011-3-11 11:13:28 EasyJWeb 1.0-m2 Exp $
 */
public class DepartmentServiceImpl implements IDepartmentService{
	
	private IDepartmentDAO departmentDao;
	
	public void setDepartmentDao(IDepartmentDAO departmentDao){
		this.departmentDao=departmentDao;
	}
	
	public Long addDepartment(Department department) {	
		updateLevel(department);
		this.departmentDao.save(department);
		if (department != null && department.getId() != null) {
			return department.getId();
		}
		return null;
	}
	
	public Department getDepartment(Long id) {
		Department department = this.departmentDao.get(id);
		return department;
		}
	
	public boolean delDepartment(Long id) {	
			Department department = this.getDepartment(id);
			if (department != null) {
				this.departmentDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelDepartments(List<Serializable> departmentIds) {
		
		for (Serializable id : departmentIds) {
			delDepartment((Long) id);
		}
		return true;
	}
	
	public IPageList getDepartmentBy(IQueryObject queryObject) {		
		return QueryUtil.query(queryObject, Department.class,this.departmentDao);		
	}
	
	public boolean updateDepartment(Long id, Department department) {
		if (id != null) {
			department.setId(id);
		} else {
			return false;
		}
		updateLevel(department);
		this.departmentDao.update(department);
		return true;
	}	
	
	@Override
	public String loadTree(Integer organizationId, Integer departmentId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql = "select o from Department o where o.disabled=0 ";
		String param1 = " and o.organization is null";
		String param2 = " and o.parent is null";
		if(!new Integer(0).equals(organizationId))
			param1 = " and o.organization.id="+organizationId;
		if(!new Integer(0).equals(departmentId))
			param2 = " and o.parent.id="+departmentId;
		sql += param1;
		sql += param2;
		String orderBy = " order by o.code";
		sql += orderBy;
		List list = departmentDao.query(sql, null, 0, 4000);
		for(int i=0;i<list.size();i++){
			Department bean = (Department) list.get(i);
			String id = "'"+bean.getOrganization().getId().toString()+","+bean.getId().toString()+"'";
			result += "{id:"+id+",text:'"+"("+bean.getCode()+")"+bean.getName()+"',";
			if(isLeaf(bean.getId()))
				result += "leaf:true}";
			else
				result += "leaf:false}";
			if(i<list.size()-1)
				result += ",";
		}
		return result+"]";
	}
	
	private boolean isLeaf(Long nodeId){
		String sql = "select o from Department o where o.parent.id="+nodeId+" and o.disabled=false";
		List list = departmentDao.query(sql, null, 0, 1);
		return list.isEmpty();
	}
	
	private void updateLevel(Department department){
		Department parent = department.getParent();
		if(null!=parent){
			department.setLevel(parent.getLevel()+"-"+department.getCode());
			department.setGrade(parent.getGrade()+1);
		}else{
			Organization organization = department.getOrganization();
			organization.setLevel(organization.getLevel()+"-"+department.getCode());
			
			department.setGrade(1);
		}
	}
	
	@Override
	public String queryForTuhao(String tuhao) {
		// TODO Auto-generated method stub
		String sql = "select d.code from Department d where d.tuhao='"+tuhao+"'";
		List list = departmentDao.query(sql, null, 0, 1);
		if(list.isEmpty())
			return null;
		else
			return list.get(0).toString();
	}
	
	@Override
	public Department getDepartment(String tuhao) {
		// TODO Auto-generated method stub
		String sql = "select d from Department d where d.tuhao='"+tuhao+"'";
		List list = departmentDao.query(sql, null, 0, 1);
		if(list.isEmpty())
			return null;
		else
			return (Department) list.get(0);
	}
}
