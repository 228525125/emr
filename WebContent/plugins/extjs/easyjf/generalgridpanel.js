Ext.namespace("Ext.ux");

Ext.ux.GeneralGridPanel=Ext.extend(Ext.Panel,{
	closable: true,
  	autoScroll:true,
  	layout:"fit",
  	gridViewConfig:{},   
  	linkRenderer:function(v)
  	{
  		if(!v)return "";
  		else return String.format("<a href='{0}' target='_blank'>{0}</a>",v);
  	},
    search:function()
    {    
    },
    refresh:function()
    {
    	//this.store.removeAll();
   		this.store.reload();
    },    
    initWin:function(width,height,title)
    {
    	var win=new Ext.Window({
			width:width,
			height:height,
			buttonAlign:"center",
			title:title,
			layout:'fit',
			modal:true,
			shadow:true,
			closeAction:"close",
			items:[this.fp],
			buttons:[{text:"保存",
					  handler:this.save,
					  scope:this},
					  {text:"清空",
					   handler:this.reset,
					   scope:this},
					  {text:"取消",
					   handler:this.closeWin,
					   scope:this}
					   	]					  
		});
		return win;
    },
    showWin:function()
	{	
		if(!this.win||null==this.win){
			if(!this.fp||null==this.fp){
				this.fp=this.createForm();
			}
		this.win=this.createWin();
		this.win.on("close",function(){this.win=null;this.fp=null;},this);
		}
		this.win.show();
	},
	create:function()
	{
		this.showWin();
		this.reset();
	},
	save:function()
	{	
		this.fp.form.submit({
				waitMsg:'正在保存。。。',
	            url:this.saveUrl,
	            method:'POST',
	            success:function(form,action){
					if(null!=action.result&&action.result.msg)
		        		Ext.Msg.alert('提示',action.result.msg,function(){
		        			this.closeWin();
				           	this.store.reload(); 
		        		},this);
					else{
			           	this.closeWin();
			           	this.store.reload();
					}
	            },
	            failure:function(form,action){
	            	if(null!=action.result&&action.result.msg)
		        		Ext.Msg.alert('提示',action.result.msg,function(){
		        			this.closeWin();
				           	this.store.reload(); 
		        		},this);
					else{
			           	this.closeWin();
			           	this.store.reload();
					}
	            },
	            scope:this
		});	
	},
	reset:function()
	{
	if(this.win)this.fp.form.reset();
	},
	closeWin:function()
	{
		if(this.win)this.win.close();
		this.win=null;
	},
	edit:function()
	{
		var record=this.grid.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示","请先选择要编辑的行!");
			return;
		}
	    var id=record.get(this.up);
	    this.showWin();
	    this.fp.form.loadRecord(record); 
	},	
	removeData:function()
	{
			var record=this.grid.getSelectionModel().getSelected();
			if(!record){
				Ext.Msg.alert("提示","请先选择要编辑的行!");
				return;
			}
			var m=Ext.MessageBox.confirm("删除提示","是否真的要删除数据？",function(ret){
			if(ret=="yes"){	
			  Ext.Ajax.request({
	            url:this.deleteUrl,
	            params:{
	                'id':record.get(this.up)
	            },
	            method:'POST',
	            success:function(response){
	            	var resp = Ext.util.JSON.decode(response.responseText); 
	    			if(resp.data.msg)
	    				Ext.Msg.alert('提示',resp.data.msg,function(){
		            		this.store.reload();	
		            	},this);
		   			else
		   				Ext.Msg.alert("提示信息","成功删除数据!",function(){
				            this.store.reload();	
				        },this);
	            },
	            scope:this
			  });
			}},this);
	},
    initComponent : function(){   
       this.store=new Ext.data.JsonStore({
		id:"id",
       	url: this.storeUrl,
       	root:"result",
  		totalProperty:"rowCount",
  		fields:this.storeMapping,
  		remoteSort:true,
  		baseParams:{pageSize:pgSize},
  		listeners:{
			'load':validate
	   	}
   		});
      	this.store.paramNames.sort="orderBy";
	 	this.store.paramNames.dir="orderType";	  
      	this.cm.defaultSortable=true;   	  	
      	Ext.ux.GeneralGridPanel.superclass.initComponent.call(this);
        var viewConfig=Ext.apply({forceFit:true},this.gridViewConfig);  
        this.grid=new Ext.grid.GridPanel({
        store: this.store,
        cm: this.cm,
        trackMouseOver:false,    
        loadMask: true,
        border:false,
        viewConfig:viewConfig,
        plugins: this.expander,
        tbar: ['   ',
       		 {    
                text: '添加',  
                pressed: true,           
                handler: this.create,
                scope:this
            },'   ',
            {    
                text: '修改',  
                pressed: true,            
                handler: this.edit,
                scope:this
            },'   ',
            {    
                text: '删除',  
				pressed: true,           
                handler: this.removeData,
                scope:this
            },'   ',
             {    
                text: '刷新',  
				pressed: true,           
                handler: this.refresh,
                scope:this
            }
            ,new Ext.Toolbar.Fill(),
            '查找: ', ' ',
	         new Ext.app.SearchField({
	              store: this.store,
	              width:320,
	              emptyText:'请输入商品标题的关键字',
	              scope: this
	         })
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: pgSize,
            store: this.store,
            displayInfo: true,
            displayMsg: '共{2}条记录，当前第 {0}条 到 {1}条',
            emptyMsg: "没有找到记录"
        })
   		});   		   		
       
   		this.add(this.grid);
        }
});