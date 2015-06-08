package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Glossary;
/**
 * GlossaryService
 * @author EasyJWeb 1.0-m2
 * $Id: GlossaryService.java,v 0.0.1 2011-3-4 14:53:15 EasyJWeb 1.0-m2 Exp $
 */
public interface IGlossaryService {
	/**
	 * 保存一个Glossary，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addGlossary(Glossary instance);
	
	/**
	 * 根据一个ID得到Glossary
	 * 
	 * @param id
	 * @return
	 */
	Glossary getGlossary(Long id);
	
	/**
	 * 删除一个Glossary
	 * @param id
	 * @return
	 */
	boolean delGlossary(Long id);
	
	/**
	 * 批量删除Glossary
	 * @param ids
	 * @return
	 */
	boolean batchDelGlossarys(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Glossary
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getGlossaryBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Glossary
	  * @param id 需要更新的Glossary的id
	  * @param dir 需要更新的Glossary
	  */
	boolean updateGlossary(Long id,Glossary instance);
}
