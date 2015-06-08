SelectDepartmentWindow = Ext.extend(Ext.Window, {
    width: 493,
    height: 371,
    layout: 'border',    
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
				},scope:this}
			}
		});
		
		this.departmentStore = new Ext.data.JsonStore({
			url: 'department.do?cmd=list',
			root:"result",
			fields:["id","name"]
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
                        title: '所属部门',
                        columnWidth: 0.5,
                        viewConfig: "forceFit: true",
                        store: this.store,
                        height: 280,
                        width: 154,
                        columns: [
                            {
                                xtype: 'gridcolumn',
                                dataIndex: 'string',
                                header: 'Column',
                                sortable: true,
                                width: 200
                            }
                        ]
                    },
                    {
                        xtype: 'grid',
                        title: '组织架构',
                        columnWidth: 0.5,
                        height: 280,
                        store: this.departmentStore,
                        viewConfig: "forceFit: true",
                        width: 237,
                        columns: [
                            {
                                xtype: 'gridcolumn',
                                dataIndex: 'string',
                                header: 'Column',
                                sortable: true,
                                width: 200
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
						      alert('提交');
					   	  },
					   	  scope:this
					  },
					  {
						  text:"重置",
						  handler:function(){
						      alert('重置');
					   	  },
					   	  scope:this
					  },
					  {
						   text:"取消",
						   handler:function(){
						       alert('取消');  
					   	   },
					       scope:this
					   }
				]
            }
        ];
        SelectDepartmentWindow.superclass.initComponent.call(this);
    }
});
