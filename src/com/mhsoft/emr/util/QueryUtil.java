package com.mhsoft.emr.util;

import com.mhsoft.ext.web.tools.JpaPageList;
import com.mhsoft.ext.web.tools.QueryOrderBy;
import com.mhsoft.ext.web.tools.JDBCPageList;
import com.mhsoft.emr.dao.JDBCQueryDao;

import com.easyjf.core.dao.GenericDAO;
import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.PageObject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.web.tools.IPageList;

public class QueryUtil {

	/*public static IPageList query(QueryObject queryObject, String totalSQL, String sql, GlobalDao dao){
		PageObject pageObj = queryObject.getPageObj();
		int currentPage;
		int pageSize;
		currentPage = pageObj.getCurrentPage();
		pageSize = pageObj.getPageSize();
		TopPageList pageList = new TopPageList(totalSQL, sql, queryObject, dao);
		pageList.doList(currentPage, pageSize);// 查询第几页，每页多少条
		return pageList;
	}*/
	
	public static IPageList query(QueryObject queryObject, String totalSQL, String sql, QueryOrderBy orderBy, JDBCQueryDao dao){		
		return query(queryObject, totalSQL, sql, orderBy, false, dao);
	}
	
	public static IPageList query(QueryObject queryObject, String totalSQL, String sql, boolean isCallable, JDBCQueryDao dao){		
		return query(queryObject, totalSQL, sql, null, isCallable, dao);
	}
	
	/**
	 *利用JDBC执行SQL查询,返回PageList
	 */
	public static IPageList query(QueryObject queryObject, String totalSQL, String sql, QueryOrderBy orderBy, boolean isCallable, JDBCQueryDao dao){
		PageObject pageObj = queryObject.getPageObj();
		int currentPage;
		int pageSize;
		currentPage = pageObj.getCurrentPage();
		pageSize = pageObj.getPageSize();
		if(null!=queryObject.getOrderBy()&&!isCallable)
			queryObject.setOrderBy(orderBy.getField(queryObject.getOrderBy(), sql));
		JDBCPageList pageList = new JDBCPageList(totalSQL, sql, queryObject, isCallable, dao);
		pageList.doList(currentPage, pageSize);// 查询第几页，每页多少条
		return pageList;
	}
	
	/**
	 * 数据查询工具，可以通过queryObject查询封装来进行数据，数据查询涉及到查询条件，参数值，还涉及到分页信息等。若页码不符合查询范围，由自动设置为第一页，若pageSize为-1，则查询所有数据
	 * 
	 * @param queryObject
	 * @param entityType
	 * @param totalSQL 统计查询语句
	 * @param querySQL 查询语句
	 * @param dao
	 * @return 分页查询结果
	 */
	public static IPageList query(IQueryObject queryObject, Class entityType, String totalSQL,  String querySQL, 
			GenericDAO dao) {
		PageObject pageObj = queryObject.getPageObj();
		int currentPage;
		int pageSize;
		currentPage = pageObj.getCurrentPage();
		pageSize = pageObj.getPageSize();
		JpaPageList pageList = new JpaPageList(entityType, totalSQL, querySQL, queryObject.getParameters(), dao);
		pageList.doList(currentPage, pageSize);// 查询第几页，每页多少条
		return pageList;
	}

}
