package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.domain.Weishengju;
import com.mhsoft.emr.service.IWeishengjuService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.dao.IWeishengjuDAO;


/**
 * WeishengjuServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: WeishengjuServiceImpl.java,v 0.0.1 2011-3-24 14:21:11 EasyJWeb 1.0-m2 Exp $
 */
public class WeishengjuServiceImpl implements IWeishengjuService{
	
	private IWeishengjuDAO weishengjuDao;
	
	public void setWeishengjuDao(IWeishengjuDAO weishengjuDao){
		this.weishengjuDao=weishengjuDao;
	}
	
	public Long addWeishengju(Weishengju weishengju) {	
		this.weishengjuDao.save(weishengju);
		if (weishengju != null && weishengju.getId() != null) {
			return weishengju.getId();
		}
		return null;
	}
	
	public Weishengju getWeishengju(Long id) {
		Weishengju weishengju = this.weishengjuDao.get(id);
		return weishengju;
		}
	
	public boolean delWeishengju(Long id) {	
			Weishengju weishengju = this.getWeishengju(id);
			if (weishengju != null) {
				this.weishengjuDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelWeishengjus(List<Serializable> weishengjuIds) {
		
		for (Serializable id : weishengjuIds) {
			delWeishengju((Long) id);
		}
		return true;
	}
	
	public IPageList getWeishengjuBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Weishengju.class,this.weishengjuDao);		
	}
	
	public boolean updateWeishengju(Long id, Weishengju weishengju) {
		if (id != null) {
			weishengju.setId(id);
		} else {
			return false;
		}
		this.weishengjuDao.update(weishengju);
		return true;
	}	
	
	@Override
	public String loadTree(Integer currentNodeId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql;
		if(new Integer(0).equals(currentNodeId))
			sql = "select o from Weishengju o where o.parent is null and o.disabled=0";
		else
			sql = "select o from Weishengju o where o.parent.id="+currentNodeId+" and o.disabled=0";
		List list = weishengjuDao.query(sql, null, 0, AppContext.RESULTSIZE);
		for(int i=0;i<list.size();i++){
			Weishengju org = (Weishengju) list.get(i);
			Long id = org.getId();
			result += "{id:"+id+",text:'"+org.getName()+"("+org.getCode()+")',";
			if(isLeaf(id))
				result += "leaf:true}";
			else
				result += "leaf:false}";
			if(i<list.size()-1)
				result += ",";
		}
		return result+"]";
	}
		
	private boolean isLeaf(Long nodeId){
		String sql = "select o from Weishengju o where o.parent.id="+nodeId+" and o.disabled=false";
		List list = weishengjuDao.query(sql, null, 0, 1);
		return list.isEmpty();
	}
	
}
