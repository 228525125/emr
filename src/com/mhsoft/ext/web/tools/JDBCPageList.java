package com.mhsoft.ext.web.tools;

import java.util.Collection;

import com.easyjf.core.support.query.QueryObject;
import com.easyjf.web.tools.IQuery;
import com.easyjf.web.tools.PageList;
import com.mhsoft.emr.dao.JDBCQueryDao;

public class JDBCPageList extends PageList {

	private String totalSQL;
	private String sql = "select 1";
	
	public JDBCPageList(String totalSQL, String query_sql, QueryObject qo, JDBCQueryDao dao) {
		this(totalSQL, query_sql, qo.getOrderBy(), qo.getOrderType(), qo.getParameters(), false, dao);
	}
	
	public JDBCPageList(String totalSQL, String query_sql, QueryObject qo, boolean isCallable, JDBCQueryDao dao) {
		this(totalSQL, query_sql, qo.getOrderBy(), qo.getOrderType(), qo.getParameters(), isCallable, dao);
	}
	
	public JDBCPageList(String totalSQL, String query_sql, String orderBy, String orderType, Collection params, boolean isCallable, JDBCQueryDao dao) {
		// TODO Auto-generated constructor stub
		if(null!=totalSQL&&!"".equals(totalSQL))
			this.totalSQL = totalSQL;
		this.sql = query_sql;
		if(isCallable){                   //如果是过程，就采用传参的方式执行，这里不需要操作
			;
		}else{
			if(null!=orderBy&&!"".equals(orderBy)&&(query_sql.indexOf(" order by ")<=0)){
				this.sql += " order by "+orderBy;
				if(null!=orderType&&!"".equals(orderType))
					this.sql += " "+orderType;
			}
		}		
		IQuery query = new JDBCQuery(dao);
		query.setParaValues(params);
		this.setQuery(query);
	}
	
	public void doList(int pageNo, int pageSize) {
		String totalSQL = null;
		if(null!=this.totalSQL&&!"".equals(this.totalSQL))
			totalSQL = this.totalSQL;
		else
			totalSQL = "select COUNT(*) "+sql.substring(sql.indexOf("from"));
		super.doList(pageSize, pageNo, totalSQL, sql);
	}
}
