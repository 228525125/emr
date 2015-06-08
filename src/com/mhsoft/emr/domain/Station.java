package com.mhsoft.emr.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.easyjf.container.annonation.POLoad;

/**
 * 岗位
 * @author Administrator
 * 注意：岗位必须属于某个部门
 */
@Entity
public class Station {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false,unique=true)
	private String code;   //编码
	
	private String name;   //名称
	
	private String auxCode;   //简码
	
	private Boolean disabled = false; //禁用

	private String description;       //描述
	
	private String level;      
	
	@POLoad(name="department")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="departmentId",nullable=false)
	private Department department;
	
	@POLoad(name="parent")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="parentId",nullable=true)
	private Station parent;//上级角色
	
	@ManyToMany (cascade = CascadeType.MERGE) 
	@JoinTable (name =  "StationASOperateLimit" , //关联表名 
			inverseJoinColumns =  @JoinColumn (name =  "operateLimitId" ),//被维护端外键 
            joinColumns =  @JoinColumn (name =  "stationId" ))//维护端外键 
	private Set<OperateLimit>  operateLimitList = new HashSet<OperateLimit>();

	@ManyToMany (cascade = CascadeType.MERGE) 
	@JoinTable (name =  "StationASDataLimit" , //关联表名 
			inverseJoinColumns =  @JoinColumn (name =  "dataLimitId" ),//被维护端外键 
            joinColumns =  @JoinColumn (name =  "stationId" ))//维护端外键 
	private Set<DataLimit>  dataLimitList = new HashSet<DataLimit>();
	
	@ManyToMany (cascade = CascadeType.MERGE) 
	@JoinTable (name =  "StationASEmployee" , //关联表名 
			inverseJoinColumns =  @JoinColumn (name =  "employeeId" ),//被维护端外键 
            joinColumns =  @JoinColumn (name =  "stationId" ))//维护端外键 
	private Set<Employee> employeeList = new HashSet<Employee>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuxCode() {
		return auxCode;
	}

	public void setAuxCode(String auxCode) {
		this.auxCode = auxCode;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<OperateLimit> getOperateLimitList() {
		return operateLimitList;
	}

	public void setOperateLimitList(Set<OperateLimit> operateLimitList) {
		this.operateLimitList = operateLimitList;
	}

	public Set<DataLimit> getDataLimitList() {
		return dataLimitList;
	}

	public void setDataLimitList(Set<DataLimit> dataLimitList) {
		this.dataLimitList = dataLimitList;
	}

	public Set<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(Set<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Station getParent() {
		return parent;
	}

	public void setParent(Station parent) {
		this.parent = parent;
	}
	
}
