package com.mhsoft.emr.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

public class SplitPageResultSetExtractor implements ResultSetExtractor {

	private int start;// 起始行号 
	private int len;// 结果集合的长度 
	private final RowMapper rowMapper;// 行包装器
	
	public SplitPageResultSetExtractor(RowMapper rowMapper, int start, int len) {
		// TODO Auto-generated constructor stub
		Assert.notNull(rowMapper, "RowMapper is required"); 
		this.rowMapper = rowMapper;
		this.start = start;
		this.len = len;
	}
	
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		// TODO Auto-generated method stub
		List result = new ArrayList();
		int rowNum = 0;
		int end = start + len;
		this.start += 1;
		point: while (rs.next()) {
			++rowNum;
			if (rowNum < start) {
				continue point;
			} else if (rowNum > end) {
				break point;
			} else {
				result.add(this.rowMapper.mapRow(rs, rowNum));
			}
		}
		return result; 
	}

}
