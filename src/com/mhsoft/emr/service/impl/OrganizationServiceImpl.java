package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.service.IOrganizationService;
import com.mhsoft.emr.util.AppContext;
import com.mhsoft.emr.dao.IOrganizationDAO;


/**
 * OrganizationServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: OrganizationServiceImpl.java,v 0.0.1 2011-3-11 11:14:10 EasyJWeb 1.0-m2 Exp $
 */
public class OrganizationServiceImpl implements IOrganizationService{
	
	private IOrganizationDAO organizationDao;
	
	public void setOrganizationDao(IOrganizationDAO organizationDao){
		this.organizationDao=organizationDao;
	}
	
	public Long addOrganization(Organization organization) {
		updateLevel(organization);
		this.organizationDao.save(organization);
		if (organization != null && organization.getId() != null) {
			return organization.getId();
		}
		return null;
	}
	
	public Organization getOrganization(Long id) {
		Organization organization = this.organizationDao.get(id);
		return organization;
		}
	
	public boolean delOrganization(Long id) {	
			Organization organization = this.getOrganization(id);
			if (organization != null) {
				this.organizationDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelOrganizations(List<Serializable> organizationIds) {		
		for (Serializable id : organizationIds) {
			delOrganization((Long) id);
		}
		return true;
	}
	
	public IPageList getOrganizationBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Organization.class,this.organizationDao);		
	}
	
	public boolean updateOrganization(Long id, Organization organization) {
		if (id != null) {
			organization.setId(id);
		} else {
			return false;
		}
		updateLevel(organization);
		this.organizationDao.update(organization);
		return true;
	}	
	
	@Override
	public String loadTree(Integer currentNodeId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql;
		if(new Integer(0).equals(currentNodeId))
			sql = "select o from Organization o where o.parent is null and o.disabled=0";
		else
			sql = "select o from Organization o where o.parent.id="+currentNodeId+" and o.disabled=0";
		List list = organizationDao.query(sql, null, 0, AppContext.RESULTSIZE);
		for(int i=0;i<list.size();i++){
			Organization org = (Organization) list.get(i);
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
		String sql = "select o from Organization o where o.parent.id="+nodeId+" and o.disabled=false";
		List list = organizationDao.query(sql, null, 0, 1);
		return list.isEmpty();
	}
	
	private void updateLevel(Organization organization){
		Organization parent = organization.getParent();
		if(null!=parent){
			organization.setLevel(parent.getLevel()+"-"+organization.getCode());
			organization.setGrade(parent.getGrade()+1);
		}else{
			organization.setLevel(organization.getCode());
			organization.setGrade(1);
		}
	}
	
}
