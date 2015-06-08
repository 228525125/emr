package com.mhsoft.emr.mvc;

import com.easyjf.web.ActionContext;
import com.easyjf.web.Module;
import com.easyjf.web.Page;
import com.easyjf.web.WebForm;
import com.mhsoft.emr.domain.EmrUser;

public class FrameAction extends BaseAction {

	public Page doIndex(WebForm f, Module m) {
		if(null!=getUser()){
			return page("index");
		}else{
			return page("login");
		}
		
	}
}
