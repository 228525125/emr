package com.mhsoft.emr.service;

import java.io.Serializable;
import java.util.List;
import com.easyjf.web.tools.IPageList;
import com.easyjf.core.support.query.IQueryObject;
import com.mhsoft.emr.domain.Station;
/**
 * StationService
 * @author EasyJWeb 1.0-m2
 * $Id: StationService.java,v 0.0.1 2011-3-11 11:14:25 EasyJWeb 1.0-m2 Exp $
 */
public interface IStationService {
	/**
	 * 保存一个Station，如果保存成功返回该对象的id，否则返回null
	 * 
	 * @param instance
	 * @return 保存成功的对象的Id
	 */
	Long addStation(Station instance);
	
	/**
	 * 根据一个ID得到Station
	 * 
	 * @param id
	 * @return
	 */
	Station getStation(Long id);
	
	/**
	 * 删除一个Station
	 * @param id
	 * @return
	 */
	boolean delStation(Long id);
	
	/**
	 * 批量删除Station
	 * @param ids
	 * @return
	 */
	boolean batchDelStations(List<Serializable> ids);
	
	/**
	 * 通过一个查询对象得到Station
	 * 
	 * @param properties
	 * @return
	 */
	IPageList getStationBy(IQueryObject queryObject);
	
	/**
	  * 更新一个Station
	  * @param id 需要更新的Station的id
	  * @param dir 需要更新的Station
	  */
	boolean updateStation(Long id,Station instance);
}
