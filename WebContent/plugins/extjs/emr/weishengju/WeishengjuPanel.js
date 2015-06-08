WeishengjuPanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'weishengjupanel',
    createForm:function(){
		if(!this.fp||null==this.fp){
			this.fp = new WeishengjuFormPanel({
				parentStore: this.parentStore
			});
			if(0!=this.organizationId)
				this.fp.form.findField("parent").setValue(this.organizationId);
		}
		if(!this.win||null==this.win){
			this.win = new Ext.Window({
				width:360,
				height:350,
				layout:'fit',
				buttonAlign:"center",
				title:'编辑机构信息',
				modal:true,
				shadow:true,
				closeAction:"close",
				items:[this.fp],
				buttons:[{text:"保存",
						  handler:function(){
							this.fp.form.submit({
									waitMsg:'正在保存。。。',
						            url:'weishengju.do?cmd=save',
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
	add_weishengju:function(){
		this.createForm();
		this.win.show();
	},
	edit_weishengju:function(){		
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
			url: 'weishengju.do?cmd=list',
			root:"result",
			fields:["id","code","name","auxCode","disabled","description","address","contact","parent","principal"],
			listeners:{
				'beforeload': function(storeThis,option){
					storeThis.removeAll();
				}
			}
		});
		
		this.parentStore = new Ext.data.JsonStore({
			url: 'weishengju.do?cmd=list',
			root:"result",
			fields:["id","name"]
		});
		
		this.weishengjuId = 0;
		
		this.store.on('beforeload',function(storeThis,option){
			storeThis.baseParams.weishengjuId = this.weishengjuId;
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
                        id: 'weishengjutreepanel',	
                        border: false,
                        root: {
                            text: '组织机构',
                            id: '0'
                        },
                        loader: {
                            url: 'weishengju.do?cmd=tree',                            
                            listeners: {
                        		'beforeload':function(loader,node){
	    							loader.baseParams.weishengjuId=node.id;
	    						}
                        	}
                        },
                        listeners: {                        	
                        	'click':{fn:function(node,e){
	                			this.weishengjuId = node.id;
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
                        id: 'weishengjugrid',
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
                                header: '负责人',
                                sortable: true,
                                width: 80,
                                dataIndex: 'principal'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '地址',
                                sortable: true,
                                width: 200,
                                dataIndex: 'address'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '联系方式',
                                sortable: true,
                                width: 150,
                                dataIndex: 'contact'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '上级机构',
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
                                    handler: this.add_weishengju,
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
                                    handler: this.edit_weishengju,
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
        WeishengjuPanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('weishengjugrid');
        	this.tree = Ext.getCmp('weishengjutreepanel');	
			this.store.load();
			this.parentStore.load();
		},this);
    }
});
