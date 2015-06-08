package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.FileClass;

public interface IFileClassService {

	/**
	 * 保存一个FileClass，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addFileClass(FileClass instance);
	
	/**
	 * 根据一个ID得到FileClass
	 * 
	 * @param id
	 * @return
	 */
	FileClass getFileClass(Long id);
	
	/**
	 * 删除一个FileClass
	 * @param id
	 * @return
	 */
	boolean delFileClass(Long id);
	
	/**
	 * 批量删除FileClass
	 * @param ids
	 * @return
	 */
	boolean batchDelFileClasss(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到FileClass
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getFileClassBy(IQueryObject queryObject);
	
	/**
	  * 更新一个FileClass
	  * @param id 需要更新的FileClass的id
	  * @param dir 需要更新的FileClass
	  */
	boolean updateFileClass(Long id,FileClass instance);
	
	String loadTree(Integer currentNodeId);
}
