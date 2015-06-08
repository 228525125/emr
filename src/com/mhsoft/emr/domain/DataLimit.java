package com.mhsoft.emr.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.easyjf.container.annonation.POLoad;

/**
 * 数据权限
 * @author Administrator
 *
 */
@Entity
public class DataLimit {

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
	
	@POLoad(name="module")
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.ALL)
	@JoinColumn(name="moduleId",nullable=true)
	private Module module;
	
	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "dataLimitList", fetch = FetchType.LAZY)
	private Set<Station> stationList = new HashSet<Station>();                           //岗位是可选
	
	@Enumerated(EnumType.STRING)
	private DataLimitType limitType;

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

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
	
	public DataLimitType getLimitType() {
		return limitType;
	}

	public void setLimitType(DataLimitType limitType) {
		this.limitType = limitType;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public Set<Station> getStationList() {
		return stationList;
	}

	public void setStationList(Set<Station> stationList) {
		this.stationList = stationList;
	}

	public enum DataLimitType {
		VISIBLE, READONLY, WRITE, MANAGE
	}


}
