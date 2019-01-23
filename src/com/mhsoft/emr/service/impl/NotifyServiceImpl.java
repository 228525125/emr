package com.mhsoft.emr.service.impl;

import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.dao.INotifyDAO;
import com.mhsoft.emr.domain.Notify;
import com.mhsoft.emr.service.INotifyService;

public class NotifyServiceImpl implements INotifyService{

private INotifyDAO notifyDao;
	
	public void setNotifyDao(INotifyDAO notifyDao){
		this.notifyDao=notifyDao;
	}
	
	public Long addNotify(Notify notify) {	
		this.notifyDao.save(notify);
		if (notify != null && notify.getId() != null) {
			return notify.getId();
		}
		return null;
	}
	
	public Notify getNotify(Long id) {
		Notify notify = this.notifyDao.get(id);
		return notify;
		}
	
	public boolean delNotify(Long id) {	
			Notify notify = this.getNotify(id);
			if (notify != null) {
				this.notifyDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public IPageList getNotifyBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Notify.class,this.notifyDao);		
	}
	
	public boolean updateNotify(Long id, Notify notify) {
		if (id != null) {
			notify.setId(id);
		} else {
			return false;
		}
		this.notifyDao.update(notify);
		return true;
	}

	@Override
	public boolean batchDelNotify(List<Serializable> ids) {
		// TODO Auto-generated method stub
		for (Serializable id : ids) {
			delNotify((Long) id);
		}
		return true;
	}

}
