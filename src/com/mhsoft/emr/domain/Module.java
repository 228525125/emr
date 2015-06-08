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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.easyjf.container.annonation.POLoad;

/**
 * 功能模块
 * @author Administrator
 *
 */
@Entity
public class Module {

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false,unique=true)
	private String code;   //编码
	
	private String name;   //名称
	
	private String auxCode;   //简码
	
	private Boolean disabled = false; //禁用
	
	private String description;       //描述
	
	private String level;        //层次关系描述
	
	@POLoad(name="parent")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="parentId",nullable=true)
	private Module parent;//上级
	
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,mappedBy="module")
	private Set<DataLimit>  dataLimitList = new HashSet<DataLimit>();		
	
	@OneToMany (cascade = CascadeType.MERGE,fetch=FetchType.EAGER,mappedBy="module") 
	private Set<OperateLimit>  operateLimitList = new HashSet<OperateLimit>();

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<DataLimit> getDataLimitList() {
		return dataLimitList;
	}

	public void setDataLimitList(Set<DataLimit> dataLimitList) {
		this.dataLimitList = dataLimitList;
	}

	public Set<OperateLimit> getOperateLimitList() {
		return operateLimitList;
	}

	public void setOperateLimitList(Set<OperateLimit> operateLimitList) {
		this.operateLimitList = operateLimitList;
	}

	public Module getParent() {
		return parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
}
