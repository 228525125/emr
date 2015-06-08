/**
 * url      Ajax请求地址               (必须)
 * params   Ajax请求参数(totalSQL,querySQL,isCallable,queryFields,qType)               (必须)
 * title    表格标题
 * id       表格id
 * region   表格布局参数
 * applyTo  表格applyTo参数
 * 
 */
createDynamicGrid = function (config){	
	Ext.Ajax.request({
		url:config.url,       
		params:config.params,   
		method:'POST',
		success:function(response, options){				
			var responseArray = Ext.util.JSON.decode(response.responseText); 
			var gridhead = responseArray[1];
			var cols = [];
			cols.push(new Ext.grid.RowNumberer());
			
			for(var i=0;i<gridhead.length;i++){					
				gridhead[i].menuDisabled = true;
				gridhead[i].sortable = true;
				cols.push(gridhead[i]);					
			}

			var cm = new Ext.grid.ColumnModel({
		        columns : cols
		    });							

			var filesName = [];
		    for (var j=0;j<gridhead.length;j++) {
		    	filesName.push(gridhead[j].dataIndex);
		    }

		    //因为是动态生成表格，所以记录最后生成一条表头信息，这里应该在总记录中减去这条表头记录才对
		    /*var currentPage = responseArray[0].currentPage;
		    var nextPage = responseArray[0].nextPage;
		    int pages = responseArray[0].pages;
			int pageSize = responseArray[0].pageSize;
			int previousPage = responseArray[0].previousPage;
			int rowCount = responseArray[0].rowCount;

			responseArray[0].rowCount -= 1;
			responseArray[0].pages = rowCount/pageSize;
			if(currentPage*pageSize>rowCount){
				responseArray[0].currentPage -= 1;
				if(responseArray[0].currentPage>1){
					responseArray[0].previousPage -= 1;
				}
				if(nextPage>1)
					responseArray[0].nextPage -= 1;					
			}*/
		    
		    //自定义查询窗口
		    /*var querywin = new QueryWindow({
				width:380,
				height:300,				
				parentStore:this.store,
				conditionsURL:'person.do?cmd=getConditions',
				initDatas:[new Row({
					join:'并且-and',
		        	item:'身份证号-idCardNo-String',
		        	expression:'等于-=',
		        	value:''
	    		}),new Row({
	    			join:'并且-and',
		        	item:'居民姓名-name-String',
		        	expression:'等于-=',
		        	value:''
	        	}),new Row({
	    			join:'并且-and',
		        	item:'所属网格-dist-District',
		        	expression:'包含-like',
		        	value:''
		    	}),new Row({
					join:'并且-and',
		        	item:'通讯地址-addr-String',
		        	expression:'包含-like',
		        	value:''
		    	})]
			});*/
			
			
		    var store =  new Ext.data.JsonStore({
		    	url : config.url,
		    	baseParams: config.params,
		        data : responseArray[0],
		        fields : filesName,
		        root : "result",
		        listeners : {
					'beforeload' : {fn:function(storeThis,option){
		    			storeThis.baseParams.qType = "store";
						storeThis.removeAll();
		    		},scope:this}
				}
		    });

		    var searchField = new Ext.app.SearchField({
	            store: store,
	            width: 240,
	            emptyText: '请输入关键字'        
	        });

		    var grid = new Ext.grid.GridPanel({
		    	viewConfig : {forceFit:true},
		    	loadMask: true,
		        cm : cm,
		        title : config.title,
		        id : config.id, 
		        store : store,
		        autoScroll : true,
		        region : config.region,
		        applyTo : config.applyTo,
		        height : config.height,
		        width : config.width,
		        tbar: {
                    xtype: 'toolbar',
                    items: ['->',
                        searchField/*,
                        {
        	            	text:'自定义条件',
        	            	handler:function(){
                    			//querywin.show();
							},
        	            	scope:this
        		        }*/
                    ]
                },
		        bbar: {
                    xtype: 'paging',
                    pageSize: 22,
                    store: store,
                    displayInfo: true,
                    displayMsg: '共{2}条记录，当前第 {0}条 到 {1}条',
                    emptyMsg: '没有找到记录'
                }
		    });
		},
		scope:this
	});
}