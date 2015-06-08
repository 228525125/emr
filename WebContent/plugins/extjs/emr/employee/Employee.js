openjoindepartmentwindow = function(organizationId,employeeId){
	if(organizationId&&null!=organizationId&&employeeId&&null!=employeeId){
		var win = new SelectDepartmentWindow({
			organizationId: organizationId,
			departmentId: '-1',
			employeeId: employeeId
		});
		win.show();
	}
}
openjoinrolewindow = function(employeeId){
	if(employeeId&&null!=employeeId){
		var win = new SelectRoleWindow({			
			roleId: '-1',
			employeeId: employeeId
		});
		win.show();
	}
}