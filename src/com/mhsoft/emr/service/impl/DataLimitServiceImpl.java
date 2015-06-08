package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.DataLimit;
import com.mhsoft.emr.domain.Module;
import com.mhsoft.emr.service.IDataLimitService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.dao.IDataLimitDAO;


/**
 * DataLimitServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: DataLimitServiceImpl.java,v 0.0.1 2011-3-11 11:13:18 EasyJWeb 1.0-m2 Exp $
 */
public class DataLimitServiceImpl implements IDataLimitService{
	
	private IDataLimitDAO dataLimitDao;
	
	public void setDataLimitDao(IDataLimitDAO dataLimitDao){
		this.dataLimitDao=dataLimitDao;
	}
	
	public Long addDataLimit(DataLimit dataLimit) {	
		updateLevel(dataLimit);
		this.dataLimitDao.save(dataLimit);
		if (dataLimit != null && dataLimit.getId() != null) {
			return dataLimit.getId();
		}
		return null;
	}
	
	public DataLimit getDataLimit(Long id) {
		DataLimit dataLimit = this.dataLimitDao.get(id);
		return dataLimit;
		}
	
	public boolean delDataLimit(Long id) {	
			DataLimit dataLimit = this.getDataLimit(id);
			if (dataLimit != null) {
				this.dataLimitDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelDataLimits(List<Serializable> dataLimitIds) {
		
		for (Serializable id : dataLimitIds) {
			delDataLimit((Long) id);
		}
		return true;
	}
	
	public IPageList getDataLimitBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, DataLimit.class,this.dataLimitDao);		
	}
	
	public boolean updateDataLimit(Long id, DataLimit dataLimit) {
		if (id != null) {
			dataLimit.setId(id);
		} else {
			return false;
		}
		updateLevel(dataLimit);
		this.dataLimitDao.update(dataLimit);
		return true;
	}	
	
	@Override
	public String loadTree(Integer currentNodeId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql;
		if(new Integer(0).equals(currentNodeId))
			sql = "select o from DataLimit o where o.module is null and o.disabled=0";
		else
			sql = "select o from DataLimit o where o.module.id="+currentNodeId+" and o.disabled=0";
		List list = dataLimitDao.query(sql, null, 0, AppContext.RESULTSIZE);
		for(int i=0;i<list.size();i++){
			DataLimit bean = (DataLimit) list.get(i);
			Long id = bean.getId();
			result += "{id:"+id+",text:'"+bean.getName()+"("+bean.getCode()+")',";			
			result += "leaf:true}";
			if(i<list.size()-1)
				result += ",";
		}
		return result+"]";
	}
	
	private void updateLevel(DataLimit dataLimit){				
		Module module = dataLimit.getModule();
		dataLimit.setLevel(module.getLevel()+"-"+dataLimit.getCode());		
	}
	
}
