/**
 * url:发送给服务器的地址！
 * params:参数信息，格式为json，例如{id:'1',name:'zhangsan'}
 * loadmask:boolean 如果为true 则创建一个mask
 * compId:需要被罩住的控件的id
 * waitText:被罩住时显示的字符串
 */
function post(config){
	var mask;
	if(config.loadmask){
		mask = new Ext.LoadMask(config.compId, {
		     msg : config.waitText
		});
		mask.show();
	}
	
	Ext.Ajax.request({
		url:config.url,
		params:config.params,
		method:'POST',
		success:function(response, options){
			if(mask)
				 mask.hide();
			var responseArray = Ext.util.JSON.decode(response.responseText); 
			var resp = responseArray.result[0];
			 if(resp.success==true){
				 if(resp.msg)
					 Ext.Msg.alert('提示',resp.msg, function(){if(undefined!=config.store&&null!=config.store)config.store.load();},this);
				 else
					 Ext.Msg.alert('提示','处理完毕!',function(){if(undefined!=config.store&&null!=config.store)config.store.load();},this);
			 }else{
				 Ext.Msg.alert('提示','服务器没有响应，请稍后再试！');
			 }
		}
	});
}

/**
 * 动态刷新flash
 * @param tagName object标签的name属性
 * @param index  要修改对象在getElementsByName()返回的对象集合的位置
 * @param methodName url改变后的方法名称
 * @return 
 */
function changeChart(tagName,index,methodName){
	var objs = document.getElementsByName(tagName);
	var object = objs[index];
	var objectClone = object.cloneNode(true);
	var url1 = object.childNodes[1].value;
	objectClone.childNodes[1].value=url1.replace(url1.substring(url1.lastIndexOf('method%3D'),url1.length),'method%3D'+methodName);
	
	/*var embed = objs[1];
	var embedClone = embed.cloneNode(true);
	var url2 = embed.src;
	embedClone.src=url2.replance(url2.substring(url2.lastIndexOf('method%3D'),url2.length),'method%3D'+methodName);*/

	object.replaceNode(objectClone);
	//embed.replaceNode(embedClone);
}

function submit_failure(form,action){
	Ext.Msg.alert('提示',action.result.msg);
}

/**
 * @param title 页面标题 
 * @param iconCls 页面图标
 * @param url 页面加载请求url
 * @param iframeWidth 内嵌窗口宽
 * @param iframeHeight 内嵌窗口高
 * @param iframeId 提交的form表单ID
 * @return 
 */
function createFormPage(config){	
	var tab = frame.tabs.getActiveTab();
	var panel = new Ext.Panel({		
		title:config.title,
		closable: true,
		iconCls:config.iconCls,
		html:'<center><IFRAME ID='+config.iframeId+' WIDTH='+config.iframeWidth+' HEIGHT='+config.iframeHeight+' FRAMEBORDER=0 SCROLLING=TRUE SRC="'+config.url+'"/></center>'		
		/*bbar:[{
			text:'提交',
			handler:function(){
				var doc = Ext.get(config.iframeId).dom.contentWindow.document;								
				doc.forms[0].submit();
				frame.tabs.remove(panel);
				panel.destroy();
				frame.tabs.activate(tab);
			},
			scope:this
		}]*/
	});
	frame.tabs.add(panel).show();
}

/**
 * 判断variable是否存在
 * @param variable
 * @return
 */
function isUndefined(variable) {
	return typeof variable == 'undefined' ? true : false;
}

