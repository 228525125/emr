package com.mhsoft.emr.service.impl;
import java.io.Serializable;
import java.util.List;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryUtil;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.domain.Organization;
import com.mhsoft.emr.domain.Station;
import com.mhsoft.emr.service.IStationService;
import com.mhsoft.emr.dao.IStationDAO;


/**
 * StationServiceImpl
 * @author EasyJWeb 1.0-m2
 * $Id: StationServiceImpl.java,v 0.0.1 2011-3-11 11:14:25 EasyJWeb 1.0-m2 Exp $
 */
public class StationServiceImpl implements IStationService{
	
	private IStationDAO stationDao;
	
	public void setStationDao(IStationDAO stationDao){
		this.stationDao=stationDao;
	}
	
	public Long addStation(Station station) {	
		updateLevel(station);
		this.stationDao.save(station);
		if (station != null && station.getId() != null) {
			return station.getId();
		}
		return null;
	}
	
	public Station getStation(Long id) {
		Station station = this.stationDao.get(id);
		return station;
		}
	
	public boolean delStation(Long id) {	
			Station station = this.getStation(id);
			if (station != null) {
				this.stationDao.remove(id);
				return true;
			}			
			return false;	
	}
	
	public boolean batchDelStations(List<Serializable> stationIds) {
		
		for (Serializable id : stationIds) {
			delStation((Long) id);
		}
		return true;
	}
	
	public IPageList getStationBy(IQueryObject queryObject) {	
		return QueryUtil.query(queryObject, Station.class,this.stationDao);		
	}
	
	public boolean updateStation(Long id, Station station) {
		if (id != null) {
			station.setId(id);
		} else {
			return false;
		}
		updateLevel(station);
		this.stationDao.update(station);
		return true;
	}
	
	private void updateLevel(Station station){
		Department department = station.getDepartment();
		station.setLevel(department.getLevel()+"-"+station.getCode());
	}
	
}
