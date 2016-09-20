package com.mhsoft.emr.domain;

import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.easyjf.container.annonation.POLoad;

/**
 * 文件
 * @author chenxian
 *
 */
@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false,unique=true)
	private String code;   //编码
	
	private String name;   //名称
	
	private String auxCode;   //简码
	
	private Boolean disabled = false; //禁用
	
	private String description;       //描述
	
	private String address;  //路径
	
	private String level;
	
	@POLoad(name="department")
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="departmentId",nullable=false)
	private Department department;        //所属机构
	
	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "employeeList", fetch = FetchType.LAZY)
	private Set<Organization> organizationList = new HashSet<Organization>();
	
	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "employeeList", fetch = FetchType.LAZY)
	private Set<Role>  roleList = new HashSet<Role>();       //工位
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;                 //创建时间
	
	@POLoad(name="fileClass")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="fileClassId",nullable=true)
	private FileClass fileClass;        //文件类型
	
	private Boolean empty = true;     //是否已上传文件
	
	private Boolean extraEmpty = true;    //是否已上传附件
	
	private String version = "A";           //版本
	
	private String selected;          //选择的版本的文件名称
	
	private String cite;               //引用的文件编码         
	
	private Boolean checked = false; //审核
	
	private String checker;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date checkDate;                 //审核时间
	
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

	public Set<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(Set<Role> roleList) {
		this.roleList = roleList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Organization> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(Set<Organization> organizationList) {
		this.organizationList = organizationList;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public FileClass getFileClass() {
		return fileClass;
	}

	public void setFileClass(FileClass fileClass) {
		this.fileClass = fileClass;
	}

	public Boolean getEmpty() {
		return empty;
	}

	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public Boolean getExtraEmpty() {
		return extraEmpty;
	}

	public void setExtraEmpty(Boolean extraEmpty) {
		this.extraEmpty = extraEmpty;
	}

	public String getCite() {
		return cite;
	}

	public void setCite(String cite) {
		this.cite = cite;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
}
