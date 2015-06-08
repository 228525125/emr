package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.domain.Employee;
import com.mhsoft.emr.domain.EmrUser;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IEmployeeService;
import com.mhsoft.emr.dao.IDepartmentDAO;
import com.mhsoft.emr.dao.IEmployeeDAO;
import com.mhsoft.emr.dao.JDBCQueryDao;


/**
 * EmployeeServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: EmployeeServiceImpl.java,v 0.0.1 2011-3-11 11:13:39 EasyJWeb 1.0-m2 Exp $
 */
public class EmployeeServiceImpl implements IEmployeeService{
	
	private IEmployeeDAO employeeDao;
	
	private JDBCQueryDao jdbcDao;
	
	public void setEmployeeDao(IEmployeeDAO employeeDao){
		this.employeeDao=employeeDao;
	}
	
	public void setJdbcDao(JDBCQueryDao jdbcDao) {
		this.jdbcDao = jdbcDao;
	}

	public Long addEmployee(Employee employee) {	
		updateLevel(employee);
		this.employeeDao.save(employee);
		if (employee != null && employee.getId() != null) {
			return employee.getId();
		}
		return null;
	}
	
	public Employee getEmployee(Long id) {
		Employee employee = this.employeeDao.get(id);
		return employee;
		}
	
	public boolean delEmployee(Long id) {	
			Employee employee = this.getEmployee(id);
			if (employee != null) {
				this.employeeDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelEmployees(List<Serializable> employeeIds) {
		
		for (Serializable id : employeeIds) {
			delEmployee((Long) id);
		}
		return true;
	}
	
	public IPageList getEmployeeBy(IQueryObject queryObject) {
		return QueryUtil.query(queryObject, Employee.class,this.employeeDao);		
	}
	
	public boolean updateEmployee(Long id, Employee employee) {
		if (id != null) {
			employee.setId(id);
		} else {
			return false;
		}
		updateLevel(employee);
		this.employeeDao.update(employee);
		return true;
	}
	
	private void updateLevel(Employee employee){
		String level = "";
		if(null!=employee.getDepartment()){
			level += employee.getDepartment().getLevel();  
		}
		employee.setLevel(level+employee.getCode());
	}
	
	public Employee getEmployee(String code){
		List list = this.employeeDao.query("select e from Employee e where e.code=?", new Object[]{code}, 0, 1);
		if(list.isEmpty())
			return null;
		else
			return (Employee) list.get(0);
	}
	
	@Override
	public boolean isRepeat(Department department) {
		// TODO Auto-generated method stub
		List list = this.employeeDao.query("select e from Employee e where e.department = ?", new Object[]{department}, 0, 1);
		if(list.isEmpty())
			return true;
		else
			return false;
	}
}
