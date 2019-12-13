/***
 * 查询公司
 */
function findCompanyInfo(){
	debugger;
	var company_name = $('#company_name').textbox("getValue");//公司名称筛选
	var options = $('#companyInfo_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#companyInfo_vv").datagrid("loading");
	$.post(BASE_PATH + "/CompanyController/findCompanyInfo.do", {
		bNum : bNum,
		rows : rows,
		company_name : company_name
	}, function(data) {
		try {
			$("#companyInfo_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#companyInfo_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 新增页面的打开
 */
function addCompanyInfo(){
	$('#company_add').dialog("open");
	$("#company_id").val("");
	$("#company_name_old").val("");
	$('#company_name_add').textbox("setValue","");//公司名称
	$('#company_duty_paragraph_add').textbox("setValue","");//税号
	$('#company_address_add').textbox("setValue","");//地址
	//$('#company_phone_number_add').textbox("setValue","");//号码
	$('#company_landline_number_add').textbox("setValue","");//座机号码
	$('#company_official_website_add').textbox("setValue","");//官网
	$('#company_fax_add').textbox("setValue","");//传真
	$('#company_mail_add').textbox("setValue","");//邮箱
	$('#company_bank_account_add').textbox("setValue","");//卡号
	$('#bank_div').hide();
	$('#bankType_div').hide();
	$('#bankAccountRes')[0].innerText="*必填";
}
/***
 * 提交公司新增
 */
function submitCompanyAdd(){
	debugger;
	var company_id = $("#company_id").val();
	var company_name = $('#company_name_add').textbox("getValue");
	var company_name_old = $("#company_name_old").val();
	if(company_name==null||company_name===""){
		$.messager.alert("提示","公司名称不能够为空!");
		return;
	}
	var duty_paragraph = $('#company_duty_paragraph_add').textbox("getValue");//税号
	var address = $('#company_address_add').textbox("getValue");//地址
	//var phone_number = $('#company_phone_number_add').textbox("getValue");//号码
	var landline_number = $('#company_landline_number_add').textbox("getValue");//座机号码
	var official_website = $('#company_official_website_add').textbox("getValue");//官网
	var fax = $('#company_fax_add').textbox("getValue");//传真
	var mail = $('#company_mail_add').textbox("getValue");//邮箱
	var enable_application = 0;//0不启用，1启用
	var radios = document.getElementsByName("enable_application");
    for(var i=0;i<radios.length;i++){
        if(radios[i].checked){
        	enable_application = radios[i].value;
            break;
        }
    }
	var bank_account = $('#company_bank_account_add').textbox("getValue");//卡号
	var resBank = bankCardAttribution(bank_account);
	var bankCode ="";//银行编号
	var bankName = "";//银行名称
	var cardType = "";//卡类型编号
	var cardTypeName = "";//卡类型
	if(!isNotNull(duty_paragraph)){
		$.messager.alert("提示","税号不能为空!");
		return;
	}
	if(!isNotNull(address)){
		$.messager.alert("提示","地址不能为空!");
		return;
	}
//	if(!isNotNull(phone_number)){
//		$.messager.alert("提示","号码不能为空!");
//		return;
//	}else{
//		if(!phoneNumber(phone_number)){
//			$.messager.alert("提示","号码格式不正确!");
//			return;
//		}
//	}
	if(!isNotNull(landline_number)){
		$.messager.alert("提示","座机不能为空!");
		return;
	}else{
		if(!landlineNumber(landline_number)){
			$.messager.alert("提示","座机号码格式不正确!");
			return;
		}
	}
	if(isNotNull(official_website)){
		if(!isURL(official_website)){
			$.messager.alert("提示","官网地址不正确!");
			return;
		}
	}
	if(isNotNull(fax)){
		if(!isFax(fax)){
			$.messager.alert("提示","传真格式不正确!");
			return;
		}
	}
	if(isNotNull(mail)){
		if(!mailAddress(mail)){
			$.messager.alert("提示","邮箱格式不正确!");
			return;
		}
	}
	if(typeof(resBank)==="object"){
		bankCode = resBank.bankCode;
		bankName = resBank.bankName;
		cardType = resBank.cardType;
		cardTypeName = resBank.cardTypeName;
	}else{
		$.messager.alert("提示","银行卡号不正确!");
		return;
	}
	var dataCompany = $("#companyInfo_vv").datagrid("getSelected");
	var dataCompanyStrng = JSON.stringify(dataCompany);
	$.post(BASE_PATH + "/CompanyController/submitCompanyAdd.do", {
		company_id : company_id,
		company_name : company_name,
		company_name_old : company_name_old,
		duty_paragraph : duty_paragraph,
		address : address,
		landline_number : landline_number,
		official_website : official_website,
		fax : fax,
		mail : mail,
		bank_account : bank_account,
		bankCode : bankCode,
		bankName : bankName,
		cardType : cardType,
		cardTypeName : cardTypeName,
		dataCompanyStrng:dataCompanyStrng,
		enable_application:enable_application
	}, function(data) {
		debugger;
		try {
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.msg);
			if(temp.result==="success"){
				$('#company_add').dialog("close");
				findCompanyInfo();
				$('#company_main').combobox({
					url:BASE_PATH+"/LoginController/compang_main.do?company_id="+ loginUser.company_id
				});
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 修改公司名称
 */
function editCompanyInfo(){
	debugger;
	var data = $("#companyInfo_vv").datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要修改的数据!");
		return;
	}
	$('#company_add').dialog("open");
	$("#company_id").val(data.id);
	$("#company_name_old").val(data.company_name);
	$('#company_name_add').textbox("setValue",data.company_name);
	$('#company_duty_paragraph_add').textbox("setValue",data.duty_paragraph);//税号
	$('#company_address_add').textbox("setValue",data.address);//地址
	//$('#company_phone_number_add').textbox("setValue",data.phone_number);//号码
	$('#company_landline_number_add').textbox("setValue",data.landline_number);//座机号码
	$('#company_official_website_add').textbox("setValue",data.official_website);//官网
	$('#company_fax_add').textbox("setValue",data.fax);//传真
	$('#company_mail_add').textbox("setValue",data.mail);//邮箱
	$('#company_bank_account_add').textbox("setValue",data.bank_account);//卡号
	var radios = document.getElementsByName("enable_application");
    for(var i=0;i<radios.length;i++){
    	 radios[i].checked = false;
        if(radios[i].value==data.enable_application){
        	radios[i].checked = true;
        }
    }
	if(isNotNull(data.bank_account)){
		console.log(bankCardAttribution(data.bank_account));
		var res = bankCardAttribution(data.bank_account);
		if(typeof(res)==="object"){
			$('#bankAccountRes')[0].innerText="√";
			$('#bank_div').show();
			$('#bankType_div').show();
			$('#company_bank_add').textbox("setValue",res.bankName);
			$('#company_bankType_add').textbox("setValue",res.cardTypeName);
		}else{
			$('#bank_div').hide();
			$('#bankType_div').hide();
			$('#bankAccountRes')[0].innerText="格式不正确";
		}
	}else{
			$('#bank_div').hide();
			$('#bankType_div').hide();
			$('#bankAccountRes')[0].innerText="*必填";
	}
}
/***
 * 删除公司名称
 */
function deleteCompanyInfo(){
	var data = $("#companyInfo_vv").datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要删除的数据!");
		return;
	} 
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
				var company_id = data.id;
				var company_name = data.company_name;
				$.post(BASE_PATH + "/CompanyController/deleteCompanyInfo.do", {
					company_id : company_id,
					company_name : company_name
				}, function(data) {
					try {
						var temp = JSON.parse(data);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							findCompanyInfo();
							$('#company_main').combobox({
								url:BASE_PATH+"/LoginController/compang_main.do?company_id="+ loginUser.company_id
							});
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
 * 打开公司修改删除密码页面
 */
function editCompanyDeletePwd(){
	debugger;
	var data = $("#companyInfo_vv").datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要修改的数据!");
		return;
	}
	$('#company_deletePwd_edit').dialog("open");
	$('#company_deletePwd').textbox("setValue","");
}
/***
 * 提交公司修改删除密码
 */
function submitCompanydeletePwd_edit(){
	var data = $("#companyInfo_vv").datagrid("getSelected");
	var company_id = data.id;
	var company_name = data.conpany_name;
	var old_deletePwd = data.deletePwd;
	var deletePwd = $('#company_deletePwd').textbox("getValue");
	if(deletePwd==null||deletePwd===""){
		$.messager.alert("提示","密码不能为空!");
		return;
	}
	$.post(BASE_PATH + "/CompanyController/submitCompanydeletePwd_edit.do", {
		company_id : company_id,
		old_deletePwd : old_deletePwd,
		deletePwd : deletePwd,
		company_name : company_name
	}, function(data) {
		try {
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.msg);
			if(temp.result==="success"){
				findCompanyInfo();
				$('#company_main').combobox({
					url:BASE_PATH+"/LoginController/compang_main.do?company_id="+ loginUser.company_id
				});
			}
		} catch (e) {
			console.log(e);
		}
		
		
	});
}
/***
 * 查询部门的信息
 */
function findDepartMent(){
	debugger;
	var companySelect = $("#companyInfo_vv").datagrid("getSelected");
	var company_id = companySelect.id;
	$('#departMent_vv').datagrid("loading");
	$.post(BASE_PATH + "/CompanyController/findDepartMent.do", {
		company_id : company_id
	}, function(data) {
		$("#departMent_vv").datagrid("loaded");
		var temp = JSON.parse(data);
		$("#departMent_vv").datagrid("loadData",temp);
	});
}
/***
 * 新增部门页面的打开
 */
function addDepartMent(){
	debugger;
	$('#department_add').dialog("open");
	$('#department_id').val("");
	$('#department_name_old').val("");
	$('#department_priority_old').val("");
	$('#department_name_add').textbox("setValue","");
	var priority = 1;
	var datas = $("#departMent_vv").datagrid("getData");
	if(datas==null){
		priority = 1;
	}else{
		if(datas.rows.length<=0){
			priority = 1;
		}else{
			priority = datas.rows[datas.total-1].priority+1;
			if(priority>99){
				priority = 99;
			}
		}
	}
	$('#department_priority_add').numberspinner("setValue",priority);
}
/***
 * 提交新增部门信息
 */
function submitDepartmentAdd(){
	var department_id = $('#department_id').val();
	var department_name_old = $('#department_name_old').val();
	var department_priority_old = $('#department_priority_old').val();
	var department_name = $('#department_name_add').textbox("getValue");
	var department_priority = $('#department_priority_add').numberspinner("getValue");
	var companySelect = $("#companyInfo_vv").datagrid("getSelected");
	if(companySelect==null){
		$.messager.alert("提示","请先选择公司或找管理员新增公司!");
		return;
	}
	var company_id = companySelect.id;
	if(department_name==null||department_name===""){
		$.messager.alert("提示","部门名称不能为空!");
		return;
	}
	$.post(BASE_PATH + "/CompanyController/submitDepartmentAdd.do", {
		department_id : department_id,
		department_name_old : department_name_old,
		department_priority_old : department_priority_old,
		department_name:department_name,
		department_priority:department_priority,
		company_id:company_id
	}, function(data) {
		debugger;
		try {
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.msg);
			if(temp.result==="success"){
				$('#department_add').dialog("close");
				findDepartMent();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 修改部门信息打开页面
 */
function editDepartMent(){
	var data = $('#departMent_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要修改的部门!");
		return;
	}
	debugger;
	$('#department_add').dialog("open");
	$('#department_id').val(data.department_id);
	$('#department_name_old').val(data.department);
	$('#department_priority_old').val(data.priority);
	$('#department_name_add').textbox("setValue",data.department);
	$('#department_priority_add').numberspinner("setValue",data.priority);
}


/***
 * 删除部门名称
 */
function deleteDepartMent(){
	var data = $('#departMent_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要删除的部门!");
		return;
	}
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
				var department_id = data.department_id;
				var department_name = data.department;
				$.post(BASE_PATH + "/CompanyController/deleteDepartMent.do", {
					department_id : department_id,
					department_name : department_name
				}, function(data) {
					try {
						var temp = JSON.parse(data);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							findDepartMent();
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
 * 提交修改部门优先级别
 */
function confirmPriority(){
	debugger;
	var dataList = $('#departMent_vv').datagrid("getData");
	var rows ;
	var list = [];
	if(dataList!=null){
		rows = dataList.rows;
		for(var i = 0;i<rows.length;i++){
			var obj = new Object();
			obj.department = rows[i].department;
			obj.id = rows[i].department_id;
			obj.oldpriority = rows[i].priority;
			obj.priority = (i+1);
			list.push(obj);
		}
		if(list.length>0){
			var liststring = JSON.stringify(list);
		}
		$.post(BASE_PATH + "/CompanyController/confirmPriority.do", {
			liststring : liststring
		}, function(data) {
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					findDepartMent();
				}
			} catch (e) {
				console.log(e);
			}
			
		});
	}
}
/**
 * 修改职位排序
 */
function positionPriority(){
	debugger;
	var dataList = $('#position_vv').datagrid("getData");
	var rows ;
	var list = [];
	if(dataList!=null){
		rows = dataList.rows;
		for(var i = 0;i<rows.length;i++){
			var obj = new Object();
			obj.position = rows[i].position;
			obj.id = rows[i].position_id;
			obj.oldpriority = rows[i].priority;
			obj.priority = (i+1);
			list.push(obj);
		}
		if(list.length>0){
			var liststring = JSON.stringify(list);
		}
		$.post(BASE_PATH + "/CompanyController/positionPriority.do", {
			liststring : liststring
		}, function(data) {
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					findPosition();
				}
			} catch (e) {
				console.log(e);
			}
			
		});
	}
}

/***
 * 查询职位信息
 */
function findPosition(){
	debugger;
	var departmentSelect = $("#departMent_vv").datagrid("getSelected");
	var department_id = departmentSelect.department_id;
	$('#position_vv').datagrid("loading");
	$.post(BASE_PATH + "/CompanyController/findPosition.do", {
		department_id : department_id
	}, function(data) {
		$("#position_vv").datagrid("loaded");
		var temp = JSON.parse(data);
		$("#position_vv").datagrid("loadData",temp);
	});
}
/***
 * 新增职位信息页面打开
 */
function addPosition(){
	$('#position_add').dialog("open");
	$('#position_id').val("");
	$('#position_name_old').val("");
	$("#position_name_add").textbox("setValue","");
}
/***
 * 新增修改职位信息的提交
 */
function submitPositionAdd(){
	var position_id = $('#position_id').val();
	var position_name_old = $('#position_name_old').val();
	var position_name = $('#position_name_add').textbox("getValue");
	var departMentSelect = $("#departMent_vv").datagrid("getSelected");
	if(departMentSelect==null){
		$.messager.alert("提示","请先选择部门或新增部门!");
		return;
	}
	var departMent_id = departMentSelect.department_id;
	if(position_name==null||position_name===""){
		$.messager.alert("提示","职位名称不能为空!");
		return;
	}
	$.post(BASE_PATH + "/CompanyController/submitPositionAdd.do", {
		position_id : position_id,
		position_name_old : position_name_old,
		position_name : position_name,
		departMent_id:departMent_id
	}, function(data) {
		debugger;
		try {
			var temp = JSON.parse(data);
			$.messager.alert("提示",temp.msg);
			if(temp.result==="success"){
				$('#position_add').dialog("close");
				findPosition();
			}
		} catch (e) {
			console.log(e);
		}
		
	});
}
/***
 * 修改职位信息页面打开
 */
function editPosition(){
	var data = $('#position_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要修改的职位信息!");
		return;
	}
	$('#position_add').dialog("open");
	$('#position_id').val(data.position_id);
	$('#position_name_old').val(data.position);
	$("#position_name_add").textbox("setValue",data.position);
}
/***
 * 删除职位信息
 */
function deletePosition(){
	var data = $('#position_vv').datagrid("getSelected");
	if(data==null){
		$.messager.alert("提示","请选择要删除的职位!");
		return;
	}
	$.messager.prompt('请示','请输入删除密码？',function(r){
		debugger;
		if(r==deletePwd){
				var position_id = data.position_id;
				var position_name = data.position;
				$.post(BASE_PATH + "/CompanyController/deletePosition.do", {
					position_id : position_id,
					position_name : position_name
				}, function(data) {
					try {
						var temp = JSON.parse(data);
						$.messager.alert("提示",temp.msg);
						if(temp.result==="success"){
							findPosition();
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
/**
 * 查询职位下的人员信息
 */
function findPositionUser(){
	debugger;
	var res = $('#position_vv').datagrid("getSelected");
	var position_id = res.position_id;
	$.post(BASE_PATH + "/CompanyController/findPositionUser.do", {
		position_id : position_id
	}, function(data) {
		$("#positionUser_vv").datagrid("loaded");
		var temp = JSON.parse(data);
		$("#positionUser_vv").datagrid("loadData",temp);
	});
}
/**
 * 人员的排序
 */
function positionUserPriority(){
	debugger;
	var dataList = $('#positionUser_vv').datagrid("getData");
	var rows ;
	var list = [];
	if(dataList!=null){
		rows = dataList.rows;
		for(var i = 0;i<rows.length;i++){
			var obj = new Object();
			obj.user_name = rows[i].user_name;
			obj.id = rows[i].id;
			obj.oldpriority = rows[i].priority;
			obj.priority = (i+1);
			list.push(obj);
		}
		if(list.length>0){
			var liststring = JSON.stringify(list);
		}
		$.post(BASE_PATH + "/CompanyController/positionUserPriority.do", {
			liststring : liststring
		}, function(data) {
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					findPositionUser();
				}
			} catch (e) {
				console.log(e);
			}
			
		});
	}
}