LoginWindow = Ext.extend(Ext.Window, {
    title: '用户登录',
    iconCls:'logoutIcon',
    closeAction:"close",
    width: 273,
    height: 165,
    modal:true,
    layout: 'fit',
    buttonAlign:'right',
    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                id: 'login_form',
                border: false,
                //frame : true,
                padding: 20,
                labelWidth: 40,
                items: [
                    {
                        xtype: 'textfield',
                        fieldLabel: '用户',
                        anchor: '100%',
                        name: 'account',
                        allowBlank: false
                    },
                    {
                        xtype: 'textfield',
                        fieldLabel: '密码',
                        anchor: '100%',
                        inputType: 'password',
                        allowBlank: false,
                        name: 'password'
                    }
                ]
            }
        ];
        
        this.buttons = [
            {
            	text:'提交',
	        	handler: function(){
            		var fp = Ext.getCmp('login_form');
            		fp.form.submit({
            			url:"emrUser.do?cmd=validate1",
        	            method:'POST',
        	            waitText:'正在提交。。。',
        	            success:function(form,action){
            				this.close();
            				window.location.reload();
            			},
        	            scope:this
            		});
            	},
        		scope: this	
            },{
            	text:'取消',
	        	handler: function(){
        			this.close();
        		},
        		scope: this	
            }
        ];
        LoginWindow.superclass.initComponent.call(this);
    }
});
