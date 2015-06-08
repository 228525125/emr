package com.mhsoft.ext.web.tools;

import java.util.Collection;
import java.util.List;
import com.easyjf.core.dao.GenericDAO;
import com.easyjf.web.tools.IQuery;
/**
 * 实现IQuery接口,注意方法getResult的参数由查询条件改为完整查询语句
 * @author Administrator
 *
 */
public class JpaQuery implements IQuery {

	private GenericDAO dao;

	private int begin;

	private int max;
	
	private Collection paraValues;
	
	public JpaQuery(GenericDAO dao) {
		this.dao = dao;
	}

	public List getResult(String querySQL) {
		Object[] params = null;
		if (this.paraValues != null) {
			params = this.paraValues.toArray();
		}
		return dao.query(querySQL, params, begin, max);
	}

	public List getResult(String querySQL, int begin, int max) {
		Object[] params = null;
		if (this.paraValues != null) {
			params = this.paraValues.toArray();
		}
		return this.dao.query(querySQL, params, begin, max);
	}

	public int getRows(String condition) {
		int n = condition.toLowerCase().indexOf("order by");
		Object[] params = null;
		if (this.paraValues != null) {
			params = this.paraValues.toArray();
		}
		if (n > 0) {
			condition = condition.substring(0, n);
		}
		List ret = dao.query(condition, params, 0, 0);
		if (ret != null && ret.size() > 0) {
			return ((Long) ret.get(0)).intValue();
		} else {
			return 0;
		}
	}

	public void setFirstResult(int begin) {
		this.begin = begin;
	}

	public void setMaxResults(int max) {
		this.max = max;
	}

	public void setParaValues(Collection params) {
		this.paraValues = params;
	}

}
