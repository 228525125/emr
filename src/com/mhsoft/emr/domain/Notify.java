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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.easyjf.container.annonation.POLoad;

@Entity
public class Notify {

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
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;                 //创建时间
	
	private Boolean empty = true;     //是否已上传文件
	
	private Boolean extraEmpty = true;    //是否已上传附件
	
	private String selected;          //选择的版本的文件名称     
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getEmpty() {
		return empty;
	}

	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}

	public Boolean getExtraEmpty() {
		return extraEmpty;
	}

	public void setExtraEmpty(Boolean extraEmpty) {
		this.extraEmpty = extraEmpty;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
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
