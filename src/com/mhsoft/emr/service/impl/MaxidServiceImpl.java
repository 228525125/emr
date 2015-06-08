package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.dao.IMaxidDAO;
import com.mhsoft.emr.domain.Maxid;
import com.mhsoft.emr.service.IMaxidService;


/**
 * MaxidServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: MaxidServiceImpl.java,v 0.0.1 2011-7-29 12:39:52 EasyJWeb 1.0-m2 Exp $
 */
public class MaxidServiceImpl implements IMaxidService{
	
	private IMaxidDAO maxidDao;
	
	public void setMaxidDao(IMaxidDAO maxidDao){
		this.maxidDao=maxidDao;
	}
	
	public Long addMaxid(Maxid maxid) {	
		this.maxidDao.save(maxid);
		if (maxid != null && maxid.getId() != null) {
			return maxid.getId();
		}
		return null;
	}
	
	public Maxid getMaxid(Long id) {
		Maxid maxid = this.maxidDao.get(id);
		return maxid;
		}
	
	public boolean delMaxid(Long id) {	
			Maxid maxid = this.getMaxid(id);
			if (maxid != null) {
				this.maxidDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelMaxids(List<Serializable> maxidIds) {
		
		for (Serializable id : maxidIds) {
			delMaxid((Long) id);
		}
		return true;
	}
	
	public IPageList getMaxidBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Maxid.class,this.maxidDao);		
	}
	
	public boolean updateMaxid(Long id, Maxid maxid) {
		if (id != null) {
			maxid.setId(id);
		} else {
			return false;
		}
		this.maxidDao.update(maxid);
		return true;
	}	
	@Override
	public String createMaxId(Integer unit) {
		// TODO Auto-generated method stub
		List list = this.maxidDao.query("select m from Maxid m", null, 0, 1);
		Long serial = 1l;
		if(list.isEmpty()){
			Maxid maxid = new Maxid();
			maxid.setSerialNumber(1l);
			this.maxidDao.save(maxid);
		}else{
			Maxid maxid = (Maxid) list.get(0);
			serial = maxid.getSerialNumber();
			serial += 1;
			maxid.setSerialNumber(serial);
			this.maxidDao.save(maxid);
		}
		
		String ret = "";
		for(int i = 0; i<unit - serial.toString().length();i++)
			ret += "0";
		
		ret += serial;
		return ret;
	}
}
