RolePanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'rolepanel',
    createForm:function(){
		if(!this.fp||null==this.fp){
			this.fp = new RoleFormPanel({
				parentStore: this.parentStore
			});
			if(0!=this.roleId)
				this.fp.form.findField("parent").setValue(this.roleId);			
		}
		if(!this.win||null==this.win){
			this.win = new Ext.Window({
				width:360,
				height:350,
				layout:'fit',
				buttonAlign:"center",
				title:'编辑工位信息',
				modal:true,
				shadow:true,
				closeAction:"close",
				items:[this.fp],
				buttons:[{text:"保存",
						  handler:function(){
							this.fp.form.submit({
									waitMsg:'正在保存。。。',
						            url:'role.do?cmd=save',
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
	add_organization:function(){
		this.createForm();
		this.win.show();
	},
	edit_organization:function(){		
		var record=this.gp.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示","请先选择要编辑的行!");
			return;
		}
		
		this.createForm();
		this.win.show();
		this.fp.form.loadRecord(record);
		this.fp.form.findField("parent").setValue(record.get("parent").id);
	},
    initComponent: function() {
		
		this.store = new Ext.data.JsonStore({
			url: 'role.do?cmd=list',
			root:"result",
			fields:["id","code","name","auxCode","disabled","description"],
			listeners:{
				'beforeload': function(storeThis,option){
					storeThis.removeAll();
				}
			}
		});
		
		this.parentStore = new Ext.data.JsonStore({
			url: 'role.do?cmd=list',
			root:"result",
			fields:["id","name"]
		});
		
		this.roleId = 0;
		
		this.store.on('beforeload',function(storeThis,option){
			storeThis.baseParams.roleId = this.roleId;
		},this);
	
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
                        xtype: 'treepanel',
                        id: 'roletreepanel',	
                        border: false,
                        root: {
                            text: '区域',
                            id: '0'
                        },
                        loader: {
                            url: 'role.do?cmd=tree',                            
                            listeners: {
                        		'beforeload':function(loader,node){
	    							loader.baseParams.roleId=node.id;
	    						}
                        	}
                        },
                        listeners: {                        	
                        	'click':{fn:function(node,e){
	                			this.roleId = node.id;
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
                        id: 'rolegrid',
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
                                header: '区域',
                                sortable: false,
                                width: 100,
                                dataIndex: 'parent',
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
                                    handler: this.add_organization,
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
                                    handler: this.edit_organization,
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
        RolePanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('rolegrid');
        	this.tree = Ext.getCmp('roletreepanel');	
			this.store.load();
			this.parentStore.load();
		},this);
    }
});
