package com.mhsoft.emr.service;

import java.util.List;

public interface IPermissionService {

	public <T> T get(Class<T> entityClass, Object id);
	
	public void persist(Object entity);
	
	public void merge(Object entity);
	
	public void remove(Object entity);
	
	public List find(String queryString);
	
}
