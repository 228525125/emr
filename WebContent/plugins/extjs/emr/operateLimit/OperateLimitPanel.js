OperateLimitPanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'operatelimitpanel',
    createForm:function(){
		if(!this.fp||null==this.fp){
			this.fp = new OperateLimitFormPanel({
				moduleStore: this.moduleStore							
			});	
			this.fp.form.findField("module").setValue(this.moduleId);
		}
		if(!this.win||null==this.win){
			this.win = new Ext.Window({
				width:360,
				height:350,
				layout:'fit',
				buttonAlign:"center",
				title:'编辑操作权限信息',
				modal:true,
				shadow:true,
				closeAction:"close",
				items:[this.fp],
				buttons:[{text:"保存",
						  handler:function(){
							this.fp.form.submit({
									waitMsg:'正在保存。。。',
						            url:'operateLimit.do?cmd=save',
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
	add_operatelimit:function(){
		if(this.moduleId==0)
			Ext.Msg.alert("提示","请先选择功能模块!");
		else{
			this.createForm();
			this.win.show();
		}
	},
	edit_operatelimit:function(){		
		var record=this.gp.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示","请先选择要编辑的行!");
			return;
		}
		
		this.createForm();
		this.win.show();
		this.fp.form.loadRecord(record);		
		//============对combo类型的字段赋值==============//
		if(record.get("module")&&null!=record.get("module"))
			this.fp.form.findField("module").setValue(record.get("module").id);		
	},
    initComponent: function() {
		this.store = new Ext.data.JsonStore({
			url: 'operateLimit.do?cmd=list',
			root:"result",
			fields:["id","code","name","auxCode","disabled","description","module","limitType"],
			listeners:{
				'beforeload': function(storeThis,option){
					storeThis.removeAll();
				}
			}
		});
		
		this.moduleStore = new Ext.data.JsonStore({
			url: 'module.do?cmd=list',
			root:"result",
			fields:["id","name"]
		});		
		
		this.moduleId = 0;
		
		this.store.on('beforeload',function(storeThis,option){
			storeThis.baseParams.moduleId = this.moduleId;			
		},this);
		
		this.root = new Ext.tree.AsyncTreeNode({
			text: '功能模块',
            id: '0',                      //分别表示机构编号、部门编号   	
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
                items: [
					{
					    xtype: 'combo',
					    width: 200,
					    name: 'moduleselect',
					    valueField: 'id',
					    displayField: 'name',
					    emptyText: '请选择功能模块...',
					    mode: 'local',
					    store: this.moduleStore,
					    triggerAction: 'all',
					    listeners: {
							'select':{fn:function(combo,newValue,oldValue){
								this.moduleId = newValue.data.id;
								this.tree.getRootNode().id = this.moduleId;
								this.tree.getRootNode().setText(newValue.data.name);
								this.store.load();
								this.tree.getLoader().load(this.tree.getRootNode());								
							},scope:this}
						}
					},
                    {
                        xtype: 'treepanel',
                        id: 'operatelimittreepanel',	
                        border: false,
                        root: {
                            text: '功能模块',
                            id: '0'                      //分别表示机构编号、部门编号
                        },
                        loader: {
                        	
                            url: 'operateLimit.do?cmd=tree',                            
                            listeners: {
                        		'beforeload':{fn:function(loader,node){
	    							loader.baseParams.moduleId=this.moduleId;	    							
	    						},scope:this}
                        	}
                        },
                        listeners: {                        	
                        	'click':{fn:function(node,e){                			                			
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
                        id: 'operatelimitgrid',
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
                                header: '描述',
                                sortable: true,
                                width: 200,
                                dataIndex: 'description'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '所属模块',
                                sortable: false,
                                width: 100,
                                dataIndex: 'module',
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}
                            },
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
                                    handler: this.add_operatelimit,
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
                                    handler: this.edit_operatelimit,
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
                            ]
                        },
                        bbar: {
                            xtype: 'paging',
                            pageSize: 22,
                            store: this.store,
                            displayInfo: true,
                            displayMsg: '共{2}条记录，当前第 {0}条 到 {1}条',
                            emptyMsg: '没有找到记录'
                        }
                    }
                ]
            }
        ];
        OperateLimitPanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('operatelimitgrid');
        	this.tree = Ext.getCmp('operatelimittreepanel');	
			this.store.load();
			this.moduleStore.load();			
		},this);
    }
});
