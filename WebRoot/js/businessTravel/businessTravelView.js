/**
 * 出差审批查询需要审批
 */
function findbusinessTraveI(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#businessTraveI_userId').textbox("getValue");
	var bt = $('#bt_businessTraveI').datebox("getValue");
	var et = $('#et_businessTraveI').datebox("getValue");
	var options = $('#businessTraveI_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#businessTraveI_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findbusinessTraveI.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#businessTraveI_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#businessTraveI_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}

/**
 * 出差审批界面
 */
function auditbusinessTraveI(){
	var data = $("#businessTraveI_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditbusinessTraveI_dialog').dialog("open");
		$('#auditbusinessTraveI_user_name').textbox("setValue",data.user_name);
		$('#auditbusinessTraveI_address').textbox("setValue",data.address);
		$('#auditbusinessTraveI_start_time').textbox("setValue",data.start_time);
		$('#auditbusinessTraveI_stop_time').textbox("setValue",data.stop_time);
		$('#auditbusinessTraveI_day').textbox("setValue",data.day);
		$('#auditbusinessTraveI_hour').textbox("setValue",data.hour);
		$('#auditbusinessTraveI_result').textbox("setValue",data.result);
		$('#auditbusinessTraveI_remarks').textbox("setValue",data.remarks);
		var radios = document.getElementsByName("auditbusinessTraveI_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditbusinessTraveI_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}

/**
 * 出差审批提交
 */
function submitAuditbusinessTraveI(){
	var data = $("#businessTraveI_vv").datagrid("getSelected");
	if(data!=null){
		var audit_id = data.audit_id;//审批人详情信息ID
		var id = data.id;//出差详情信息ID
		var result_id = data.result_id;//申请状态
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditbusinessTraveI_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditbusinessTraveI_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/OutAttendanceController/submitAuditbusinessTraveI.do", {
	    	audit_id:audit_id,
	    	id : id,
	    	result_id : result_id,
	    	audit_status : audit_status,
	    	audit_resmarks : audit_resmarks
		}, function(data) {
			debugger;
			try {
				var temp = JSON.parse(data);
				$.messager.alert("提示",temp.msg);
				if(temp.result==="success"){
					$('#auditbusinessTraveI_dialog').dialog("close");
					findbusinessTraveI();
					searchNews();
				}
			} catch (e) {
				console.log(e);
			}
			
		});
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}
/**
 * 出差审批记录的查询
 */
function findauditbusinessTraveI_historical_records(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#businessTraveI_userId_historical_records').textbox("getValue");
	var bt = $('#bt_businessTraveI_historical_records').datebox("getValue");
	var et = $('#et_businessTraveI_historical_records').datebox("getValue");
	var options = $('#businessTraveI_historical_records_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#businessTraveI_historical_records_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findauditbusinessTraveI_historical_records.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#businessTraveI_historical_records_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#businessTraveI_historical_records_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}