package com.mhsoft.ext.web.tools;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.easyjf.core.dao.GenericDAO;
import com.easyjf.web.tools.IQuery;
import com.mhsoft.emr.dao.JDBCQueryDao;

public class JDBCQuery implements IQuery {

	private JDBCQueryDao dao;

	private int begin;

	private int max;
	
	private Collection paraValues;

	public JDBCQuery(JDBCQueryDao dao) {
		// TODO Auto-generated constructor stub
		this.dao = dao;
	}
	
	public List getResult(String sql) {
		// TODO Auto-generated method stub
		return dao.querySP(sql, paraValues.toArray(new Object[paraValues.size()]), begin, max);
	}

	public List getResult(String sql, int begin, int max) {
		// TODO Auto-generated method stub
		return dao.querySP(sql, paraValues.toArray(new Object[paraValues.size()]), begin, max);
	}

	public int getRows(String sql) {
		// TODO Auto-generated method stub
		try{
			return (Integer)dao.queryForObject(sql, Integer.class);
		}catch(EmptyResultDataAccessException e){
			return 0;
		}
	}

	public void setFirstResult(int begin) {
		// TODO Auto-generated method stub
		this.begin = begin;
	}

	public void setMaxResults(int max) {
		// TODO Auto-generated method stub
		this.max = max;
	}

	public void setParaValues(Collection paraValues) {
		// TODO Auto-generated method stub
		this.paraValues = paraValues;
	}

}
