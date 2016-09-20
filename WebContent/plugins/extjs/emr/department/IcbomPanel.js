IcbomPanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'icbompanel',
    initComponent: function() {
		/*this.store = new Ext.data.JsonStore({
			url: 'department.do?cmd=icbomhild',
			root:"result",
			fields:["FNumber","FName","FModel","FHelpCode","FQty"],
			listeners:{
				'beforeload': function(storeThis,option){
					storeThis.removeAll();
				}
			},
			baseParams:{pageSize:2000}
		});*/
    	
    	this.store = new Ext.data.JsonStore({
			url: 'employee.do?cmd=list',
			root:"result",
			fields:["id","code","name","fileClass","auxCode","disabled","description","address","department","departmentCode","departmentId","empty","selected","version","extraEmpty","cite","date","checked","checkDate","checker","departmentModel"],
			listeners:{
				'beforeload': {fn:function(storeThis,option){
					storeThis.removeAll();
					storeThis.baseParams.organizationId = this.organizationId;
					storeThis.baseParams.departmentId = this.departmentId;					
				},scope:this}
			},
			baseParams:{pageSize:2000}
		});
    	
    	this.store.baseParams['disabled']='false';
		
		this.organizationId = 1;
		this.departmentId = 0;
		
		this.departmentStore = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","tuhao","name","code"],
			baseParams:{notNullField:'tuhao',pageSize:200}
		});
		
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
		
		this.sm = new Ext.grid.CheckboxSelectionModel({
	    	dataIndex:'select',
	    	singleSelect:false
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
					    name: 'departmentselect',
					    valueField: 'id',
					    displayField: 'tuhao',
					    emptyText: '请选择物料...',
					    mode: 'remote',
					    store: this.departmentStore,
					    triggerAction: 'all',
					    listeners: {
							'select':{fn:function(combo,newValue,oldValue){
								this.departmentId = newValue.data.id;
								this.tree.getRootNode().id = this.departmentId;
								this.tree.getRootNode().setText(newValue.data.name);
								this.store.load();
								this.tree.getLoader().load(this.tree.getRootNode());
							},scope:this}
						}
					},
                    {
                        xtype: 'treepanel',
                        id: 'icbomtreepanel',	
                        border: false,
                        root: {
                            text: '物料',
                            id: '0'                      //分别表示机构编号、部门编号
                        },
                        loader: {
                            url: 'department.do?cmd=icbomtree',                            
                            listeners: {
                        		'beforeload':function(loader,node){
	    							loader.baseParams.departmentId=node.id;
	    						}
                        	}
                        },
                        listeners: {                        	
                        	'click':{fn:function(node,e){
	                			this.departmentId = node.id;	                			
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
                        id: 'icbomgrid',
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
                                header: '所属物料',
                                sortable: false,
                                width: 100,
                                dataIndex: 'department'/*,
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}*/
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '文件类型',
                                sortable: true,
                                width: 100,
                                dataIndex: 'fileClass',
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '名称',
                                sortable: true,
                                hidden: true,
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
                                header: '更新时间',
                                sortable: true,
                                width: 60,
                                dataIndex: 'date',
                                renderer:{fn:function(value,metadata,record){
                                	if(null!=value&&''!=value){
                                		var d1 = new Date(Date.parse(value.replace(/-/g, "/"))).getTime();
                                		var cur = new Date().getTime();
                                		if(d1>cur-1000*60*60*24*3)
                                			return "<font color=red>"+value+"</font>"
                                	}
                                	
	          		    	  		return value;
	          		      		},scope:this}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '规格型号',
                                sortable: false,
                                width: 100,
                                hidden: true,
                                dataIndex: 'departmentModel'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '物料代码',
                                sortable: false,
                                width: 100,
                                hidden: true,
                                dataIndex: 'departmentCode'
                            },                            
                            {
                                xtype: 'gridcolumn',
                                header: '工位',
                                sortable: false,
                                width: 60,
                                dataIndex: 'department',
                                renderer:{fn:function(value,metadata,record){
	          		    	  		return '<a href="javascript:frame.openjoinrolewindow('+record.get("id")+')"><font color=blue>查看工位</font></a>';
	          		      		},scope:this}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '审核',
                                sortable: true,
                                width: 40,
                                dataIndex: 'checked',
                                renderer:function(value){if('0'==value){return "否";}else{return "是";}}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '审核人',
                                sortable: true,
                                width: 40,
                                dataIndex: 'checker'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '审核时间',
                                sortable: true,
                                width: 40,
                                dataIndex: 'checkDate'
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
                                header: '引用',
                                sortable: true,
                                width: 40,
                                dataIndex: 'cite'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '文档',
                                sortable: false,
                                width: 50,
                                dataIndex: 'departmentCode',
                                renderer:{fn:function(value,metadata,record){
                            		var isEmpty = record.get("empty");
                            		if(isEmpty)
                            			return '';
                            		else{
                            			var cite = record.get('cite');
                            			if(null==cite || ''==cite){
                            				var code = record.get('code');
                            				var serial = code.substr(code.lastIndexOf('.')+1,code.length);
                            				return '<a href="employee.do?cmd=search&department='+value+'&fileClass='+record.get('fileClass').id+'&serial='+serial+'" target="_blank"><font color=blue>查看文档</font></a>';
                            			}else{
                            				var index = cite.lastIndexOf('.');
                            				var fileClass = cite.substr(index-5,5);
                            				var department = '';
                            				if(cite.lastIndexOf(fileClass)==cite.indexOf(fileClass))     //AB7001EL.02.02.029879 : department=AB7001EL.02 
                            					department = cite.substr(0,cite.lastIndexOf(fileClass)-1);
                            				else
                            					department = cite.substr(0,cite.indexOf(fileClass)-1);
                            				var serial = cite.substr(index+1,cite.length);
                            				return '<a href="employee.do?cmd=search&department='+department+'&fileClass='+fileClass+'&serial='+serial+'" target="_blank"><font color=blue>查看文档</font></a>';
                            			}
                            		}
	          		    	  			
	          		      		},scope:this}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '上传编号',
                                sortable: true,
                                width: 50,
                                dataIndex: 'selected',
                                hidden: false,
                                renderer:function(value){
                            		if(null!=value&&undefined!=value.split("]")[3])
                                		return value.split("]")[3].substring(1);
                            		else
                            			return '';
                            	}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '版本',
                                sortable: false,
                                width: 50,
                                dataIndex: 'departmentCode',
                                hidden: true,
                                renderer:{fn:function(value,metadata,record){
                            		/*var isEmpty = record.get("empty");
                            		if(isEmpty)
                            			return '';
                            		else
	          		    	  			return '<a href="employee.do?cmd=history&department='+value+'&fileClass='+record.get('fileClass').id+'" target="_blank"><font color=blue>历史版本</font></a>';*/
                                	
                                	var isEmpty = record.get("empty");
                            		if(isEmpty)
                            			return '';
                            		else{
                            			var cite = record.get('cite');
                            			if(null==cite || ''==cite){
                            				var code = record.get('code');
                            				var serial = code.substr(code.lastIndexOf('.')+1,code.length);
                            				return '<a href="employee.do?cmd=history&department='+value+'&fileClass='+record.get('fileClass').id+'&serial='+serial+'" target="_blank"><font color=blue>历史版本</font></a>';
                            			}else{
                            				var index = cite.lastIndexOf('.');
                            				var fileClass = cite.substr(index-5,5);
                            				var department = cite.substr(0,cite.lastIndexOf(fileClass)-1);
                            				var serial = cite.substr(index+1,cite.length);
                            				return '<a href="employee.do?cmd=history&department='+department+'&fileClass='+fileClass+'&serial='+serial+'" target="_blank"><font color=blue>历史版本</font></a>';
                            			}
                            		}
	          		      		},scope:this}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '附件',
                                sortable: false,
                                width: 50,
                                dataIndex: 'departmentCode',
                                renderer:{fn:function(value,metadata,record){
                            		var isEmpty = record.get("extraEmpty");
                            		if(isEmpty)
                            			return '';
                            		else
	          		    	  			return '<a href="employee.do?cmd=extrafile&code='+record.get('code')+'" target="_blank"><font color=blue>查看附件</font></a>';
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
        IcbomPanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('icbomgrid');
        	this.tree = Ext.getCmp('icbomtreepanel');	
			this.store.load();
		},this);
    }
});
