package com.mhsoft.emr.mvc;

import com.easyjf.container.annonation.Inject;
import com.mhsoft.emr.service.IPermissionService;

public class PermissionAction extends BaseAction {

	@Inject
	private IPermissionService service;

	public void setService(IPermissionService service) {
		this.service = service;
	}
	
	
}
