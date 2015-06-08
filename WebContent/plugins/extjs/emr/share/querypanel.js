/**
 * 他必须接受一个parentStore，调用他的父模块的store
 */
QueryPanel = Ext.extend(Ext.Panel,{	
	layout:'fit',
	border:false,
	autoScroll:true,	
	initComponent : function(){		
		QueryPanel.superclass.initComponent.call(this);
		this.com = new Ext.grid.ColumnModel([
		    {
		    	 header: "",
				 dataIndex: 'join',
				 width: 50,
				 renderer:function(v){
		    		if(v&&null!=v)
		    			return v.split('-')[0];
		    		else
		    			return '';
		    	 },
				 editor: new Ext.form.ComboBox({					 	
						hiddenName:'join',
						valueField:'id',
				        displayField:'name',
				        emptyText:'请选择...',
				        width:200,
				        allowBlank:false,				        
				        mode:'local',
				     	triggerAction:'all',
				        forceSelection:true,
				        editable:false,
				        store:new Ext.data.Store({     
				            data:[['并且-and','并且'],['或者-or','或者']], 
				            autoLoad: true,
				            reader:new Ext.data.ArrayReader({}, [
				                  {name: 'id'},
				                  {name: 'name'} 
							])
				        })
				})             
		    },{		    	
 			    header: "项目",
 			    dataIndex: 'item',
 			    width: 120,
 			    renderer:function(v){
		    		if(v&&null!=v)
		    			return v.split('-')[0];
		    		else
		    			return '';
		    	},
 			    editor: new Ext.form.ComboBox({
 				    //tpl: '<tpl for="."><div class="x-combo-list-item">{FCapt}</div></tpl>',
 				    store: this.conditions,
 				    hiddenName:'item',
 				    allowBlank: false,
 				    displayField:'FCapt',
 				    valueField:'FValue',
 				    typeAhead: true,
 				    mode: 'local',
 				    triggerAction: 'all',
 				    emptyText:'请选择...',
 				    forceSelection:true
 				})   
		    },{		    
				 header: "表达式",
				 dataIndex: 'expression',
				 width: 50,
				 renderer:function(v){
		    		if(v&&null!=v)
		    			return v.split('-')[0];
		    		else
		    			return '';
		    	 },
				 editor: new Ext.form.ComboBox({					 	
						hiddenName:'expression',
						valueField:'id',
				        displayField:'name',
				        emptyText:'请选择...',
				        //width:200,
				        allowBlank:false,				        
				        mode:'local',
				     	triggerAction:'all',
				        forceSelection:true,
				        editable:false,
				        store:new Ext.data.Store({     
				            data:[['小于-<','小于'],['大于->','大于'],['小于等于-<=','小于等于'],['大于等于->=','大于等于'],['包含-like','包含'],['等于-=','等于'],['不等于-<>','不等于']], 
				            autoLoad: true,
				            reader:new Ext.data.ArrayReader({}, [
				                  {name: 'id'},
				                  {name: 'name'} 
							])
				        })
				})                				 
		    },{				 
 				 header: "值",
 				 dataIndex: 'value',
 				 width: 120,
 				 editor: new Ext.form.TextField({
 					 allowBlank: false
 			     }) 
 			 }
		]);
		/*var Row = Ext.data.Record.create([
		       {name: 'join', type: 'string'},
		       {name: 'item', type: 'string'},
		       {name: 'expression', type: 'string'},
		       {name: 'value', type: 'string'}		       
		]);*/
		this.store = new Ext.data.Store({
			reader: new Ext.data.ArrayReader({
				id:"rows"
	        }, Row)
		});

		this.conditions.add_btn = new Ext.Button({
			text:'添加条件',
			disabled:true,
			handler:function(){				
				var p = new Row({
					join:'并且-and',
                	item:'',
                	expression:'',
                	value:''                	
                });
				
                this.grid.stopEditing();
                this.store.insert(0, p);
                this.grid.startEditing(0, 0);
            },
            scope:this
		});
		this.grid = new Ext.grid.EditorGridPanel({	        
	        store: this.store,
	        cm:this.com,
	        border:false,
	        //autoExpandColumn:'cpph',
	        disableSelection:false,
	        viewConfig:{forceFit:true},
	        region:'center',
	        clicksToEdit:1,
	        tbar: [this.conditions.add_btn,
	               ' ',
	               {
	        	   		text:'删除条件',
	        	   		handler:function(){
	        				var model = this.grid.getSelectionModel();
	        				if(!model.hasSelection()){
	        					Ext.Msg.alert("提示","请先选择要编辑的行!");
	        					return;
	        				}
	        				Ext.Msg.show({
								title:'提示',
								msg:'真的要删除这条记录吗?',
								buttons:Ext.Msg.OKCANCEL,
								fn:function(btn, text){
									if(btn=='ok'){
										this.store.remove(model.selection.record);
									}
								},
								icon:Ext.MessageBox.QUESTION,
								scope:this
							});
	        			},
	        			scope:this
	               }]
	    });
	    this.add(this.grid);	    	   
	}
});