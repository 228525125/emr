package com.mhsoft.emr.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mhsoft.emr.dao.JDBCQueryDao;

public class JDBCQueryDaoImpl extends JdbcDaoSupport implements JDBCQueryDao {

	public List queryForList(String sql) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().queryForList(sql);
	}

	public List queryForList(String sql, Object[] params) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().queryForList(sql, params);
	}

	public Map queryForMap(String sql) {
		// TODO Auto-generated method stub
		return getJdbcTemplate().queryForMap(sql);
	}

	public Object queryForObject(String sql, Class type) {
		// TODO Auto-generated method stub		
		return getJdbcTemplate().queryForObject(sql, type);		
	}
	
	@Override
	public Object execute(String callName, CallableStatementCallback csc) {
		// TODO Auto-generated method stub
		//getJdbcTemplate().call(CallableStatementCreator, arg1)
		return getJdbcTemplate().execute(callName, csc);
	}
	
	

	public List querySP(String sql, Object[] params, int start, int len) {
		// TODO Auto-generated method stub
		return (List) getJdbcTemplate().query(new ScrollInsensitivePreparedStatementCreator(sql), new SplitPageResultSetExtractor(new RowMapper(){

			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub							
				if(ResultSet.TYPE_SCROLL_INSENSITIVE==rs.getType()){					
					if(rs.absolute(rowNum)){	
						ResultSetMetaData rsmd= rs.getMetaData();
						Map bean = new HashMap();
						for(int i=1;i<=rsmd.getColumnCount();i++){
							String label = rsmd.getColumnName(i);
							bean.put(label, rs.getObject(label));
						}
						return bean;
					}
				}else{
					for(;rs.getRow()<=rowNum;rs.next()){
						if(rs.getRow()==rowNum){
							ResultSetMetaData rsmd= rs.getMetaData();
							Map bean = new HashMap();
							for(int i=1;i<=rsmd.getColumnCount();i++){
								String label = rsmd.getColumnName(i);
								bean.put(label, rs.getObject(label));
							}
							return bean;
						}
					}					
				}
				
				return null;
			}
			
		},start,len));		
	}

}
