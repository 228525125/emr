package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.DataLimit;
import com.mhsoft.emr.domain.Module;
import com.mhsoft.emr.domain.OperateLimit;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IOperateLimitService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.dao.IOperateLimitDAO;


/**
 * OperateLimitServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: OperateLimitServiceImpl.java,v 0.0.1 2011-3-11 11:14:00 EasyJWeb 1.0-m2 Exp $
 */
public class OperateLimitServiceImpl implements IOperateLimitService{
	
	private IOperateLimitDAO operateLimitDao;
	
	public void setOperateLimitDao(IOperateLimitDAO operateLimitDao){
		this.operateLimitDao=operateLimitDao;
	}
	
	public Long addOperateLimit(OperateLimit operateLimit) {
		updateLevel(operateLimit);
		this.operateLimitDao.save(operateLimit);
		if (operateLimit != null && operateLimit.getId() != null) {
			return operateLimit.getId();
		}
		return null;
	}
	
	public OperateLimit getOperateLimit(Long id) {
		OperateLimit operateLimit = this.operateLimitDao.get(id);
		return operateLimit;
		}
	
	public boolean delOperateLimit(Long id) {	
			OperateLimit operateLimit = this.getOperateLimit(id);
			if (operateLimit != null) {
				this.operateLimitDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelOperateLimits(List<Serializable> operateLimitIds) {
		
		for (Serializable id : operateLimitIds) {
			delOperateLimit((Long) id);
		}
		return true;
	}
	
	public IPageList getOperateLimitBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, OperateLimit.class,this.operateLimitDao);		
	}
	
	public boolean updateOperateLimit(Long id, OperateLimit operateLimit) {
		if (id != null) {
			operateLimit.setId(id);
		} else {
			return false;
		}
		updateLevel(operateLimit);
		this.operateLimitDao.update(operateLimit);
		return true;
	}	
		
	public String loadTree(Integer currentNodeId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql;
		if(new Integer(0).equals(currentNodeId))
			sql = "select o from OperateLimit o where o.module is null and o.disabled=0";
		else
			sql = "select o from OperateLimit o where o.module.id="+currentNodeId+" and o.disabled=0";
		List list = operateLimitDao.query(sql, null, 0, AppContext.RESULTSIZE);
		for(int i=0;i<list.size();i++){
			OperateLimit bean = (OperateLimit) list.get(i);
			Long id = bean.getId();
			result += "{id:"+id+",text:'"+bean.getName()+"("+bean.getCode()+")',";			
			result += "leaf:true}";
			if(i<list.size()-1)
				result += ",";
		}
		return result+"]";
	}
	
	private void updateLevel(OperateLimit operateLimit){				
		Module module = operateLimit.getModule();
		operateLimit.setLevel(module.getLevel()+"-"+operateLimit.getCode());		
	}
	
}
