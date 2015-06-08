package com.mhsoft.emr.dao.impl;

import java.util.List;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import com.mhsoft.emr.dao.IPermissionDAO;

public class PermissionDAO extends JpaDaoSupport implements IPermissionDAO {
	
	@Override
	public <T> T get(Class<T> entityClass, Object id) {
		// TODO Auto-generated method stub
		return getJpaTemplate().find(entityClass, id);
	}
	
	@Override
	public void persist(Object entity) {
		// TODO Auto-generated method stub
		getJpaTemplate().persist(entity);
	}
	
	@Override
	public void merge(Object entity) {
		// TODO Auto-generated method stub
		getJpaTemplate().merge(entity);
	}
	
	@Override
	public void remove(Object entity) {
		// TODO Auto-generated method stub
		getJpaTemplate().remove(entity);
	}

	@Override
	public List find(String queryString) {
		// TODO Auto-generated method stub
		return getJpaTemplate().find(queryString);
	}
}
