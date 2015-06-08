package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.dao.IDomeDAO;
import com.mhsoft.emr.domain.Dome;
import com.mhsoft.emr.service.IDomeService;


/**
 * DomeServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: DomeServiceImpl.java,v 0.0.1 2011-3-1 10:20:29 EasyJWeb 1.0-m2 Exp $
 */
public class DomeServiceImpl implements IDomeService{
	
	private IDomeDAO domeDao;
	
	public void setDomeDao(IDomeDAO domeDao){
		this.domeDao=domeDao;
	}
	
	public Long addDome(Dome dome) {	
		this.domeDao.save(dome);
		if (dome != null && dome.getId() != null) {
			return dome.getId();
		}
		return null;
	}
	
	public Dome getDome(Long id) {
		Dome dome = this.domeDao.get(id);
		return dome;
		}
	
	public boolean delDome(Long id) {	
			Dome dome = this.getDome(id);
			if (dome != null) {
				this.domeDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelDomes(List<Serializable> domeIds) {
		
		for (Serializable id : domeIds) {
			delDome((Long) id);
		}
		return true;
	}
	
	public IPageList getDomeBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Dome.class,this.domeDao);		
	}
	
	public boolean updateDome(Long id, Dome dome) {
		if (id != null) {
			dome.setId(id);
		} else {
			return false;
		}
		this.domeDao.update(dome);
		return true;
	}	
	
}
