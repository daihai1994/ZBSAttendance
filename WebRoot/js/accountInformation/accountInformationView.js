/***
 * 查询权限信息
 */
function findAuthority(){
	var company_id = company_id_select;
	$("#authority_vv").datagrid("loading");
	$.post(BASE_PATH + "/AccountInfoController/findAuthority.do", {
		company_id : company_id
	}, function(data) {
		try {
			$("#authority_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#authority_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 新增页面的打开
 */
function addAuthority(){
	$('#authority_add').dialog("open");
	$('#authority_id').val("");
	$('#authority_describe_old').val("");
	$('#authority_describe_add').textbox("setValue","");
}
/***
 * 修改页面的打开
 */
function editAuthority(){
	var data = $("#authority_vv").datagrid("getSelected");
	if(data!=null){
		$('#authority_add').dialog("open");
		$('#authority_id').val(data.id);
		$('#authority_describe_old').val(data.authority_describe);
		$('#authority_describe_add').textbox("setValue",data.authority_describe);
	}else{
		$.messager.alert("提示","请选择要修改的数据!");
	}
}
/***
 * 新增/修改权限的提交
 */
function submitAuthorityAdd(){
	var authority_id = $('#authority_id').val();
	var authority_describe_old = $('#authority_describe_old').val();
	var authority_describe = $('#authority_describe_add').textbox("getValue");
	if(authority_describe==null||authority_describe===""){
		 $.messager.alert("提示","权限不能为空!");
	       return;
	}
	var company_id = company_id_select;
	$.post(BASE_PATH + "/AccountInfoController/submitAuthorityAdd.do", {
		company_id : company_id,
		authority_id : authority_id,
		authority_describe_old : authority_describe_old,
		authority_describe : authority_describe
	}, function(data) {
		debugger;
		try {
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.msg);
			if(temp.result==="success"){
				$('#authority_add').dialog("close");
				findAuthority();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 删除权限信息
 */
function deleteAuthority(){
	var data = $("#authority_vv").datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要删除的数据!");
		return;
	} 
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
				var authority_id = data.id;
				var authority_describe = data.authority_describe;
				$.post(BASE_PATH + "/AccountInfoController/deleteAuthority.do", {
					authority_id : authority_id,
					authority_describe : authority_describe
				}, function(data) {
					try {
						var temp = JSON.parse(data);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							findAuthority();
						}
					} catch (e) {
						console.log(e);
					}
					
				});
			}else{
				$.messager.alert("提示","密码错误!");
			}
	})
}
/***
 * 查询用户信息
 */
function findAccountInfo(){
	clickRowAccountInfo = null;
	var user_id = $('#userId_accountInfo').textbox("getValue");
	var phone_number = $('#phone_accountInfo').textbox("getValue");
	var user_name = $('#userName_accountInfo').textbox("getValue");
	var jobNumber = $('#jobNumber_accountInfo').textbox("getValue");
	var company_id = company_id_select;
	var options = $('#accountInfo_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#accountInfo_vv").datagrid("loading");
	$.post(BASE_PATH + "/AccountInfoController/findAccountInfo.do", {
		bNum : bNum,
		rows : rows,
		company_id : company_id,
		user_id : user_id,
		phone_number : phone_number,
		user_name : user_name,
		jobNumber:jobNumber
	}, function(data) {
		try {
			$("#accountInfo_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#accountInfo_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 新增账号信息
 */
function addAccountInfo(){
	debugger;
	$('#accountInfo_add').dialog("open");
	$('#accountInfo_id').val("");
	var companyInfo = $('#company_main').combobox("getText");
	/**工号初始化**/
	$.post(BASE_PATH + "/AccountInfoController/findJobNumber.do", {
		company_id: company_id_select
	}, function(data) {
		var jobNumber = parseInt(data)+1;
		$('#job_number_accountInfo').numberbox("setValue",jobNumber);
	});
	
	/**公司初始化**/
	$('#company_accountInfo').textbox("setValue",companyInfo);
	/**初始化部门**/
	$('#department_accountInfo').combobox({
		url:BASE_PATH+"/AccountInfoController/findDepartment.do?company_id="+ company_id_select ,
	    onLoadSuccess: function(data) {
	    	debugger;
	    	if(data.length>0){
	    		$(this).combobox("select",data[0].id);
	    	}
	    },
	})
	/**初始化职位**/
	$('#position_accountInfo').combobox({
		 onLoadSuccess: function(data) {
	    	debugger;
	    	if(data.length>0){
	    		$(this).combobox("select",data[0].id);
	    	}
		 }
	})
	
	/**初始化部门2**/
	$('#department_accountInfo1').combobox({
		url:BASE_PATH+"/AccountInfoController/findDepartment.do?company_id="+ company_id_select ,
	    onLoadSuccess: function(data) {
	    	debugger;
	    	
	    },
	})
	/**初始化职位2**/
	$('#position_accountInfo1').combobox({
		 onLoadSuccess: function(data) {
	    	debugger;
	    	
		 }
	})
	
	/**初始化user_id**/
	$.post(BASE_PATH + "/AccountInfoController/findUser_Id.do", {
	}, function(data) {
		var user_id = parseInt(data)+1;
		$('#user_id_accountInfo').textbox("setValue",user_id);
	});
	/**用户姓名**/
	$('#user_name_accountInfo').textbox("setValue","");
	/**密码**/
	$('#pwd_div').show();
	$('#user_pwd_accountInfo').textbox("setValue","");
	/**电话号码**/
	$('#phone_number_accountInfo').textbox("setValue","");
	/**邮箱**/
	$('#mail_address_accountInfo').textbox("setValue","");
	/**联系地址**/
	$('#contact_address_accountInfo').textbox("setValue","");
	/**紧急联系人**/
	$('#emergency_contact_name_accountInfo').textbox("setValue","");
	/**紧急联系人电话**/
	$('#emergency_contact_phone_accountInfo').textbox("setValue","");
	/**权限**/
	$('#role_accountInfo').combobox("setValue","0");
	/**入职时间**/
	$('#entry_time_accountInfo').datebox("setValue","");
}
/***
 * 修改账号信息
 */
function editAccountInfo(){
	debugger;
	var data = clickRowAccountInfo;
	if(data!=null){
		$('#accountInfo_add').dialog("open");
		if(loginUser.company_id==0){//如果公司等于0，是admin账号
			$('#role_accountInfo').combobox("enable");
		}else {
			$('#role_accountInfo').combobox("disable");
		}
		$('#job_number_accountInfo').numberbox("disable");
		$('#accountInfo_id').val(data.id);
		/**公司初始化**/
		$('#company_accountInfo').textbox("setValue",data.company_name);
		/**初始化部门**/
		var department_id = data.department_id;
		var department_idList = [];
		if(department_id!=null){
			department_idList = department_id.split(",");
		}
		$('#department_accountInfo').combobox({
			url:BASE_PATH+"/AccountInfoController/findDepartment.do?company_id="+ data.company_id ,
		    onLoadSuccess: function(data) {
		    	debugger;
		    	if(data.length>0){
		    		if(department_idList.length>0){
		    			if(department_idList[0]!=null){
		    				$(this).combobox("select",parseInt(department_idList[0]));
		    			}
		    		}
		    		
		    	}
		    },
		})
		/**初始化职位**/
		var position_id = data.position_id;
		var position_idList = [];
		if(position_id!=null){
			 position_idList = position_id.split(",");
		}
		
		$('#position_accountInfo').combobox({
			 onLoadSuccess: function(data) {
		    	debugger;
		    	if(data.length>0){
		    		if(position_idList.length>0){
		    			if(position_idList[0]!=null){
		    				for(var i = 0;i<data.length;i++){
		    					if(parseInt(position_idList[0])==data[i].id){
		    						$(this).combobox("select",parseInt(position_idList[0]));
		    						break;
		    					}
		    				}
		    			}
		    		}
		    	}
			 }
		})
		
		/**初始化部门2**/
		$('#department_accountInfo1').combobox({
			url:BASE_PATH+"/AccountInfoController/findDepartment.do?company_id="+data.company_id ,
		    onLoadSuccess: function(data) {
		    	debugger;
		    	if(data.length>0){
		    		if(department_idList.length>1){
		    			if(department_idList[1]!=null){
		    				$(this).combobox("select",parseInt(department_idList[1]));
		    			}
		    		}
		    		
		    	}
		    	
		    },
		})
		/**初始化职位2**/
		$('#position_accountInfo1').combobox({
			 onLoadSuccess: function(data) {
		    	debugger;
		    	if(data.length>0){
		    		if(position_idList.length>1){
		    			if(position_idList[1]!=null){
		    				for(var i = 0;i<data.length;i++){
		    					if(parseInt(position_idList[1])==data[i].id){
		    						$(this).combobox("select",parseInt(position_idList[1]));
		    						break;
		    					}
		    				}
		    			}
		    		}
		    	}
			 }
		})
		
		/**初始化user_id**/
		$('#user_id_accountInfo').textbox("setValue",data.user_id);
		/**用户姓名**/
		$('#user_name_accountInfo').textbox("setValue",data.user_name);
		/**密码**/
		$('#pwd_div').hide();
		$('#user_pwd_accountInfo').textbox("setValue",data.user_pwd);
		/**电话号码**/
		$('#phone_number_accountInfo').textbox("setValue",data.phone_number);
		/**邮箱**/
		$('#mail_address_accountInfo').textbox("setValue",data.mail_address);
		/**联系地址**/
		$('#contact_address_accountInfo').textbox("setValue",data.contact_address);
		/**紧急联系人**/
		$('#emergency_contact_name_accountInfo').textbox("setValue",data.emergency_contact_name);
		/**紧急联系人电话**/
		$('#emergency_contact_phone_accountInfo').textbox("setValue",data.emergency_contact_phone);
		/**权限**/
		if(data.role==0){
			$('#role_accountInfo').combobox("setValue",data.role);
		}else if(data.role==1){
			if(loginUser.company_id==0){
				$('#role_accountInfo').combobox("setValue",data.role);
			}else{
				$('#role_accountInfo').combobox("setText","管理员");
			}
			
		}
		var radios = document.getElementsByName("gender");
	    for(var i=0;i<radios.length;i++){
	    	 radios[i].checked = false;
	        if(radios[i].value==data.gender_id){
	        	radios[i].checked = true;
	        }
	    }
	    /**工号**/
	    $('#job_number_accountInfo').numberbox("setValue",data.job_number);
	    /**入职时间**/
	    $('#entry_time_accountInfo').datebox("setValue",data.entry_time);
	}else{
		$.messager.alert("提示","请选择要修改的账号信息!");
	}
}
/***
 * 提交账号信息
 */
function submitAccountInfoAdd(){
	debugger;
	var accountInfo_id = $('#accountInfo_id').val();//修改信息的ID
	var company_id = $('#company_main').combobox("getValue"); //公司的ID
	var user_id = $('#user_id_accountInfo').textbox("getValue");//user_id
	var job_number =  $('#job_number_accountInfo').numberbox("getValue");
	if(job_number==null||job_number===""){
		 $.messager.alert("提示","工号不能为空!");
	       return;
	}
	var data = clickRowAccountInfo;
	var role;
	if(accountInfo_id==null||accountInfo_id===""){
		role = $('#role_accountInfo').combobox("getValue");//权限1是管理员，0是普通用户
	}else{
		if(loginUser.company_id==0){
			role = $('#role_accountInfo').combobox("getValue");//权限1是管理员，0是普通用户
		}else{
			role = data.role;	
		}
		
	}
	var department = $('#department_accountInfo').combobox("getValue");//部门1的iD
	if(department==null||department===""){
		 $.messager.alert("提示","部门不能为空!");
	       return;
	}
	var position = $('#position_accountInfo').combobox("getValue");//职位1的ID
	if(position==null||position===""){
		 $.messager.alert("提示","职位不能为空!");
	       return;
	}
	var department1 = $('#department_accountInfo1').combobox("getValue");//部门2的id
	var position1 = $('#position_accountInfo1').combobox("getValue");//职位2的ID
	if(department1!=null&&department1!==""){
		if(position1==null||position1===""){
			 $.messager.alert("提示","职位2不能为空!");
		       return;
		}
	}
	var user_name = $('#user_name_accountInfo').textbox("getValue");//名称
	var user_pwd = $('#user_pwd_accountInfo').textbox("getValue");//密码
	if(user_pwd==null||user_pwd===""){
		 $.messager.alert("提示","密码不能为空!");
	       return;
	}
	var entry_time = $('#entry_time_accountInfo').datebox("getValue");
	if(entry_time==null||entry_time===""){
		 $.messager.alert("提示","入职时间不能为空!");
	       return;
	}
	var gender = 1;//性别1男2女3其它
	var radios = document.getElementsByName("gender");
    for(var i=0;i<radios.length;i++){
        if(radios[i].checked){
        	gender = radios[i].value;
            break;
        }
    }
    var phone_number = $('#phone_number_accountInfo').textbox("getValue");//手机号码
    if(phone_number==null||phone_number===""){
		 $.messager.alert("提示","手机号码不能为空!");
	       return;
	}
    if(!phoneNumber(phone_number)){ 
       $.messager.alert("提示","手机号码格式不正确");
       $('#phone_number_accountInfo').textbox("setValue","");
       return;
    } 
    var mail_address = $('#mail_address_accountInfo').textbox("getValue");//邮箱
    if(isNotNull(mail_address)){
//    	if(!mailAddressUtils(mail_address)){ 
//    	　　　　 $.messager.alert("提示","邮箱格式不正确");
//    	 		$('#mail_address_accountInfo').textbox("setValue","");
//         		return;
//    	　　}
	}
    var contact_address = $('#contact_address_accountInfo').textbox("getValue");//联系地址
    var emergency_contact_name = $('#emergency_contact_name_accountInfo').textbox("getValue");//紧急联系人
    var emergency_contact_phone = $('#emergency_contact_phone_accountInfo').textbox("getValue");//紧急联系人号码
    
    
    var oldData = JSON.stringify(data);
    $.post(BASE_PATH + "/AccountInfoController/submitAccountInfoAdd.do", {
    	accountInfo_id : accountInfo_id,
    	user_id: user_id,
    	company_id : company_id,
    	department : department,
    	position : position,
    	department1 : department1,
    	position1 : position1,
    	user_name : user_name,
    	user_pwd : user_pwd,
    	gender : gender,
    	phone_number : phone_number,
    	mail_address : mail_address,
    	contact_address: contact_address,
    	emergency_contact_name : emergency_contact_name,
    	emergency_contact_phone : emergency_contact_phone,
    	role : role,
    	oldData : oldData,
    	job_number : job_number,
    	entry_time:entry_time
	}, function(data) {
		debugger;
		try {
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.msg);
			if(temp.result==="success"){
				$('#accountInfo_add').dialog("close");
				findAccountInfo();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 删除账号信息
 */
function deleteAccountInfo(){
	var data = clickRowAccountInfo;
	if(data==null){
		$.messager.alert("提示","请选择要删除的数据!");
		return;
	} 
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
				var accountInfo_id = data.id;
				var user_id = data.user_id;
				$.post(BASE_PATH + "/AccountInfoController/deleteAccountInfo.do", {
					accountInfo_id : accountInfo_id,
					user_id : user_id
				}, function(data) {
					try {
						var temp = JSON.parse(data);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							findAccountInfo();
						}
					} catch (e) {
						console.log(e);
					}
					
				});
			}else{
				$.messager.alert("提示","密码错误!");
			}
	})

}
/***
 * 提交账号和权限的配置
 */
function addUserAndAuthority(){
	var userData = clickRowAccountInfo;
	if(userData!=null){
		var user_id = userData.user_id;
		var id = userData.id;
		var authorityIdOld = userData.authority_id;
		var authorityId = "";
		var authorityList = $('#authority_vv').datagrid("getChecked");
		if(authorityList!=null){
			for(var i = 0;i<authorityList.length;i++){
				authorityId+=(authorityList[i].id+",");
			}
			if(authorityId.length>0){
				authorityId = authorityId.substring(0,authorityId.length-1);
			}
		}
		$.post(BASE_PATH + "/AccountInfoController/addUserAndAuthority.do", {
			authorityIdOld : authorityIdOld,
			authorityId : authorityId,
			user_id : user_id,
			id : id
		}, function(data) {
			
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					findAccountInfo();
					findAuthority()
				} 
			} catch (e) {
				console.log(e);
			}
			
		});
	}else{
		$.messager.alert("提示","请选择要配置的账号!");
	}
}
/***
 * 修改密码
 */
function updateAccountInfoPasswork(){
	$('#accountInfo_updatePasswork').dialog("open");
	$('#accountInfo_oldPasswork').textbox("setValue","");
	$('#accountInfo_newPasswork').textbox("setValue","");
	$('#accountInfo_newPassworkTwo').textbox("setValue","");
}
/**
 * 提交修改密码
 */
function submitUpdatePasswork(){
	var oldPasswork = $('#accountInfo_oldPasswork').textbox("getValue");
	var newPasswork = $('#accountInfo_newPasswork').textbox("getValue");
	var newPassworkTwo = $('#accountInfo_newPassworkTwo').textbox("getValue");
	if(!isNotNull(oldPasswork)){
		 $.messager.alert("提示","旧密码不能为空!");
	       return;
	}
	if(!isNotNull(newPasswork)){
		 $.messager.alert("提示","新密码不能为空!");
	       return;
	}
	if(!isNotNull(newPassworkTwo)){
		 $.messager.alert("提示","确认密码不能为空!");
	       return;
	}
	if(newPasswork!==newPassworkTwo){
		$.messager.alert("提示","两次密码不一样!");
	       return;
	}
	$.post(BASE_PATH + "/AccountInfoController/submitUpdatePasswork.do", {
		oldPasswork : oldPasswork,
		newPasswork : newPasswork,
		user_id : loginUser.loginName,
		newPassworkTwo : newPassworkTwo
	}, function(data) {
		
		try {
			var temp = JSON.parse(data);
			if(temp.result==="success"){
				$.messager.alert("提示","修改密码成功!");
				setTimeout("window.location.href = window.location.origin+'/ZBSAttendance'","1000");
			} 
		} catch (e) {
			console.log(e);
		}
		
	});
}