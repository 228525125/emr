package com.mhsoft.emr.service;

import java.util.List;
import java.util.Map;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.web.tools.IPageList;

public interface JDBCQueryService {

	/**
	 * 样板方法
	 * @param qo
	 * @param duodianzhichi
	 * @return
	 */
	public IPageList user_list(QueryObject qo, String duodianzhichi);
	/**
	 * 公用JDBC查询
	 * @param qo
	 * @param sql
	 * @return IPageList
	 */
	
	public IPageList query(QueryObject qo, String totalSQL, String querySQL);
	
	/**
	 * 公共查询
	 * @param sql
	 * @return list
	 */
	public List query(String sql);
	
	/**
	 * 公共查询
	 * @param sql
	 * @param type
	 * @return
	 */
	public Object query(String sql, Class type);
	
	/**
	 * 只用于DynamicGrid的生成
	 * @param sql
	 * @return 返回的结果包含了表头数据，原理是sql语句把表头名称与dataIndex用分隔符分开例如：“名称-name”
	 */
	public List query_dynamicgrid(QueryObject qo, String totalSQL, String querySQL, boolean isCallable);
	
	/**
	 * 根据任务单查询物料代码
	 * @param BillNo
	 * @return
	 */
	String queryForWorkNo(String BillNo);
	
	public String icbomTree(Integer did);
}
