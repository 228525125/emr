package com.mhsoft.emr.service.impl;

import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.dao.IFileClassDAO;
import com.mhsoft.emr.domain.FileClass;
import com.mhsoft.emr.service.IFileClassService;
import com.mhsoft.emr.util.AppContext;

public class FileClassServiceImpl implements IFileClassService {

private IFileClassDAO fileClassDao;
	
	public void setFileClassDao(IFileClassDAO fileClassDao){
		this.fileClassDao=fileClassDao;
	}
	
	public Long addFileClass(FileClass fileClass) {	
		updateLevel(fileClass);
		this.fileClassDao.save(fileClass);
		if (fileClass != null && fileClass.getId() != null) {
			return fileClass.getId();
		}
		return null;
	}
	
	public FileClass getFileClass(Long id) {
		FileClass fileClass = this.fileClassDao.get(id);
		return fileClass;
		}
	
	public boolean delFileClass(Long id) {	
			FileClass fileClass = this.getFileClass(id);
			if (fileClass != null) {
				this.fileClassDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelFileClasss(List<Serializable> fileClassIds) {
		
		for (Serializable id : fileClassIds) {
			delFileClass((Long) id);
		}
		return true;
	}
	
	public IPageList getFileClassBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, FileClass.class,this.fileClassDao);		
	}
	
	public boolean updateFileClass(Long id, FileClass fileClass) {
		if (id != null) {
			fileClass.setId(id);
		} else {
			return false;
		}
		updateLevel(fileClass);
		this.fileClassDao.update(fileClass);
		return true;
	}
	
	public String loadTree(Integer currentNodeId) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql;
		if(new Integer(0).equals(currentNodeId))
			sql = "select o from FileClass o where o.parent is null and o.disabled=0";
		else
			sql = "select o from FileClass o where o.parent.id="+currentNodeId+" and o.disabled=0";
		List list = fileClassDao.query(sql, null, 0, AppContext.RESULTSIZE);
		for(int i=0;i<list.size();i++){
			FileClass fileClass = (FileClass) list.get(i);
			Long id = fileClass.getId();
			result += "{id:"+id+",text:'"+fileClass.getName()+"("+fileClass.getCode()+")',";
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
		String sql = "select o from FileClass o where o.parent.id="+nodeId+" and o.disabled=false";
		List list = fileClassDao.query(sql, null, 0, 1);
		return list.isEmpty();
	}
	
	private void updateLevel(FileClass fileClass){
		FileClass parent = fileClass.getParent();
		if(null!=parent)
			fileClass.setLevel(parent.getLevel()+"-"+fileClass.getCode());
		else
			fileClass.setLevel(fileClass.getCode());
	}
	
	@Override
	public FileClass getFileClas(String code) {
		// TODO Auto-generated method stub
		String sql = "select f from FileClass f where f.code='"+code+"'";
		List list = fileClassDao.query(sql, null, 0, 1);
		if(list.isEmpty())
			return null;
		else
			return (FileClass) list.get(0);
	}

}
