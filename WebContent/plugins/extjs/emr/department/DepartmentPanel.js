DepartmentPanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'departmentpanel',
    createForm:function(){
		if(!this.fp||null==this.fp){
			this.fp = new DepartmentFormPanel({
				organizationStore: this.organizationStore,
				parentStore: this.parentStore,
				employeeStore: this.employeeStore				
			});
			this.fp.form.findField("organization").setValue(this.organizationId);
			if(0!=this.departmentId)
				this.fp.form.findField("parent").setValue(this.departmentId);
		}
		if(!this.win||null==this.win){
			this.win = new Ext.Window({
				width:360,
				height:350,
				layout:'fit',
				buttonAlign:"center",
				title:'编辑物料信息',
				modal:true,
				shadow:true,
				closeAction:"close",
				items:[this.fp],
				buttons:[{text:"保存",
						  handler:function(){
							this.fp.form.submit({
									waitMsg:'正在保存。。。',
						            url:'department.do?cmd=save',
						            method:'POST',
						            success:function(form,action){
										if(null!=action.result&&action.result.msg)
							        		Ext.Msg.alert('提示',action.result.msg,function(){
							        			this.win.close();
									           	this.store.reload(); 
							        		},this);
										else{
								           	this.win.close();
								           	this.store.reload();
										}
						            },
						            scope:this
							});	
								
						  },
						  scope:this},
						  {text:"清空",
						   handler:function(){
							  this.fp.form.reset();
						   },
						   scope:this},
						  {text:"取消",
						   handler:function(){
							  this.win.close();  
						   },
						   scope:this}
						 ]					  
			});
			this.win.on('close',function(){this.fp=null;this.win=null;},this);
		}
	},
	add_department:function(){
		if(this.organizationId==0)
			Ext.Msg.alert("提示","请先选择机构!");
		else{
			this.createForm();
			this.win.show();
		}
	},
	edit_department:function(){		
		var record=this.gp.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示","请先选择要编辑的行!");
			return;
		}
		
		this.createForm();
		this.win.show();
		this.fp.form.loadRecord(record);		
		//============对combo类型的字段赋值==============//
		if(record.get("parent")&&null!=record.get("parent"))
			this.fp.form.findField("parent").setValue(record.get("parent").id);
		if(record.get("organization")&&null!=record.get("organization"))
			this.fp.form.findField("organization").setValue(record.get("organization").id);
		/* form没有这个字段
		if(record.get("owner")&&null!=record.get("owner"))
			this.fp.form.findField("owner").setValue(record.get("owner").id);*/
	},
    initComponent: function() {
		this.store = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","code","name","model","tuhao","auxCode","disabled","description","organization","parent"],
			listeners:{
				'beforeload': function(storeThis,option){
					storeThis.removeAll();
				}
			},
			baseParams:{pageSize:2000}
		});
		
		this.parentStore = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","name"],
			baseParams:{pageSize:50}
		});
		
		this.organizationStore = new Ext.data.JsonStore({
			url: 'organization.do?cmd=list',
			root:"result",
			fields:["id","name"]
		});
		
		this.employeeStore = new Ext.data.JsonStore({
			url: 'employee.do?cmd=list',
			root:"result",
			fields:["id","name"]
		});
		
		this.organizationId = 0;
		this.departmentId = 0;
		
		this.store.on('beforeload',function(storeThis,option){
			storeThis.baseParams.organizationId = this.organizationId;
			storeThis.baseParams.departmentId = this.departmentId;
		},this);
		
		this.queryfield = new Ext.app.SearchField({
            store: this.store,
            width:220,
            emptyText:'请输入关键字...',
            scope: this
        });
		
		this.root = new Ext.tree.AsyncTreeNode({
			text: '物料',
            id: '0,0',                      //分别表示机构编号、部门编号   	
			expanded:true
		});
	
        this.items = [
            {
                xtype: 'panel',
                title: '树形目录',
                region: 'west',
                width: 200,
                margins: '0 0 0 0',
                split: true,
                collapsible: true,
                autoScroll:true,
                items: [
					{
					    xtype: 'combo',
					    width: 200,
					    name: 'organizationselect',
					    valueField: 'id',
					    displayField: 'name',
					    emptyText: '请选择组织机构',
					    mode: 'local',
					    store: this.organizationStore,
					    triggerAction: 'all',
					    listeners: {
							'select':{fn:function(combo,newValue,oldValue){
								this.organizationId = newValue.data.id;
								this.departmentId = 0;
								this.tree.getRootNode().id = this.organizationId+','+this.departmentId;
								this.tree.getRootNode().setText(newValue.data.name);
								this.store.load();
								this.tree.getLoader().load(this.tree.getRootNode());
							},scope:this}
						}
					},
                    {
                        xtype: 'treepanel',
                        id: 'departmenttreepanel',	
                        border: false,
                        root: {
                            text: '物料',
                            id: '0,0'                      //分别表示机构编号、部门编号
                        },
                        loader: {
                        	
                            url: 'department.do?cmd=tree',                            
                            listeners: {
                        		'beforeload':function(loader,node){
	    							loader.baseParams.organizationId=node.id.split(",")[0];
	    							loader.baseParams.departmentId=node.id.split(",")[1];
	    						}
                        	}
                        },
                        listeners: {                        	
                        	'click':{fn:function(node,e){
	                			this.organizationId = node.id.split(",")[0];
	                			this.departmentId = node.id.split(",")[1];	                			
	                			this.store.load();   //刷新右边的内容
	                		},scope:this}
                        }
                    }
                ]
            },
            {
                xtype: 'panel',
                region: 'center',
                margins: '0 0 0 0',
                layout: 'fit',
                items: [
                    {
                        xtype: 'grid',
                        border: false,
                        loadMask: true,
                        trackMouseOver: false,
                        animCollapse: false,
                        store: this.store,
                        viewConfig: {
                            forceFit: true
                        },
                        id: 'departmentgrid',
                        columns: [
                            {
                                xtype: 'gridcolumn',
                                dataIndex: 'code',
                                header: '编码',
                                sortable: true,
                                width: 80
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '名称',
                                sortable: true,
                                width: 100,
                                dataIndex: 'name'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '规格',
                                sortable: true,
                                width: 100,
                                dataIndex: 'model'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '图号',
                                sortable: true,
                                width: 100,
                                dataIndex: 'tuhao'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '类型',
                                sortable: false,
                                width: 100,
                                dataIndex: 'parent',
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}
                            },
                            /*{
                                xtype: 'gridcolumn',
                                header: '所属机构',
                                sortable: false,
                                width: 100,
                                dataIndex: 'organization',
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}
                            },*/
                            {
                                xtype: 'gridcolumn',
                                header: '禁用',
                                sortable: true,
                                width: 50,
                                dataIndex: 'disabled',
                                renderer:function(value){if('0'==value){return "否";}else{return "是";}}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '',                                
                                hidden: true,
                                dataIndex: 'id'
                            }
                        ],
                        tbar: {
                            xtype: 'toolbar',
                            items: [{
                                xtype: 'spacer',
                                width: 6
                            	},
                                {
                                    xtype: 'button',
                                    text: '添加',
                                    pressed: true,           
                                    handler: this.add_department,
                                    scope:this
                                },
                                {
                                    xtype: 'spacer',
                                    width: 3
                                },
                                {
                                    xtype: 'button',
                                    text: '修改',
                                    pressed: true,           
                                    handler: this.edit_department,
                                    scope:this
                                },
                                {
                                    xtype: 'spacer',
                                    width: 3
                                },
                                /*{
                                    xtype: 'button',
                                    text: '删除'
                                },*/
                                {
                                    xtype: 'button',
                                    text: '刷新',
                                    pressed: true,           
                                    handler: function(){this.store.reload();},
                                    scope:this
                                }
                                ,'->','查找: ', ' ',
               		         	this.queryfield
                            ]
                        },
                        bbar: {
                            xtype: 'paging',
                            pageSize: 2000,
                            store: this.store,
                            displayInfo: true,
                            displayMsg: '共{2}条记录，当前第 {0}条 到 {1}条',
                            emptyMsg: '没有找到记录'
                        }
                    }
                ]
            }
        ];
        DepartmentPanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('departmentgrid');
        	this.tree = Ext.getCmp('departmenttreepanel');	
			this.store.load();
			this.parentStore.load();
			this.organizationStore.load();
		},this);
    }
});
