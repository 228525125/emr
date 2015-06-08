package com.mhsoft.emr.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 基本词汇
 * @author chenxian
 *
 */
@Entity
public class Glossary {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;   //内码
	
	private Integer type;//基本信息类型
	
	@Column(length=30)
	private String code; //显示代码
	
	@Column(length=200)
	private String name; //名称
	
	@Column(length=30)
	private String auxCode; //拼音
	
	private Boolean disabled = false; //禁用
	
	private Boolean readOnly = false; //只读
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	public void setSwid(String code) {
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public int hashCode() {
		return id.intValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Glossary) {
			Glossary glos = (Glossary) obj;
			return glos.getId().equals(id);
		}
		return super.equals(obj);
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
