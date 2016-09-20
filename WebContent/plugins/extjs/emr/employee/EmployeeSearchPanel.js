EmployeeSearchPanel = Ext.extend(Ext.Viewport, {
	layout: 'fit',
	id:'employeesearchpanel',
    initComponent: function() {
		if(''==this.department)
			this.department = '';
		if(''==this.fileClass)
			this.fileClass = ''; 
		this.role = '';
		this.inputType = 0;
		
		this.departmentField = new Ext.form.TextField({
            fieldLabel: '物料代码',
            name: 'department',
            value: this.department,
            listeners:{
        		'change':{fn:function(t,valuenew,valueold){
					this.department=valuenew;
        		},scope:this},
        		'specialkey':{fn: function(f, e){
        			if(e.getKey() == e.ENTER){
        				this.department = f.getValue();
        				this.panel.load({
        	            	url: "employee.do?cmd=searchFile",
        	            	params: {department: this.department, role: this.role, fileClass: this.fileClass},
        	            	text: "Loading...",
        	            	timeout: 30,
        	            	scripts: false,
        	            	scope: this,
        	            	callback : function(){
        	            		this.textField.setValue(this.departmentField.getValue());
        	            		if(1==this.inputType)
        	            			this.departmentField.setValue('');
        	            	}
        	            });

        				/*if(1==this.inputType){
            				f.setValue('');
        				}*/
                    }
        		},scope:this}
        	}
		});
		
		this.roleField = new Ext.form.TextField({
			fieldLabel: '工位',
            name: 'role',
            allowBlank:true,
            listeners:{
        		'change':{fn:function(t,valuenew,valueold){
					this.role=valuenew;
        		},scope:this},
        		'specialkey':{fn: function(f, e){
        			if(e.getKey() == e.ENTER){
        				this.fileClassField.focus();
                    }
        		},scope:this}
        	}
		});
		
		this.fileClassStore = new Ext.data.JsonStore({
			url: 'fileClass.do?cmd=selectList',
			root:"result",
			fields:["id","name"],
			listeners:{
				'load':{fn:function(storeThis,option){
					this.fileClassField.setValue(this.fileClass);
				},scope:this}
			},
			baseParams:{pageSize:100}
		});
		
		this.fileClassField = new Ext.form.ComboBox({
            fieldLabel: '文件类型',
            anchor: '90%',
            name: 'fileClass',
            hiddenName: 'fileClass',
            valueField: 'id',
            displayField: 'name',
            emptyText: '请选择...',
            mode: 'local',                
            triggerAction: 'all',
            editable:false,
            forceSelection: true,
            store: this.fileClassStore,
            listeners:{
        		'change':{fn:function(t,valuenew,valueold){
					this.fileClass=valuenew;					
        		},scope:this},
        		'specialkey':{fn: function(f, e){
        			this.fileClass = f.getValue();
        			if(e.getKey() == e.ENTER){
        				this.departmentField.focus();
        				
                    }
        		},scope:this}
        	}
		});
		
		this.queryButton = new Ext.Button({
			text: '查询',
            pressed: true,           
            handler: function(){
				this.panel.load({
	            	url: "employee.do?cmd=searchFile",
	            	params: {department: this.department, role: this.role, fileClass: this.fileClass},   //为什么不加 serial: this.serial 这个条件，因为这个模式是为扫描枪准备的，因此也不会使用这个功能来查看通用文档 
	            	text: "Loading...",
	            	timeout: 30,
	            	scripts: false,
	            	scope: this
	            });
			},
            scope:this
		});
		
		this.model = new Ext.form.ComboBox({
            fieldLabel: '输入类型',
            anchor: '90%',
            name: 'disabled',
            hiddenName: 'disabled',
            valueField: 'id',
            displayField: 'name',
            store: new Ext.data.Store({     
	            data:[[0,'键盘'],[1,'扫描枪']], 
	            autoLoad: true,
	            reader:new Ext.data.ArrayReader({}, [
	                  {name: 'id'},
	                  {name: 'name'}
				])
	        }),
            value: 0,
            mode: 'local',
            triggerAction: 'all',
            forceSelection: true,
            listeners:{
			'change':{fn:function(t,valuenew,valueold){
					this.inputType=valuenew;
					this.departmentField.setValue('');
				},scope:this}
			}
        });
		
		this.textField = new Ext.form.TextField({
			readOnly:true
		}); 
		
		this.panel = new Ext.Panel({
			border:true,
			autoScroll: true,
			tbar:[
				{
				    xtype: 'spacer',
				    width: 6
				},'模式:',{
				    xtype: 'spacer',
				    width: 6
				},this.model,
				'-','工位:',{
				    xtype: 'spacer',
				    width: 6
				},this.roleField,
				'-','文件类型:',{
				    xtype: 'spacer',
				    width: 6
				},this.fileClassField,
				'-','代码\图号\任务单:',{
				    xtype: 'spacer',
				    width: 6
				},this.departmentField,
				{
				    xtype: 'spacer',
				    width: 6
				},this.queryButton,
				'->','当前查询物料代码：',this.textField
			]
		});
		
		this.items = [this.panel];
		             
		EmployeeSearchPanel.superclass.initComponent.call(this);
		
		this.on('render',function(t){
			this.fileClassStore.load();
		});
	}
});