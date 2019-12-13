/**
 * 加班审批查询需要审批
 */
function findoverTime(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#overTime_userId').textbox("getValue");
	var bt = $('#bt_overTime').datebox("getValue");
	var et = $('#et_overTime').datebox("getValue");
	var options = $('#overTime_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#overTime_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findoverTime.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#overTime_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#overTime_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}

/**
 * 加班审批界面
 */
function auditoverTime(){
	var data = $("#overTime_vv").datagrid("getSelected");
	if(data!=null){
		$('#auditoverTime_dialog').dialog("open");
		$('#auditoverTime_user_name').textbox("setValue",data.user_name);
		$('#auditoverTime_over_type').textbox("setValue",data.overTimeType);
		$('#auditoverTime_start_time').textbox("setValue",data.start_time);
		$('#auditoverTime_stop_time').textbox("setValue",data.stop_time);
		$('#auditoverTime_day').textbox("setValue",data.day);
		$('#auditoverTime_hour').textbox("setValue",data.hour);
		$('#auditoverTime_result').textbox("setValue",data.result);
		$('#auditoverTime_remarks').textbox("setValue",data.remarks);
		var radios = document.getElementsByName("auditoverTime_radio");
	    for(var i=0;i<radios.length;i++){
	    	radios[i].checked = false;
	    }
	    $('#auditoverTime_audit_remarks').textbox("setValue","");
	}else{
		$.messager.alert("操作提示","请先选择要审批的信息","error");
	}
}
/**
 * 加班审批提交
 */
function submitAuditoverTime(){
	var data = $("#overTime_vv").datagrid("getSelected");
	if(data!=null){
		var audit_id = data.audit_id;//审批人详情信息ID
		var id = data.id;//加班详情信息ID
		var result_id = data.result_id;//申请状态
		var audit_status = 0;//审批结果，1通过，2不通过
		var radios = document.getElementsByName("auditoverTime_radio");
	    for(var i=0;i<radios.length;i++){
	    	if(radios[i].checked){
	    		audit_status = radios[i].value;
	    		break;
	    	}
	    }
	    var audit_resmarks = $('#auditoverTime_audit_remarks').textbox("getValue");//审批备注
	    $.post(BASE_PATH + "/OutAttendanceController/submitAuditoverTime.do", {
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
					$('#auditoverTime_dialog').dialog("close");
					findoverTime();
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
 * 加班审批记录的查询
 */
function findauditoverTime_historical_records(){
	debugger;
	var user_id = loginUser.loginName;//当前登入人的user_id
	var applicant_user_id = $('#overTime_userId_historical_records').textbox("getValue");
	var bt = $('#bt_overTime_historical_records').datebox("getValue");
	var et = $('#et_overTime_historical_records').datebox("getValue");
	var options = $('#overTime_historical_records_vv').datagrid('getPager').data("pagination").options;
	var page = options.pageNumber;// 当前页数
	if (page == 0) {
		page = 1;
	}
	rows = options.pageSize;// 每页的记录数（行数）
	bNum = (page - 1) * rows;
	$("#overTime_historical_records_vv").datagrid("loading");
	$.post(BASE_PATH + "/OutAttendanceController/findauditoverTime_historical_records.do", {
		bNum : bNum,
		rows : rows,
		bt : bt,
		et : et,
		user_id : user_id,
		applicant_user_id : applicant_user_id
	}, function(data) {
		try {
			$("#overTime_historical_records_vv").datagrid("loaded");
			var temp = JSON.parse(data);
			$("#overTime_historical_records_vv").datagrid("loadData",temp);
		} catch (e) {
			console.log(e);
		}
		
	});
}