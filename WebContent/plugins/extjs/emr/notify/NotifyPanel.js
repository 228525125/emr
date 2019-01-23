NotifyPanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'notifypanel',
    createForm:function(){
		if(!this.fp||null==this.fp){
			this.fp = new NotifyFormPanel();
		}
		if(!this.win||null==this.win){
			this.win = new Ext.Window({
				width:360,
				height:400,
				layout:'fit',
				buttonAlign:"center",
				title:'编辑通知信息',
				modal:true,
				shadow:true,
				closeAction:"close",
				items:[this.fp],
				buttons:[{text:"保存",
						  handler:function(){
							this.fp.form.submit({
									waitMsg:'正在保存。。。',
						            url:'notify.do',
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
	add_notify:function(){		
		this.createForm();
		this.win.show();
	},
	edit_notify:function(){
		var record=this.gp.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示","请先选择要编辑的行!");
			return;
		}
			
		this.createForm();
		this.win.show();
		this.fp.form.loadRecord(record);	
	},
    initComponent: function() {
		this.store = new Ext.data.JsonStore({
			url: 'notify.do?cmd=list',
			root:"result",
			fields:["id","code","name","auxCode","disabled","description","address","empty","selected","version","extraEmpty","cite","date","checked","checkDate","checker"],
			listeners:{
				'beforeload': {fn:function(storeThis,option){
					storeThis.removeAll();
				},scope:this}
			},
			baseParams:{pageSize:2000}
		});
		
		this.queryfield = new Ext.app.SearchField({
            store: this.store,
            width:220,
            emptyText:'请输入关键字...',
            scope: this
        });
		
		this.state = new Ext.form.ComboBox({	        
	        hiddenName:'style',
	        valueField:'id',
	        displayField:'mc',
	        value:'false',
	        width:60,
	        allowBlank:false,
	        mode:'local',
	     	triggerAction:'all',
	        forceSelection:true,
	        editable:false,
	        store:new Ext.data.Store({     
	            data:[['false','正常'],['true','禁用']], 
	            autoLoad: true,
	            reader:new Ext.data.ArrayReader({}, [
	                  {name: 'id'},
	                  {name: 'mc'} 
				])
	        }),
	        listeners:{
        		'change':{fn:function(t,valuenew,valueold){
					this.store.baseParams['disabled']=valuenew;
        		},scope:this}
        	}
	    });
	    this.store.baseParams['disabled']=this.state.getValue();
		
		this.sm = new Ext.grid.CheckboxSelectionModel({
	    	dataIndex:'select',
	    	singleSelect:false
	    });
	
        this.items = [
            {
                xtype: 'panel',
                region: 'center',
                margins: '0 0 0 0',
                layout: 'fit',
                items: [
                    {
                        xtype: 'editorgrid',
                        clicksToEdit:1,
                        sm: this.sm,
                        border: false,
                        loadMask: true,
                        trackMouseOver: false,
                        animCollapse: false,
                        store: this.store,
                        sm: this.sm,
                        viewConfig: {
                            forceFit: true
                        },                        
                        id: 'notifygrid',
                        columns: [
                            this.sm,
                            {
                                xtype: 'gridcolumn',
                                dataIndex: 'code',
                                header: '编码',
                                sortable: true,
                                width: 120,
                                editor: new Ext.form.TextField()
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '名称',
                                sortable: true,
                                hidden: false,
                                width: 100,
                                dataIndex: 'name'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '描述',
                                sortable: true,
                                width: 60,
                                dataIndex: 'description'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '发布时间',
                                sortable: true,
                                width: 60,
                                dataIndex: 'date',
                                renderer:{fn:function(value,metadata,record){
                                	if(null!=value&&''!=value){
                                		var d1 = new Date(Date.parse(value.replace(/-/g, "/"))).getTime();
                                		var cur = new Date().getTime();
                                		if(d1>cur-1000*60*60*24*3)
                                			return "<font color=red>"+value+"</font>";
                                	}
                                	
	          		    	  		return value;
	          		      		},scope:this}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '禁用',
                                sortable: true,
                                width: 40,
                                hidden: true,
                                dataIndex: 'disabled',
                                renderer:function(value){if('0'==value){return "否";}else{return "是";}}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '文档',
                                sortable: false,
                                width: 50,
                                dataIndex: 'address',
                                renderer:{fn:function(value,metadata,record){
                            		var isEmpty = record.get("empty");
                            		if(isEmpty)
                            			return '';
                            		else{
                            			return '<a href="'+value+'" target="_blank"><font color=blue>查看文档</font></a>';
                            		}
	          		    	  			
	          		      		},scope:this}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '上传编号',
                                sortable: true,
                                width: 50,
                                dataIndex: 'selected',
                                hidden: true,
                                renderer:function(value){
                            		if(null!=value&&undefined!=value.split("]")[3])
                                		return value.split("]")[3].substring(1);
                            		else
                            			return '';
                            	}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '附件',
                                sortable: false,
                                width: 50,
                                dataIndex: 'code',
                                renderer:{fn:function(value,metadata,record){
                            		var isEmpty = record.get("extraEmpty");
                            		if(isEmpty)
                            			return '';
                            		else
	          		    	  			return '<a href="notify.do?cmd=extrafile&code='+value+'" target="_blank"><font color=blue>查看附件</font></a>';
	          		      		},scope:this}
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
                            	this.state,
                            	{
	                                xtype: 'spacer',
	                                width: 6
                            	},
                            	{
                                    xtype: 'button',
                                    text: '添加',
                                    pressed: true,           
                                    handler: this.add_notify,
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
                                    handler: this.edit_notify,
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
                                    xtype: 'spacer',
                                    width: 3
                                },
                                {
                                    xtype: 'button',
                                    text: '刷新',
                                    pressed: true,           
                                    handler: function(){this.store.reload();},
                                    scope:this
                                },
                                {
                                    xtype: 'spacer',
                                    width: 3
                                },'->','查找: ', ' ',
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
        NotifyPanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('notifygrid');
			this.store.load();			
		},this);
    }
});