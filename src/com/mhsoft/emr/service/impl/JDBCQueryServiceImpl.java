package com.mhsoft.emr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easyjf.core.support.query.IQueryObject;
import com.easyjf.core.support.query.QueryObject;
import com.easyjf.web.tools.IPageList;
import com.mhsoft.ext.web.tools.DefaultOrderBy;
import com.mhsoft.ext.web.tools.QueryOrderBy;
import com.mhsoft.emr.dao.JDBCQueryDao;
import com.mhsoft.emr.domain.Department;
import com.mhsoft.emr.service.JDBCQueryService;
import com.mhsoft.emr.util.QueryUtil;

@Transactional
@Service("jdbcService")
public class JDBCQueryServiceImpl implements JDBCQueryService {

	private static final QueryOrderBy orderby = new DefaultOrderBy();
	
	@Autowired
	private JDBCQueryDao jdbcDao;
	
	public IPageList user_list(QueryObject qo, String duodianzhichi) {
		// TODO Auto-generated method stub
		String sql = "select u1.nick,u1.sex,u1.xingming,u1.chengshi,u1.zhuzhi,u1.shouji,u1.zuoji,u1.mail,u1.beizhu,u1.isHuiyuan,u1.gongsimingcheng,u1.gongsidizhi,u1.gongsidianhua,u1.gongsichuanzhen,hy.title as huiyuan,u1.tingyong from User u1 left join User u2 on u1.shopkeeper=u2.nick left join Huiyuan hy on u1.huiyuan=hy.id where u2.duodianzhichi='"+duodianzhichi+"'";
		return QueryUtil.query(qo, null, sql, orderby, jdbcDao);
	}

	public IPageList query(QueryObject qo, String totalSQL, String querySQL) {
		return QueryUtil.query(qo, totalSQL, querySQL, orderby, jdbcDao);
	}
	
	@Override
	public List query(String sql) {
		// TODO Auto-generated method stub
		return jdbcDao.queryForList(sql);
	}
	
	@Override
	public Object query(String sql, Class type) {
		// TODO Auto-generated method stub
		return jdbcDao.queryForObject(sql, type);
	}
	
	@Override
	public List query_dynamicgrid(QueryObject qo, String totalSQL, String querySQL, boolean isCallable) {
		// TODO Auto-generated method stub			
		IPageList pageList = QueryUtil.query(qo, totalSQL, querySQL, orderby, isCallable, jdbcDao);
		List<Map<String,String>> gridhead = getGridHead(pageList);
		List result = new ArrayList();
		result.add(pageList);
		result.add(gridhead);
		return result;
	}
	
	/**
	 * 获取表头信息，还没有处理分页信息
	 * @param pageList
	 * @return
	 */
	private List<Map<String,String>> getGridHead(IPageList pageList){
		List<Map<String,String>> gridhead = new ArrayList<Map<String,String>>();
		List list = pageList.getResult();		
		if(list.size()>1){
			Map bean = (Map) list.get(list.size()-1);
			Iterator it = bean.keySet().iterator();
			while(it.hasNext()){
				Map<String,String> headmap = new HashMap<String,String>();
				Object key = it.next();
				headmap.put("header", bean.get(key).toString());
				headmap.put("dataIndex", key.toString());
				gridhead.add(headmap);
			}
			list.remove(list.size()-1);
		}
		
		return gridhead;
	}
	
	@Override
	public String queryForWorkNo(String BillNo) {
		// TODO Auto-generated method stub
		String sql = "select b.FNumber from AIS20091218114908.dbo.ICMO a left join AIS20091218114908.dbo.t_ICItem b on a.FItemID=b.FItemID where FBillNo='"+BillNo+"'";
		Object ret = jdbcDao.queryForObject(sql, String.class);
		if(null!=ret)
			return ret.toString();
		else
			return null;
	}
	
	@Override
	public String icbomTree(Integer did) {
		// TODO Auto-generated method stub
		String result = "[";
		String sql = "select f.id,b.FItemID,d.FNumber,d.FName,d.FModel,d.FHelpCode " +
				 " from AIS20091218114908.dbo.ICBOM a " + 
				 " left join AIS20091218114908.dbo.ICBOMCHILD b on a.FInterID=b.FInterID " +
				 " left join AIS20091218114908.dbo.t_ICItem c on a.FItemID=c.FItemID " +
				 " left join AIS20091218114908.dbo.t_ICItem d on b.FItemID=d.FItemID " +
				 " left join Department e on c.FNumber=e.Code " +
				 " left join Department f on d.FNumber=f.Code " + 
				 " where 1=1 " +
				 " and e.id = "+did+" " +
				 " order by c.FNumber ";
		
		List list = query(sql);
		
		for(int i=0;i<list.size();i++){
			Map bean = (Map) list.get(i);
			String id = "'"+bean.get("id").toString()+"'";
			result += "{id:"+id+",text:'"+"("+bean.get("FNumber")+")"+bean.get("FName")+"',";
			if(icbomTree_isLeaf(Integer.valueOf(bean.get("FItemID").toString())))
				result += "leaf:true}";
			else
				result += "leaf:false}";
			if(i<list.size()-1)
				result += ",";
		}
		return result+"]";
	}
	
	private boolean icbomTree_isLeaf(Integer itemID){
		String sql = "select 1 from AIS20091218114908.dbo.ICBOM where FItemID ="+itemID;
		List list = query(sql);
		return list.isEmpty();
	}
}
