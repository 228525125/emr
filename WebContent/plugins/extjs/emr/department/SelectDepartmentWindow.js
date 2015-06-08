SelectDepartmentWindow = Ext.extend(Ext.Window, {
    width: 493,
    height: 371,
    layout: 'border',
    closeAction:"close",
    modal:true,
    initComponent: function() {
		this.store = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","code","name","auxCode","disabled","description"],
			listeners:{
				'beforeload': {fn:function(storeThis,option){
					storeThis.removeAll();
					storeThis.baseParams.employeeId = this.employeeId;
				},scope:this},
				'load': {fn:function(storeThis,records,option){
					for(var i=0;i<records.length;i++){
						records[i].id = records[i].get('id');
					}
				},scope:this}
			}
		});
		
		this.departmentStore = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","name"],
			listeners:{
				'load': {fn:function(storeThis,records,option){
					for(var i=0;i<records.length;i++){
						records[i].id = records[i].get('id');
					}
				},scope:this}
			}
		});
		
		this.departmentStore.on('beforeload',function(storeThis,option){
			storeThis.removeAll();
			storeThis.baseParams.organizationId = this.organizationId;
			storeThis.baseParams.departmentId = this.departmentId;			
		},this);
		
        this.items = [
            {
                xtype: 'panel',
                region: 'center',
                margins: '10 10 10 10',
                border: false,
                layout: 'column',
                items: [
                    {
                        xtype: 'grid',
                        id:'departmentfirstgrid',
                        title: '已选物料',
                        enableDragDrop: true,
                        stripeRows: true,
                        ddGroup: 'secondGridDDGroup',
                        columnWidth: 0.5,
                        viewConfig: "forceFit: true",
                        store: this.store,
                        height: 280,
                        width: 154,
                        columns: [
                            {
                                xtype: 'gridcolumn',
                                dataIndex: 'name',
                                header: '名称',
                                sortable: true,
                                width: 200
                            },
                            {
                            	xtype: 'gridcolumn',
                            	dataIndex: 'code',
                            	sortable: true,
                            	hidden: true
                            	//width: 0
                            }
                        ]
                    },
                    {
                        xtype: 'grid',
                        id:'departmentsecondgrid',
                        title: '可选物料',
                        ddGroup: 'firstGridDDGroup',
                        enableDragDrop: true,
                        stripeRows: true,
                        columnWidth: 0.5,
                        height: 280,
                        store: this.departmentStore,
                        viewConfig: "forceFit: true",
                        width: 237,
                        columns: [
                            {
                                xtype: 'gridcolumn',
                                dataIndex: 'name',
                                header: '名称',
                                sortable: true,
                                width: 200
                            },
                            {
                            	xtype: 'gridcolumn',
                            	dataIndex: 'code',
                            	sortable: true,
                            	hidden: true
                            	//width: 0
                            }
                        ]
                    }                    
                ],
                /*bbar: [             			
          			{
          				text    : '>>>>>>',
          				handler : function() {
          					
          				}
          			},
          			'->', // Fill
          			{
          				text    : '<<<<<<',
          				handler : function() {
          					
          				}
          			}
          		],*/
          		buttons:[
					  {
						  text:"提交",
						  handler:function(){
						  	  var result = 0;                             //0表示为null
					    	  for(var i=0;i<this.store.getCount();i++){
					    		 var record = this.store.getAt(i);
					    		 result += record.get('id');
					    		 if(i<this.store.getCount()-1)
					    			 result += ',';					    		 
					    	  }
					    	  
					    	  var mask = new Ext.LoadMask(this.id, {
					 		      msg : "正在提交..."
					    	  });
					    	  mask.show();
					    	  
					    	  Ext.Ajax.request({
					    			url:'employee.do?cmd=updateDepartment',
					    			params:{departments:result,employeeId:this.employeeId},
					    			method:'POST',
					    			success:function(response, options){
					    				if(mask)
					    					 mask.hide();
					    				var responseArray = Ext.util.JSON.decode(response.responseText); 
					    				var resp = responseArray.result[0];
					    				 if(resp.success==true){
					    					 if(resp.msg)
					    						 Ext.Msg.alert('提示',resp.msg,function(btn, text){
					    							 this.close();
					    						 },this);
					    					 else
					    						 Ext.Msg.alert('提示','处理完毕!',function(btn, text){
					    							 this.close();
					    						 },this);
					    				 }else{
					    					 Ext.Msg.alert('提示','服务器没有响应，请稍后再试！');
					    				 }
					    			},
					    			scope:this
					    		});
					   	  },
					   	  scope:this
					  },
					  {
						  text:"重置",
						  handler:function(){
						      this.store.removeAll();
					   	  },
					   	  scope:this
					  },
					  {
						   text:"取消",
						   handler:function(){
						       this.close();
					   	   },
					       scope:this
					   }
				]
            }
        ];
        SelectDepartmentWindow.superclass.initComponent.call(this);              
        
        this.on('show',function(t){   
        	this.firstGrid = Ext.getCmp('departmentfirstgrid');
            this.secondGrid = Ext.getCmp('departmentsecondgrid');
            
            var firstGridDropTargetEl =  t.firstGrid.getView().scroller.dom;
            var firstGridDropTarget = new Ext.dd.DropTarget(firstGridDropTargetEl, {
                ddGroup    : 'firstGridDDGroup',
                notifyDrop : function(ddSource, e, data){
                        var records =  ddSource.dragData.selections;
                        Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);                        
                        for(var i=0;i<records.length;i++){                           //这里必须先将firstGrid.store里的重复记录删除掉，否则会报错
                        	var index = t.firstGrid.store.indexOfId(records[i].id);
                        	t.firstGrid.store.remove(t.firstGrid.store.getAt(index));                        	
                        }
                        t.firstGrid.store.add(records);
                        t.firstGrid.store.sort('name', 'ASC');
                        return true;
                }
            });
            
            
            var secondGridDropTargetEl = t.secondGrid.getView().scroller.dom;
            var secondGridDropTarget = new Ext.dd.DropTarget(secondGridDropTargetEl, {
                ddGroup    : 'secondGridDDGroup',
                notifyDrop : function(ddSource, e, data){
                        var records =  ddSource.dragData.selections;
                        Ext.each(records, ddSource.grid.store.remove, ddSource.grid.store);
                        for(var i=0;i<records.length;i++){                //这里必须先将secondGrid.store里的重复记录删除掉，否则会报错
                        	var index = t.secondGrid.store.indexOfId(records[i].id);
                        	t.secondGrid.store.remove(t.secondGrid.store.getAt(index));                        	
                        }
                        t.secondGrid.store.add(records);
                        t.secondGrid.store.sort('name', 'ASC');
                        return true;
                }
            });
            
			this.store.load();
			this.departmentStore.load();
		},this);
    }
});