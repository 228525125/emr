package com.mhsoft.emr.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.util.Assert;

public class ScrollInsensitivePreparedStatementCreator implements
		PreparedStatementCreator,SqlProvider  {
	
	private String sql;
	
	public ScrollInsensitivePreparedStatementCreator(String sql) {   
		  
        Assert.notNull(sql, "SQL must not be null");   
        this.sql = sql;   
    } 

	public PreparedStatement createPreparedStatement(Connection con)
			throws SQLException {
		// TODO Auto-generated method stub
		return con.prepareStatement(this.sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
	}

	public String getSql() {
		// TODO Auto-generated method stub
		return this.sql;
	}

}
