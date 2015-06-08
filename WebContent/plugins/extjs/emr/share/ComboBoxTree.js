function removeValue(value, container) {
    if (value.length == 0)
        return '';
            
    //去除前后逗号    
    value = value.replace(/^;/, '').replace(/;$/, '');
    container = container.replace(/^;/, '').replace(/;$/, '');
            
    if (container == value)
    {
        return '';
    }
            
    var sArray = container.split(';');
    for (var i = sArray.length - 1; i >= 0; --i)
    {
        if (sArray[i] == value)
            sArray[i] = undefined;
    }
            
    var result = sArray.join(';');
    //因为undefined会连接成,,所以要将,,换成,            
    result = result.replace(/;;/,';');
    result = result.replace(/^;/, '').replace(/;$/, '');
            
    return result;
}

Ext.ux.ComboBoxTree = function(){
this.treeId = Ext.id()+'-tree';
this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
this.tpl = new Ext.Template('<tpl for="."><div style="height:'+this.maxHeight+'px"><div id="'+this.treeId+'"></div></div></tpl>');
this.store = new Ext.data.SimpleStore({fields:[],data:[[]]});
this.selectedClass = '';
this.mode = 'local';
this.triggerAction = 'all';
this.onSelect = Ext.emptyFn;
this.editable = false;

//all:所有结点都可选中
//exceptRoot：除根结点，其它结点都可选（默认）
//folder:只有目录（非叶子和非根结点）可选
//leaf：只有叶子结点可选
this.selectNodeModel = arguments[0].selectNodeModel || 'exceptRoot';
/*
* single单选 （默认）
* multiple 多选的
*/
this.selectModel = arguments[0].selectModel || 'single'; 

this.addEvents('afterchange');

Ext.ux.ComboBoxTree.superclass.constructor.apply(this, arguments);

}

Ext.extend(Ext.ux.ComboBoxTree,Ext.form.ComboBox, {
initComponent: function() {
	this.tree.on('beforeload',function(node,deep,anim){
		this.isexpandnode=true;               
	},this);	
	
	this.tree.on('load',function(node,deep,anim){
		this.isexpandnode=false;               
	},this);
	
	/*this.tree.on('click',function(node,deep,anim){
		alert(1);
		this.isexpandnode=true;               
	},this);
	
	this.tree.on('expandnode',function(node,deep,anim){
		this.isexpandnode=false;               
	},this);
	
	this.tree.on('collapsenode',function(node,deep,anim){
		this.isexpandnode=false;               
	},this);*/	
	
	this.on('specialkey',function(t,e){alert('blur');},this);
	
	},
expand : function(){   
   Ext.ux.ComboBoxTree.superclass.expand.call(this);
   this.isexpandnode=true; //isexpandnode 是控制combo是否collapse的标识
   if(this.tree.rendered){
    return;
   }

   Ext.apply(this.tree,{height:this.maxHeight, border:false, autoScroll:false});
   if(this.tree.xtype){
    this.tree = Ext.ComponentMgr.create(this.tree, this.tree.xtype);
   }
   this.tree.render(this.treeId);
  
   var root = this.tree.getRootNode();
   if(!root.isLoaded())
    root.reload();

   this.tree.on('click',this.setSingleValue,this); 
   this.tree.on('checkchange',this.setMultiValue, this);     
    },
collapse : function(){
    if(this.isexpandnode)
    	return false;
    else
    	Ext.ux.ComboBoxTree.superclass.collapse.call(this);
    },
setSingleValue : function(node){
   this.isexpandnode=false;
   this.collapse();
   var selModel = this.selectNodeModel;
   var isLeaf = node.isLeaf();
  
   if((node == this.root) && selModel != 'all'){ 
    return;
   }else if(selModel=='folder' && isLeaf){
    return;
   }else if(selModel=='leaf' && !isLeaf){
    return;
   } 
  
   this.node = node;
        var text = node.text;
        this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = node.id;
        }
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = node.id;
        
    },
setMultiValue : function(node,check){
    	this.isexpandnode=false;
        this.node = node;
        var text = node.text;
        this.lastSelectionText = text;
        var display=text;
        var val=node.id;
        
        if(!node.isLeaf()){
    return; 
   }
   if(!check){
    display=removeValue(node.text,this.getRawValue());
    val=removeValue(node.id,this.getValue());   
   }else{
    if(this.getValue()==''){
     display=text;
           val=node.id; 
    }else if(this.getRawValue().indexOf(node.text)>-1){
     return; 
    }else{
     //this.setValue(this.getValue()+';'+node.text);// 设置option值
     display=this.getRawValue()+";"+text;
           val=this.getValue()+";"+node.id; 
    }
   }
   // 选中树节点后的触发事件 
   //this.fireEvent('treeselected', node); 
          
        Ext.form.ComboBox.superclass.setValue.call(this, display);
        if(this.hiddenField){
            this.hiddenField.value = val;
        }
        this.value = val;
    },
    setComboValue : function(id,text){
      
        //this.lastSelectionText = text;
        if(this.hiddenField){
            this.hiddenField.value = id;
        }
        Ext.form.ComboBox.superclass.setValue.call(this, text);
        this.value = id;
    },
    getValue : function(){
    return typeof this.value != 'undefined' ? this.value : '';
    },

getNode : function(){
   return this.node;
},

clearValue : function(){
   Ext.ux.ComboBoxTree.superclass.clearValue.call(this);
        this.node = null;
    },

// private
    destroy: function() {
   Ext.ux.ComboBoxTree.superclass.destroy.call(this);
   Ext.destroy([this.node,this.tree]);
   delete this.node;
    }
});

Ext.reg('combotree', Ext.ux.ComboBoxTree);






/*
 * 用法：
 * 
1、在自己的js中创建目录树

//定义一个树形
var contenttree = new Ext.tree.TreePanel({ 
   rootVisible:false, 
   autoScroll:false,
   autoHeight:true,
        loader: new Ext.tree.TreeLoader({dataUrl:departmentUrl}),     
        root : new Ext.tree.AsyncTreeNode({id:'0',text:'根结点'})    
});

2、创建下拉树ComboBoxTree对象

var departmentList = new Ext.ux.ComboBoxTree({ 
   hiddenName:'department', 
      width:200, 
   fieldLabel:'营业部', 
   autoScroll:false,
   autoLoad:true, 
      //listWidth:300, 这是设置下拉框的宽度，默认和comBoxTree的宽度相等   
      tree : contenttree,    
      selectNodeModel:'leaf' //只有选叶子时，才设置值 
});
 
 */

/*
 * 下拉树原理
 * 
this.distCombo = new Ext.form.ComboBox({   
store:new Ext.data.SimpleStore({fields:[],data:[[]]}),   
editable:false,   
mode: 'local',   
triggerAction:'all',   
maxHeight: 200,   
tpl: "<tpl for='.'><div style='height:200px'><div id='tree'></div></div></tpl>",   
selectedClass:'',   
onSelect:Ext.emptyFn   
});   
var tree = new Ext.tree.TreePanel({   
loader: {url: 'district.do?cmd=district_tree'},   
border:false,   
root:new Ext.tree.AsyncTreeNode({text: '模板根目录',id:'0'})   
});   
tree.on('click',function(node){   
  this.distCombo.setValue(node.text);   
  this.distCombo.collapse();   
});   
this.distCombo.on('expand',function(){   
tree.render('tree');   
});   
//this.distCombo.render('comboxWithTree');*/


