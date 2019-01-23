RssPanel = Ext.extend(Ext.Viewport, {
    layout: 'fit',
    id: 'rsspanel',
    initComponent: function() {
		this.store = new Ext.data.JsonStore({
			url: 'notify.do?cmd=list',
			root:"result",
			fields:["id","code","name","auxCode","disabled","description","address","empty","selected","extraEmpty","date","checked","checkDate","checker"],
			listeners:{
				'beforeload': {fn:function(storeThis,option){
					storeThis.removeAll();
				},scope:this}
			},
			baseParams:{pageSize:200}
		});
		
		this.queryfield = new Ext.app.SearchField({
            store: this.store,
            width:220,
            emptyText:'请输入关键字...',
            scope: this
        });
		
	    this.store.baseParams['disabled']='false';
	
        this.items = [
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
			    id: 'rssgrid',
			    columns: [
			        {
			            xtype: 'gridcolumn',
			            dataIndex: 'code',
			            header: '编码',
			            sortable: true,
			            width: 120
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
			        items: [
			            '技术通知',
			            {
			                xtype: 'spacer',
			                width: 3
			            },
			            '->','查找: ', ' ',
				         	this.queryfield
			        ]
			    },
			    bbar: {
			        xtype: 'paging',
			        pageSize: 200,
			        store: this.store,
			        displayInfo: true,
			        displayMsg: '共{2}条记录，当前第 {0}条 到 {1}条',
			        emptyMsg: '没有找到记录'
			    }
			}
        ];
        RssPanel.superclass.initComponent.call(this);
        
        this.on('render',function(t){        	
        	this.gp = Ext.getCmp('rssgrid');
			this.store.load();			
		},this);
    }
});