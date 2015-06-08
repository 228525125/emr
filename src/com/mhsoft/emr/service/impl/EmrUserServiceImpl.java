package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.EmrUser;
import com.mhsoft.emr.service.IEmrUserService;
import com.mhsoft.emr.dao.IEmrUserDAO;


/**
 * EmrUserServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: EmrUserServiceImpl.java,v 0.0.1 2011-3-5 14:18:17 EasyJWeb 1.0-m2 Exp $
 */
public class EmrUserServiceImpl implements IEmrUserService{
	
	private IEmrUserDAO emrUserDao;
	
	public void setEmrUserDao(IEmrUserDAO emrUserDao){
		this.emrUserDao=emrUserDao;
	}
	
	public Long addEmrUser(EmrUser emrUser) {	
		this.emrUserDao.save(emrUser);
		if (emrUser != null && emrUser.getId() != null) {
			return emrUser.getId();
		}
		return null;
	}
	
	public EmrUser getEmrUser(Long id) {
		EmrUser emrUser = this.emrUserDao.get(id);
		return emrUser;
		}
	
	public boolean delEmrUser(Long id) {	
			EmrUser emrUser = this.getEmrUser(id);
			if (emrUser != null) {
				this.emrUserDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelEmrUsers(List<Serializable> emrUserIds) {
		
		for (Serializable id : emrUserIds) {
			delEmrUser((Long) id);
		}
		return true;
	}
	
	public IPageList getEmrUserBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, EmrUser.class,this.emrUserDao);		
	}
	
	public boolean updateEmrUser(Long id, EmrUser emrUser) {
		if (id != null) {
			emrUser.setId(id);
		} else {
			return false;
		}
		this.emrUserDao.update(emrUser);
		return true;
	}	
	
	public EmrUser getEmrUser(String account) {
		// TODO Auto-generated method stub
		List list = this.emrUserDao.query("select u from EmrUser u where u.account=?", new Object[]{account}, 0, 1);
		if(list.isEmpty())
			return null;
		else
			return (EmrUser) list.get(0);
	}
	
}
