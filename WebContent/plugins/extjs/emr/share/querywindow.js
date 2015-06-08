/**
 * 自定义条件查询的窗口
 * conditionsURL ：参数为获取查询项目的列表
 * parentStore : 调用它的父窗口的store，用于联动刷新
 */
QueryWindow = Ext.extend(Ext.Window,{	
	title:'自定义过滤条件',
	layout:'fit',
	closeAction:'hide',
	sub : function(){
		var count = this.querypanel.store.getCount();
		if(count>0){
			var jsonBegin = '{"rows":[';
	    	var jsonEnd = ']}';
	    	var j = 0;
	    	for(var i=0;i<count;i++){
	    		var record = this.querypanel.store.getAt(i);
	    		var join = record.get("join");
	    		var item = record.get("item");
	    		var expression = record.get("expression");
	    		var value = record.get("value");
	    		if(null!=join&&''!=join&&null!=item&&''!=item&&null!=expression&&''!=expression&&null!=value&&''!=value){
	    			jsonBegin += '{"join":"'+join+'","item":"'+item+'","expression":"'+expression+'","value":"'+value+'"}';
	    			j++;
	    		}
	    		if(i<count-1)
	    			jsonBegin+=',';
	    	}
	    	if(0!=j){          //判断是否有正确格式的条件
	    		jsonBegin+=jsonEnd;
		    	this.parentStore.baseParams['resp']=jsonBegin;
		    	this.parentStore.baseParams['queryType']='filter';
		    	this.parentStore.removeAll();
		    	this.parentStore.load();
	    	}	    	
		}
		this.hide();
	},
	reset : function(){
		this.querypanel.store.removeAll();
	},
	initComponent : function(){
		this.conditions = new Ext.data.JsonStore({
			url: this.conditionsURL,
			root:"result",
			fields:["ID","FTabl","FFiel","FValue","FCapt","FPyyy","FType","FEnab"],
			listeners:{
				'load':{fn:function(storeThis,records,options){
					storeThis.add_btn.setDisabled(false);
					if(this.initDatas&&null!=this.initDatas&&this.querypanel.store.getCount()==0)
						this.querypanel.store.insert(0,this.initDatas);
				},scope:this},
				'loadexception':function (){
					Ext.Msg.alert("提示","加载数据失败，请刷新后在试!");
				}
			}
		});
		this.querypanel = new QueryPanel({
			conditions : this.conditions
		});
		QueryWindow.superclass.initComponent.call(this);
		this.add(this.querypanel);
		this.addButton({text:'&nbsp;&nbsp;提&nbsp;&nbsp;交&nbsp;&nbsp;',handler:this.sub,scope:this});
	    this.addButton({text:'&nbsp;&nbsp;清&nbsp;&nbsp;空&nbsp;&nbsp;',handler:this.reset,scope:this});
	    this.on('show',function(t){this.conditions.load();},this);
	}
})