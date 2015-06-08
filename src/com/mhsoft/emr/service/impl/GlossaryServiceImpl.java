package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Glossary;
import com.mhsoft.emr.service.IGlossaryService;
import com.mhsoft.emr.dao.IGlossaryDAO;


/**
 * GlossaryServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: GlossaryServiceImpl.java,v 0.0.1 2011-3-4 14:53:15 EasyJWeb 1.0-m2 Exp $
 */
public class GlossaryServiceImpl implements IGlossaryService{
	
	private IGlossaryDAO glossaryDao;
	
	public void setGlossaryDao(IGlossaryDAO glossaryDao){
		this.glossaryDao=glossaryDao;
	}
	
	public Long addGlossary(Glossary glossary) {	
		this.glossaryDao.save(glossary);
		if (glossary != null && glossary.getId() != null) {
			return glossary.getId();
		}
		return null;
	}
	
	public Glossary getGlossary(Long id) {
		Glossary glossary = this.glossaryDao.get(id);
		return glossary;
		}
	
	public boolean delGlossary(Long id) {	
			Glossary glossary = this.getGlossary(id);
			if (glossary != null) {
				this.glossaryDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelGlossarys(List<Serializable> glossaryIds) {
		
		for (Serializable id : glossaryIds) {
			delGlossary((Long) id);
		}
		return true;
	}
	
	public IPageList getGlossaryBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Glossary.class,this.glossaryDao);		
	}
	
	public boolean updateGlossary(Long id, Glossary glossary) {
		if (id != null) {
			glossary.setId(id);
		} else {
			return false;
		}
		this.glossaryDao.update(glossary);
		return true;
	}	
	
}
