DynamicGrid = Ext.extend(Ext.grid.GridPanel,{
	viewConfig:{forceFit:true},
	loadMask: true,
    trackMouseOver: false,
    animCollapse: false,
	initComponent : function(){
		Ext.Ajax.request({
			url:this.module.url,
			params:this.module.params,
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
				
				this.columns = new Ext.grid.ColumnModel({
			        columns : cols
			    });
				
				var filesName = [];
			    for (var j=0;j<gridhead.length;j++) {
			    	filesName.push(gridhead[j].dataIndex);
			    }
			    
			    this.store =  new Ext.data.JsonStore({
			        data : responseArray[0],
			        fields : filesName
			    });
			},
			scope:this
		});
		DynamicGrid.superclass.initComponent.call(this);
		this.on('render',function(comp){
			alert('end');
		},this);
	}
});