EmployeeFormPanel = Ext.extend(Ext.form.FormPanel, {
    width: 343,
    height: 305,
    padding: 10,
    labelWidth: 70,
    labelAlign: 'right',
    border: false,
    frame: true,
    fileUpload: true,
    initComponent: function() {
        this.defaults = {
            width: 200
        };
        this.items = [
            {xtype:"hidden",name:"id"},
            {
                xtype: 'textfield',
                fieldLabel: '编码',
                anchor: '90%',
                allowBlank: true,
                name: 'code',
                readOnly:true
            },
            {
                xtype: 'textfield',
                fieldLabel: '名称',
                allowBlank: false,
                anchor: '90%',
                name: 'name'
            },
            {
                xtype: 'combo',
                fieldLabel: '文件类型',
                anchor: '90%',
                name: 'fileClass',
                hiddenName: 'fileClass',
                valueField: 'id',
                displayField: 'name',
                emptyText: '请选择...',
                mode: 'local',                
                triggerAction: 'all',
                forceSelection: true,
                allowBlank: false,
                store: this.fileClassStore
            },
            {
                xtype: 'textfield',
                fieldLabel: '所属物料',
                anchor: '90%',
                name: 'departmentName',
                readOnly:true
            },
            {xtype:"hidden",name:"department"},
            {
                xtype: 'fileuploadfield',
                anchor: '90%',
                id: 'file',
                emptyText: '选择一个文件',
                fieldLabel: '文件上传',
                name: 'file',
                buttonText: '',
                buttonCfg: {
                    iconCls: 'uploadIcon'
                }
            },
            {
                xtype: 'combo',
                fieldLabel: '是否禁用',
                anchor: '90%',
                name: 'disabled',
                hiddenName: 'disabled',
                valueField: 'id',
                displayField: 'name',
                store: new Ext.data.Store({     
		            data:[['0','否'],['1','是']], 
		            autoLoad: true,
		            reader:new Ext.data.ArrayReader({}, [
		                  {name: 'id'},
		                  {name: 'name'}
					])
		        }),
                value: '0',
                mode: 'local',
                triggerAction: 'all',
                forceSelection: true
            },
            {
                xtype: 'textfield',
                fieldLabel: '版本号',
                allowBlank: false,
                anchor: '90%',
                name: 'version',
                value: 'A'
            },
            {
                xtype: 'textarea',
                anchor: '90%',
                fieldLabel: '备注',
                name: 'description'
            },{
                xtype: 'fileuploadfield',
                anchor: '90%',
                id: 'extrafile',
                emptyText: '选择一个文件',
                fieldLabel: '附件上传',
                name: 'extrafile',
                buttonText: '',
                buttonCfg: {
                    iconCls: 'uploadIcon'
                }
            },{
                xtype: 'textfield',
                fieldLabel: '引用文件',
                allowBlank: true,
                anchor: '90%',
                name: 'cite'
            }
        ];
        EmployeeFormPanel.superclass.initComponent.call(this);
    }
});
