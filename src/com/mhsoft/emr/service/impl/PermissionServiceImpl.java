package com.mhsoft.emr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.mhsoft.emr.dao.IPermissionDAO;
import com.mhsoft.emr.service.IPermissionService;

public class PermissionServiceImpl implements IPermissionService {

	@Autowired
	private IPermissionDAO permissionDao;
	
	@Override
	public List find(String queryString) {
		// TODO Auto-generated method stub
		return permissionDao.find(queryString);
	}

	@Override
	public <T> T get(Class<T> entityClass, Object id) {
		// TODO Auto-generated method stub
		return permissionDao.get(entityClass, id);
	}

	@Override
	public void merge(Object entity) {
		// TODO Auto-generated method stub
		permissionDao.merge(entity);
	}

	@Override
	public void persist(Object entity) {
		// TODO Auto-generated method stub
		permissionDao.persist(entity);
	}

	@Override
	public void remove(Object entity) {
		// TODO Auto-generated method stub
		permissionDao.remove(entity);
	}

}
