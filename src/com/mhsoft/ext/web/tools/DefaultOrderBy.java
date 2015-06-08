package com.mhsoft.ext.web.tools;

public class DefaultOrderBy implements QueryOrderBy {

	public String getField(String field, String sql) {
		// TODO Auto-generated method stub
		String result = field;
		String mysql = sql.substring(6, sql.indexOf("from"));					
		while(mysql.indexOf(field)!=-1){
			int index = mysql.indexOf(field);			
			if('.'==mysql.charAt(index-1)){
				char [] chs = mysql.substring(0, index-1).toCharArray();
				int len = 1;
				for(int i=chs.length-1;i>-1;i--){
					Integer asc = Integer.valueOf(chs[i]);
					if(asc>=48&&asc<=57){
						len++;
						continue;
					}else if(asc>=65&&asc<=96){
						len++;
						continue;
					}else if(asc>=97&&asc<=122){
						len++;
						continue;
					}else{
						break;
					}
				}				
				result = mysql.substring(index-len, index)+field;   //取第一次找到的字段前缀
				break;
				//mysql = mysql.substring(index+field.length(), mysql.length());  //这里会一直查找最后一个符合条件的字段，因为有时候存在AS 的情况
			}else{
				result = field;
				break;
			}			
		}
		return result;
	}

}
