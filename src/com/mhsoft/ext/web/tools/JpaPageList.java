package com.mhsoft.ext.web.tools;

import java.util.Collection;

import com.easyjf.core.dao.GenericDAO;
import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.web.tools.IQuery;
import com.easyjf.web.tools.PageList;

public class JpaPageList extends PageList {
	private String totalSQL;
	protected String querySQL;
	
	protected Class cls;
	/**
	 * 
	 * @param cls 实体类型
	 * @param queryObject 查询对象
	 * @param dao 
	 */
	public JpaPageList(Class cls,IQueryObject queryObject,GenericDAO dao)
	{
		this(cls,queryObject.getQuery(), null, queryObject.getParameters(),dao);
	}
	/**
	 * 
	 * @param cls 实体类型(考虑要删除)
	 * @param totalSQL 统计查询语句
	 * @param queryHQL 数据查询语句(注意是完整查询语句，不仅是查询条件)
	 * @param paras	参数列表
	 * @param dao
	 */
	public JpaPageList(Class cls, String totalSQL, String querySQL, Collection paras,
			GenericDAO dao) {
		this.cls = cls;
		if(null!=querySQL&&!"".equals(querySQL))
			this.querySQL = querySQL;

		if(null!=totalSQL&&!"".equals(totalSQL))
			this.totalSQL = totalSQL;
		else
			//了解一下这种方式行不行?
			//this.totalSQL = "select count(1) " + this.querySQL.substring(this.querySQL.indexOf(" from "));
			//这种方式中其中一个类的别名一定为obj
			this.totalSQL = "select count(distinct obj) " + this.querySQL.substring(this.querySQL.indexOf(" from "));
					
		IQuery query = new JpaQuery(dao);
		query.setParaValues(paras);
		this.setQuery(query);
	}

	
	/**
	 * 查询
	 * 
	 * @param currentPage
	 *            当前页数
	 * @param pageSize
	 *            一页的查询个数
	 */
	public void doList(int currentPage, int pageSize) {
		super.doList(pageSize, currentPage, totalSQL, querySQL);
	}
}