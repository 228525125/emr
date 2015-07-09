EmployeePanel = Ext.extend(Ext.Viewport, {
    layout: 'border',
    id: 'employeepanel',
    createForm:function(){
		if(!this.fp||null==this.fp){
			this.fp = new EmployeeFormPanel({
				fileClassStore : this.fileClassStore
			});
			if(0!=this.departmentId){
				this.fp.form.findField("departmentName").setValue(this.departmentName);
				this.fp.form.findField("department").setValue(this.departmentId);
			}
		}
		if(!this.win||null==this.win){
			this.win = new Ext.Window({
				width:360,
				height:400,
				layout:'fit',
				buttonAlign:"center",
				title:'编辑文件信息',
				modal:true,
				shadow:true,
				closeAction:"close",
				items:[this.fp],
				buttons:[{text:"保存",
						  handler:function(){
							this.fp.form.submit({
									waitMsg:'正在保存。。。',
						            url:'upload.do',
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
	add_employee:function(){		
		if(this.departmentId==0||this.departmentId==-1)
			Ext.Msg.alert("提示","请先选择物料!");
		else{
			this.createForm();
			this.win.show();
		}
	},
	edit_employee:function(){
		var record=this.gp.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示","请先选择要编辑的行!");
			return;
		}
			
		this.createForm();
		this.win.show();
		this.fp.form.loadRecord(record);		
		//============对combo类型的字段赋值==============//
		
		/* department不是对象，而是一个departmentName */ 
		if(record.get("department")&&null!=record.get("department")){
			this.fp.form.findField("departmentName").setValue(record.get("department"));
		}
			
		if(record.get("departmentId")&&null!=record.get("departmentId")){
			this.fp.form.findField("department").setValue(record.get("departmentId"));				
		}
		this.fp.form.findField("fileClass").setValue(record.get("fileClass").id);	
	},
    initComponent: function() {
		this.store = new Ext.data.JsonStore({
			url: 'employee.do?cmd=list',
			root:"result",
			fields:["id","code","name","fileClass","auxCode","disabled","description","address","department","departmentCode","departmentId","empty","selected","version","extraEmpty","cite"],
			listeners:{
				'beforeload': {fn:function(storeThis,option){
					storeThis.removeAll();
					storeThis.baseParams.organizationId = this.organizationId;
					storeThis.baseParams.departmentId = this.departmentId;					
				},scope:this}
			},
			baseParams:{pageSize:2000}
		});
		
		this.departmentStore = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","tuhao","name","code"],
			baseParams:{notNullField:'tuhao',pageSize:200}
		});
		
		this.departmentName='';
		this.departmentId = 0;		
		this.organizationId = 1;
		
		this.fileClassStore = new Ext.data.JsonStore({
			url: 'fileClass.do?cmd=selectList',
			root:"result",
			fields:["id","name"],
			baseParams:{pageSize:100}
		});
		
		this.queryfield = new Ext.app.SearchField({
            store: this.store,
            width:220,
            emptyText:'请输入关键字...',
            scope: this
        });
		
		this.root = new Ext.tree.AsyncTreeNode({
			text: '组织架构',
            id: '0',                      //分别表示机构编号、部门编号   	
			expanded:true
		});
		
		this.sm = new Ext.grid.CheckboxSelectionModel({
	    	dataIndex:'select',
	    	singleSelect:true
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
								this.tree.getRootNode().id = this.organizationId+','+this.departmentId;  //重置组织架构tree重置
								this.tree.getRootNode().setText("("+newValue.data.code+")"+newValue.data.name);
								this.tree.getLoader().load(this.tree.getRootNode());
								this.store.load();								  //加载新的机构职员
							},scope:this}
						}
					},				
                    {
                        xtype: 'treepanel',
                        id: 'departmenttreepanel',	
                        border: false,
                        root: {
                            text: '物料',
                            id: '1,0'                      //分别表示机构编号、部门编号
                        },
                        loader: {                        
                            url: 'department.do?cmd=tree',                            
                            listeners: {
                        		'beforeload':{fn:function(loader,node){                       
	    							loader.baseParams.organizationId=node.id.split(",")[0];
	    							loader.baseParams.departmentId=node.id.split(",")[1];	    							
	    						},scope:this}
                        	}
                        },
                        listeners: {                        	
                        	'click':{fn:function(node,e){
	                			this.organizationId = node.id.split(",")[0];
	                			this.departmentId = node.id.split(",")[1];	                			
	                			this.departmentName = node.text;
	                			this.queryfield.onTrigger1Click();
	                			if(0!=this.departmentId)                     //避免用户点击根节点，因为根节点为机构，node.id为*,0，这样就会过滤掉所有职员信息
	                				this.store.load();                       //刷新右边的内容
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
                        xtype: 'editorgrid',
                        clicksToEdit:1,
                        sm: this.sm,
                        border: false,
                        loadMask: true,
                        trackMouseOver: false,
                        animCollapse: false,
                        store: this.store,
                        viewConfig: {
                            forceFit: true
                        },                        
                        id: 'employeegrid',
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
                                width: 200,
                                dataIndex: 'name'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '描述',
                                sortable: true,
                                width: 150,
                                dataIndex: 'description'
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '文件类型',
                                sortable: true,
                                hidden: true,
                                width: 50,
                                dataIndex: 'fileClass',
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}
                            },
                            {
                                xtype: 'gridcolumn',
                                header: '所属物料',
                                sortable: false,
                                width: 100,
                                hidden: true,
                                dataIndex: 'department'/*,
                                renderer:function(value){if(value&&null!=value){return value.name;}else{return '';}}*/
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
                                width: 120,
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
                            			if(null==cite || ''==cite)
                            				return '<a href="employee.do?cmd=search&department='+value+'&fileClass='+record.get('fileClass').id+'" target="_blank"><font color=blue>查看文档</font></a>';
                            			else{
                            				var department = cite.substr(0,cite.indexOf('.'));
                            				var fileClass = cite.substr(cite.indexOf('.')+1,5);
                            				return '<a href="employee.do?cmd=search&department='+department+'&fileClass='+fileClass+'" target="_blank"><font color=blue>查看文档</font></a>';
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
                                header: '版本',
                                sortable: false,
                                width: 50,
                                dataIndex: 'departmentCode',
                                hidden: true,
                                renderer:{fn:function(value,metadata,record){
                            		var isEmpty = record.get("empty");
                            		if(isEmpty)
                            			return '';
                            		else
	          		    	  			return '<a href="employee.do?cmd=history&department='+value+'&fileClass='+record.get('fileClass').id+'" target="_blank"><font color=blue>历史版本</font></a>';
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
                                    text: '添加',
                                    pressed: true,           
                                    handler: this.add_employee,
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
                                    handler: this.edit_employee,
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
                                },
                                {
                                    xtype: 'spacer',
                                    width: 3
                                },
                                {
                                    xtype: 'button',
                                    text: '批量添加',
                                    pressed: true,           
                                    handler: function(){                                		
                                		post({
                                			url: 'employee.do?cmd=batchAddFile',
                                			params: {departmentId:this.departmentId},
                                			loadmask:true,
                                			compId: 'employeegrid',
                                			waitText:'正在处理.....',
                                			store: this.store
                                		});
                                	
                                		//this.store.reload();
                                	},
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
        EmployeePanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('employeegrid');
        	this.tree = Ext.getCmp('departmenttreepanel');	
        	this.departmentCombo = Ext.getCmp('departmentselect');        	
			this.store.load();			
			//this.departmentStore.load();
			this.fileClassStore.load();
		},this);
    }
});
