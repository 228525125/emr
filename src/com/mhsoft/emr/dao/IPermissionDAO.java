package com.mhsoft.emr.dao;

import java.io.Serializable;
import java.util.List;

public interface IPermissionDAO {

	public <T> T get(Class<T> entityClass, Object id);
	
	public void persist(Object entity);
	
	public void merge(Object entity);
	
	public void remove(Object entity);
	
	public List find(String queryString);
}
